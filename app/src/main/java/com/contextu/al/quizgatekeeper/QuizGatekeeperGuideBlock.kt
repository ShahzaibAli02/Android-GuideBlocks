package com.contextu.al.quizgatekeeper

import android.os.Build
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.contextu.al.R
import com.contextu.al.extensions.clicked
import com.contextu.al.extensions.complete
import com.contextu.al.model.customguide.ContextualContainer
import com.contextu.al.quizgatekeeper.enums.QuizStatus
import com.contextu.al.quizgatekeeper.model.Answer
import com.contextu.al.quizgatekeeper.model.Question
import com.contextu.al.quizgatekeeper.model.QuizGK
import com.contextu.al.quizgatekeeper.viewModel.QuizGateKeeperViewModel
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import okhttp3.internal.notify
import java.time.OffsetDateTime
import java.util.Date

class QuizGatekeeperGuideBlock
{


    private var mQuizGK: QuizGK? = null
    private lateinit var typography: Typography
    private var primaryColor: Color? = null
    private val TAG="QuizGatekeeperGuideBlock"
    private val TAG_QUIZ_LOCKED="is_quiz_user_locked"

    private val TAG_QUIZ_COMPLETED="is_quiz_user_completed"
    //    val testJson="{\n" + "  \"guideBlockKey\": \"QuizGateKeeper\",\n" + "  \"Questions\": [\n" + "    {\n" + "      \"question\": \"How would you do X?\",\n" + "      \"answers\": [\n" + "        {\n" + "          \"label\": \"By clicking the edit profile\",\n" + "          \"correct\": false\n" + "        },\n" + "        {\n" + "          \"label\": \"By praying to my fave deity\",\n" + "          \"correct\": false\n" + "        },\n" + "        {\n" + "          \"label\": \"By entering the dish and selecting Fave\",\n" + "          \"correct\": true\n" + "        }\n" + "      ]\n" + "    },\n" + "    {\n" + "      \"question\": \"What planet are you on?\",\n" + "      \"answers\": [\n" + "        {\n" + "          \"label\": \"Earth\",\n" + "          \"correct\": true\n" + "        },\n" + "        {\n" + "          \"label\": \"Betelgeuse Seven\",\n" + "          \"correct\": false\n" + "        },\n" + "        {\n" + "          \"label\": \"Golgafrincham\",\n" + "          \"correct\": false\n" + "        }\n" + "      ]\n" + "    }\n" + "  ],\n" + "  \"fail\": {\n" + "    \"quiz_action\": \"Restart_Quiz\",\n" + "    \"allow_screen_access\": false,\n" + "    \"attempts\": 2,\n" + "    \"lockout_seconds\": 15,\n" + "    \"setTag\": {\n" + "      \"key\": \"Quiz_fail_datetime\",\n" + "      \"value\": \"@now\"\n" + "    }\n" + "  },\n" + "  \"pass\": {\n" + "    \"quiz_action\": {\n" + "      \"setTag\": {\n" + "        \"key\": \"Quiz_pass_datetime\",\n" + "        \"value\": \"@now\"\n" + "      },\n" + "      \"allow_screen_access\": true\n" + "    }\n" + "  }\n" + "}";
    @Composable
    fun show(activity: AppCompatActivity, mContextualContainer: ContextualContainer?,onDone:(result:Boolean)->Unit)
    {

        val mViewModel: QuizGateKeeperViewModel by activity.viewModels()
        val mQuizGK by  mViewModel.quizGK.collectAsState()
        val mQuizState by  mViewModel.quizState.collectAsState()
        var currentQuestionIndex by remember { mutableIntStateOf(0) }
        var isDialogShowing by remember { mutableStateOf(true) }
        var parsingError by remember { mutableStateOf("") }
        primaryColor = MaterialTheme.colorScheme.primary
        typography = MaterialTheme.typography;
        var userPoints: Int by remember { mutableStateOf(0) }

        LaunchedEffect(key1=mContextualContainer?.guidePayload?.guide?.extraJson) {
            userPoints=0
            currentQuestionIndex=0

            when (val result = parseJson( mContextualContainer?.guidePayload?.guide?.extraJson))
            {
                is DataState.Error ->
                {
                    parsingError = result.errorMsg
                    mViewModel.updateState(){
                        it.apply { quizStatus=QuizStatus.FAIL }
                    }
                }

                is DataState.Success ->
                {

                    if(mContextualContainer?.tagManager?.getTag("extraJson")?.firstOrNull()?.tagStringValue==mContextualContainer?.guidePayload?.guide?.extraJson || mContextualContainer?.tagManager?.getTag(TAG_QUIZ_COMPLETED)?.firstOrNull()?.tagIntegerValue==1)
                    {
                        isDialogShowing=false
                        return@LaunchedEffect
                    }
                    if(mContextualContainer?.tagManager?.getTag(TAG_QUIZ_LOCKED)?.firstOrNull()?.tagIntegerValue==1)
                    {

                        mViewModel.updateQuiz(result.data,QuizStatus.FAIL)
                        return@LaunchedEffect
                    }

                    mViewModel.updateQuiz(result.data,QuizStatus.STARTED)
                    mContextualContainer?.clicked()
                    mContextualContainer?.tagManager?.setStringTag("extraJson", mContextualContainer.guidePayload?.guide?.extraJson.toString())

                    //                    mViewModel.startTimer()

                }
            }

        }


        if(isDialogShowing)
        {
            Dialog(onDismissRequest = { }) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    if (mQuizState.quizStatus == QuizStatus.NONE)
                    {
                        LoadingScreen()
                        return@Card
                    }
                    if (mQuizState.quizStatus == QuizStatus.FAIL)
                    {


                        mQuizGK?.fail?.actionData?.let {
                            mContextualContainer?.tagManager?.setStringTag("Quiz_fail_datetime", getCurrentTime().toString())
                            if(it.key.isNullOrBlank().not())
                            {
                                mContextualContainer?.tagManager?.setStringTag(it.key!!,it.value.toString())
                            }
                        }

                        mContextualContainer?.tagManager?.setNumericTag(TAG_QUIZ_LOCKED,1)
                        LockedOut(mQuizGK?.fail?.actionData?.lockoutSeconds?.toLong()?:0L)
                        {
                            mContextualContainer?.tagManager?.setNumericTag(TAG_QUIZ_LOCKED,0)
                            mContextualContainer?.tagManager?.setNumericTag(TAG_QUIZ_COMPLETED,1)
                            isDialogShowing=false
                        }
                        return@Card
                    }
                    if(mQuizState.quizStatus==QuizStatus.PASS)
                    {
                        ResultScreen(userPoints,mViewModel.quizQuestionsSize)
                        {
                            //DO ACTION NEED TO BE DONE IN PASS
                            mQuizGK?.pass?.actionData?.let {

                                mContextualContainer?.tagManager?.setStringTag("Quiz_pass_datetime", getCurrentTime().toString())
                                mContextualContainer?.tagManager?.setNumericTag(TAG_QUIZ_COMPLETED,1)
                                if(it.key.isNullOrBlank().not())
                                {
                                    mContextualContainer?.tagManager?.setStringTag(it.key!!,it.value.toString())
                                }
                            }
                            mContextualContainer?.complete()
                            isDialogShowing=false;
                        }
                        return@Card
                    }
                    if (mQuizState.quizStatus == QuizStatus.ERROR)
                    {
                        ErrorScreen(detailMsg = parsingError)
                        return@Card
                    }
                    if (mQuizState.quizStatus == QuizStatus.RESULT)
                    {
                        ResultScreen(userPoints, mViewModel.quizQuestionsSize,mQuizState.retriesLeft,mViewModel.quizAttemptsLimit){

                            //CHECKING IF USER HAVE ANY RETRIES LEFT
                            if(mQuizState.retriesLeft!=0)
                            {
                                //REST QUIZ
                                userPoints = 0
                                currentQuestionIndex = 0
                                mViewModel.updateState() {
                                    it.apply {
                                        retriesLeft--;
                                        quizStatus = QuizStatus.STARTED
                                    }
                                }
                            }
                            else
                            {
                                //SHOW FAIL SCREEN
                                mViewModel.updateState() {
                                    it.apply {
                                        quizStatus = QuizStatus.FAIL
                                    }
                                }
                            }


                        }
                        return@Card
                    }
                    if (mQuizState.quizStatus == QuizStatus.STARTED)
                    {
                        val currentQuestion = mQuizGK?.questions?.getOrNull(currentQuestionIndex)
                        QuizHeader(modifier = Modifier.padding(10.dp),mQuizState.retriesLeft)
                        Text(modifier = Modifier
                            .align(Alignment.End)
                            .padding(end = 10.dp, top = 5.dp),text = "Question ${currentQuestionIndex+1} of ${mViewModel.quizQuestionsSize}", style = typography.titleSmall)

                        Quiz(modifier = Modifier.padding(10.dp),currentQuestion,isLastQuestion = currentQuestionIndex >= mViewModel.quizQuestionsSize-1) { selectedAnswer ->

                            if (selectedAnswer?.correct == true)
                                userPoints += 1;

                            if (currentQuestionIndex < mViewModel.quizQuestionsSize-1)
                            {
                                currentQuestionIndex++;
                            }
                            else
                            {

                                //IF User passed show Pass Screen else show screen showing user his points
                                val newQuizStatus=if(userPoints==mViewModel.quizQuestionsSize)  QuizStatus.PASS else if(mQuizState.retriesLeft!=0) QuizStatus.RESULT else QuizStatus.FAIL
                                mViewModel.updateState(){
                                    it.apply { quizStatus=newQuizStatus }
                                }
                            }
                        }
                    }


                }
            }
        }
        else{
            onDone(userPoints==mQuizGK?.questions?.size)
        }


    }
    fun getCurrentTime():Long?
    {
        return Date().time
    }
    fun Long.toHoursMinutesAndSeconds(): String {
        val totalSeconds =  this
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val remainingSeconds = totalSeconds % 60
        return String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds)
    }

    @Composable
    fun QuizHeader(modifier:Modifier = Modifier,retriesLeft:Int)
    {


        Row(modifier = modifier){
            Text(text = "Attempts left : ${retriesLeft}",  style = typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.weight(1f))

        }

        HorizontalDivider(modifier = Modifier, thickness = 2.dp, color = primaryColor!!)

    }

    @Composable
    fun LockedOut(lockOutTime:Long,onUnlocked:()->Unit)
    {
        Log.d("TAG", "LockedOut: ${lockOutTime}")
        var timeToUnlock by remember { mutableStateOf(lockOutTime) }
        if(timeToUnlock<=0L)
        {
            onUnlocked()
            return
        }
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(text = "You are locked !", style = typography.titleLarge.copy(fontWeight = FontWeight.Bold))
            LottieAnimationViewWrapper(modifier = Modifier.size(200.dp), R.raw.lottie_locked )
            Text(text = "Time to unlock : "+timeToUnlock.toHoursMinutesAndSeconds(), style = typography.titleMedium)
        }
        LaunchedEffect(Unit) {
            repeat(lockOutTime.toInt()) {
                delay(1000) // Delay for 1 second
                timeToUnlock -= 1
            }
        }
    }


    @Composable
    fun ErrorScreen(
        message: String = "Please Wait",
        detailMsg: String = "",
        modifier: Modifier = Modifier,
    )
    {
        Column(
            modifier = modifier
                .padding(10.dp)
                .fillMaxWidth()

        ) {

            Text(text = "Something went wrong ! ", style = typography.titleLarge.copy(fontWeight = FontWeight.Bold))
            Text(text = detailMsg, style = typography.bodyMedium)
            Spacer(modifier = Modifier.height(10.dp))
            Image(
                colorFilter = ColorFilter.tint(color = Color.Red), modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.CenterHorizontally), painter = painterResource(id = R.drawable.baseline_lock_24), contentDescription = "Lock"
            )

        }
    }



    @Composable
    fun LoadingScreen(message: String = "Please Wait", modifier: Modifier = Modifier)
    {
        Column(
            modifier = modifier
                .padding(10.dp)
                .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(text = message, style = typography.headlineSmall.copy(fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.height(5.dp))
            CircularProgressIndicator(modifier = Modifier.size(50.dp))
        }
    }

    @Composable
    fun ResultScreen(userPoints: Int, totalPoints: Int,remainingAttempts:Int=0,totalAttempts:Int=0,onRestart:()->Unit)
    {

        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val isFail=userPoints!=totalPoints

            LottieAnimationViewWrapper(modifier = Modifier.size(200.dp),if(isFail) R.raw.lottie_fail else  R.raw.lottie_done)
            Spacer(modifier = Modifier.height(5.dp))
            Row {
                Text(text = "Status : ", style = typography.titleMedium)
                Text(text =if(isFail) "Failed" else "Passed", color = if(isFail) colorResource(id = R.color.fail_quiz_color) else colorResource(id = R.color.pass_quiz_color),style = typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            }
            Text(modifier = Modifier.align(Alignment.CenterHorizontally),text = "Your scored ${userPoints}/${totalPoints} !", style = typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.height(10.dp))
            ElevatedButton(enabled = true, modifier = Modifier.width(200.dp), shape = RoundedCornerShape(10.dp), onClick = {
                onRestart()
            }) {
                Text(if(isFail) "Restart Quiz" else "Submit", style = typography.titleMedium)
            }
            Spacer(modifier = Modifier.height(10.dp))
            if(isFail) Text(modifier=Modifier.align(Alignment.End),text = "${remainingAttempts}/${totalAttempts} Attempts remaining.", style = typography.titleMedium.copy(fontSize = 10.sp,fontWeight = FontWeight.Bold))

        }
    }

    @Composable
    fun LottieAnimationViewWrapper(modifier:Modifier = Modifier,lottieID: Int) {
        AndroidView(
            modifier = modifier,
            factory = { context ->
                LottieAnimationView(context).apply {
                    setAnimation(lottieID)
                    playAnimation()
                    repeatCount= LottieDrawable.INFINITE
                }
            },
            update = { view ->
                view.setAnimation(lottieID)
                view.playAnimation()
            }
        )
    }
    @Composable
    fun Quiz(modifier:Modifier = Modifier,question: Question?,isLastQuestion:Boolean=false, onAnswerDone: (answer: Answer?) -> Unit)
    {

        var answer: Answer? by remember { mutableStateOf(null) }
        LaunchedEffect(key1 = question) {
            answer = null
        }
        Column(
            modifier = modifier
                .fillMaxWidth()
        ) {
            Text(
                text = question?.question
                    ?: "", style = typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )

            question?.answers?.forEach { ans ->
                val isSelected = answer == ans
                QuizOptionRow(ans, isSelected) { selectedAns ->
                    answer = selectedAns
                }
            }

            //            Spacer(modifier = Modifier.height(10.dp))
            //            QuizOptionRow()
            //            Spacer(modifier = Modifier.height(10.dp))
            //            QuizOptionRow()
            //            Spacer(modifier = Modifier.height(10.dp))
            //            QuizOptionRow()


            Spacer(modifier = Modifier.height(20.dp))
            ElevatedButton(enabled = answer != null, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp), onClick = {
                onAnswerDone(answer)
            }) {
                Text(if(isLastQuestion) "Submit" else "Next", style = typography.titleMedium)
            }
        }
    }

    @Composable
    fun QuizOptionRow(answer: Answer?, isSelected: Boolean, onClick: (selectedAns: Answer?) -> Unit)
    {

        Spacer(modifier = Modifier.height(15.dp))
        Row(modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .clickable {
                onClick(answer)
            }
            .drawBehind {
                drawRoundRect(
                    color = primaryColor!!, topLeft = Offset(0f, 0f), size = Size(size.width, size.height), cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx()), style = Stroke(width = 5.0f, cap = StrokeCap.Round)
                )
            }
            .padding(horizontal = 10.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(

                text = answer?.label
                    ?: "", style = typography.titleSmall, modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(10.dp))
            RadioButton(modifier = Modifier.size(10.dp), selected = isSelected, onClick = { //                answer?.isSelected?.value = true
                onClick(answer)
            })
        }

    }

    @Preview(showBackground = true)
    @Composable
    fun showMainPreview()
    {


        typography = MaterialTheme.typography; //        Quiz() //        ErrorScreen(detailMsg = "Failed to parse extra_json ! ")
        //        show(activity=AppCompatActivity(),contextualContainer = null)
    }

    @Preview(showBackground = true)
    @Composable
    fun showLockedOutPreview()
    {
        typography = MaterialTheme.typography;
        LockedOut(0L){

        }
    }

    @Preview(showBackground = true)
    @Composable
    fun showFailPreview()
    {
        typography = MaterialTheme.typography;
        ResultScreen(0, 0,0,0){

        }
    }
    private fun parseJson(json: String?): DataState<QuizGK>
    {
        Log.d("TAG", "json: ${json}")
        if (mQuizGK == null && json != null)
        {
            runCatching {
                mQuizGK = Gson().fromJson(json, QuizGK::class.java)
            }.onFailure {
                return DataState.Error(errorMsg = it.localizedMessage ?: "Failed to parse json")
            }
        }
        return DataState.Success(mQuizGK!!)
    }

}