package com.contextu.al.quizgatekeeper

import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.runtime.SideEffect
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.contextu.al.R
import com.contextu.al.model.customguide.ContextualContainer
import com.contextu.al.quizgatekeeper.enums.QuizStatus
import com.contextu.al.quizgatekeeper.model.Answer
import com.contextu.al.quizgatekeeper.model.Question
import com.contextu.al.quizgatekeeper.model.QuizGK
import com.contextu.al.quizgatekeeper.viewModel.QuizGateKeeperViewModel
import com.google.gson.Gson

class QuizGatekeeperGuideBlock
{


    private var mQuizGK: QuizGK? = null
    private lateinit var typography: Typography
    private var primaryColor: Color? = null

    @Composable
    fun show(activity: AppCompatActivity, contextualContainer: ContextualContainer?)
    {

        val mViewModel: QuizGateKeeperViewModel by activity.viewModels()
        val mQuizGK = mViewModel.quizGK.collectAsState().value
        val mQuizState = mViewModel.quizState.collectAsState().value
        var currentQuestionIndex by remember { mutableIntStateOf(0) }

        var parsingError by remember { mutableStateOf("") }
        primaryColor = MaterialTheme.colorScheme.primary
        typography = MaterialTheme.typography;

        var userPoints: Int by remember { mutableStateOf(0) }
        LaunchedEffect(key1=contextualContainer?.guidePayload?.guide?.extraJson) {
            userPoints=0
            currentQuestionIndex=0
            when (val result = parseJson(contextualContainer?.guidePayload?.guide?.extraJson))
            {
                is DataState.Error ->
                {
                    parsingError = result.errorMsg
                    mViewModel.updateState(){
                        it.apply { quizStatus=QuizStatus.ERROR }
                    }
                }

                is DataState.Success ->
                {
                    mViewModel.updateQuiz(result.data)
//                    mViewModel.startTimer()

                }
            }

        }
        Dialog(onDismissRequest = { /*TODO*/ }) {
            Card(modifier = Modifier.fillMaxWidth()) {


                Log.d("TAG", "Update CARD: ${mQuizState.quizStatus}")
                if (mQuizState.quizStatus == QuizStatus.NONE)
                {
                    LoadingScreen()
                    return@Card
                }
                if (mQuizState.quizStatus == QuizStatus.LOCKED)
                {
                    LockedOut()
                    return@Card
                }
                if (mQuizState.quizStatus == QuizStatus.ERROR)
                {
                    ErrorScreen(detailMsg = parsingError)
                    return@Card
                }
                if (mQuizState.quizStatus == QuizStatus.RESULT)
                {
                    ResultScreen(userPoints, mViewModel.quizQuestionsSize){

                    }
                    return@Card
                }
                if (mQuizState.quizStatus == QuizStatus.STARTED)
                {
                    val currentQuestion = mQuizGK?.questions?.getOrNull(currentQuestionIndex)
                    QuizHeader(modifier = Modifier.padding(10.dp),mQuizState.retriesLeft,mQuizState.time)
                    Text(modifier = Modifier.align(Alignment.End).padding(end=10.dp, top = 5.dp),text = "Question ${currentQuestionIndex+1} of ${mViewModel.quizQuestionsSize}", style = typography.titleSmall)

                    Quiz(modifier = Modifier.padding(10.dp),currentQuestion) { selectedAnswer ->

                        if (selectedAnswer?.row?.correct == true) userPoints += 1;

                        if (currentQuestionIndex < mViewModel.quizQuestionsSize-1)
                        {
                            currentQuestionIndex++;
                        }
                        else
                        {
                            mViewModel.updateState(){
                                it.apply { quizStatus=QuizStatus.RESULT }
                            }
                        }
                    }
                }


            }
        }

    }


    fun Long.toMinutesAndSeconds(): Pair<Long, Long> {
        val seconds = this / 1000
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return Pair(minutes, remainingSeconds)
    }
    @Composable
    fun QuizHeader(modifier:Modifier = Modifier,retriesLeft:Int,time:Long)
    {

        val timeRem=time.toMinutesAndSeconds();
        Row(modifier = modifier){
            Text(text = "Retries left : ${retriesLeft}",  style = typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "${timeRem.first}:${timeRem.second} min", style = typography.titleMedium)
        }

        HorizontalDivider(modifier = Modifier, thickness = 2.dp, color = primaryColor!!)

    }

    @Composable
    fun LockedOut()
    {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(text = "You are locked !", style = typography.titleLarge.copy(fontWeight = FontWeight.Bold))
            Image(modifier = Modifier.size(80.dp), painter = painterResource(id = R.drawable.baseline_lock_24), contentDescription = "Lock")

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
    fun ResultScreen(userPoints: Int, totalPoints: Int,onRestart:()->Unit)
    {

        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(text = "Result !", modifier = Modifier.align(Alignment.Start), style = typography.headlineSmall.copy(fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = "Your scored ${userPoints}/${totalPoints} !", style = typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.height(10.dp))
            ElevatedButton(enabled = true, modifier = Modifier.width(200.dp), shape = RoundedCornerShape(10.dp), onClick = {
                onRestart()

            }) {
                Text("Restart Quiz", style = typography.titleMedium)
            }
        }
    }

    @Composable
    fun Quiz(modifier:Modifier = Modifier,question: Question?, onAnswerDone: (answer: Answer?) -> Unit)
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
                Text("Next", style = typography.titleMedium)
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
            .drawBehind {
                drawRoundRect(
                    color = primaryColor!!, topLeft = Offset(0f, 0f), size = Size(size.width, size.height), cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx()), style = Stroke(width = 5.0f, cap = StrokeCap.Round)
                )
            }
            .padding(horizontal = 10.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = answer?.row?.label
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
        LockedOut()
    }

    @Preview(showBackground = true)
    @Composable
    fun showFailPreview()
    {
        typography = MaterialTheme.typography;
        ResultScreen(0, 0){

        }
    }

    private fun parseJson(json: String?): DataState<QuizGK>
    {
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