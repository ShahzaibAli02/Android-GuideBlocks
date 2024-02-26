package com.contextu.al.quizgatekeeper

import android.app.Activity
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModel
import com.contextu.al.R
import com.contextu.al.model.customguide.ContextualContainer
import com.contextu.al.quizgatekeeper.model.Answer
import com.contextu.al.quizgatekeeper.model.Question
import com.contextu.al.quizgatekeeper.model.QuizGK
import com.contextu.al.quizgatekeeper.viewModel.QuizGateKeeperViewModel
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import java.nio.file.WatchEvent

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
        primaryColor = MaterialTheme.colorScheme.primary
        typography = MaterialTheme.typography;
        var isLocked by remember { mutableStateOf(false) }
        SideEffect {
            val parseResult = parseJson(contextualContainer?.guidePayload?.guide?.extraJson)
            mViewModel.updateQuiz(parseResult)
        }
        Dialog(onDismissRequest = { /*TODO*/ }) {
            Card(modifier = Modifier.fillMaxWidth()) {

                if (mQuizGK == null)
                {
                    LoadingScreen()
                    return@Card
                }

                if (mQuizGK.isLocked)
                {
                    LockedOut()
                    return@Card
                }
                if (mQuizGK.isError)
                {
                    ErrorScreen(detailMsg = mQuizGK.errorMessage)
                    return@Card
                }
                Quiz(mQuizGK.questions?.getOrNull(mQuizGK.currentIndex))

            }
        }

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
    fun FailScreen()
    {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(text = "Failed !", style = typography.headlineSmall.copy(color = Color.Red, fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = "Your scored 0/10 !", style = typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.height(10.dp))
            ElevatedButton(enabled = false, modifier = Modifier.width(200.dp), shape = RoundedCornerShape(10.dp), onClick = { /*TODO*/ }) {
                Text("Restart Quiz", style = typography.titleMedium)
            }
        }
    }

    @Composable
    fun Quiz(question: Question?)
    {

        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
        ) {
            Row {
                Text(text = "Retries left : 3", style = typography.titleSmall)
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "3:00 min", style = typography.titleSmall)
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), thickness = 2.dp, color = primaryColor!!)
            Text(text = question?.question?:"", style = typography.titleLarge.copy(fontWeight = FontWeight.Bold))

            question?.answers?.forEach {answer->
                QuizOptionRow(answer){

                }
            }

//            Spacer(modifier = Modifier.height(10.dp))
//            QuizOptionRow()
//            Spacer(modifier = Modifier.height(10.dp))
//            QuizOptionRow()
//            Spacer(modifier = Modifier.height(10.dp))
//            QuizOptionRow()


            Spacer(modifier = Modifier.height(20.dp))

            ElevatedButton(enabled = false, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp), onClick = { /*TODO*/ }) {
                Text("Next", style = typography.titleMedium)
            }
        }
    }

    @Composable
    fun QuizOptionRow(answer: Answer?,onClick:()->Unit)
    {

        Row(modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .drawBehind {
                drawRoundRect(
                    color = primaryColor!!, topLeft = Offset(0f, 0f), size = Size(size.width, size.height), cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx()), style = Stroke(width = 5.0f, cap = StrokeCap.Round)
                )
            }
            .padding(horizontal = 10.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(text =answer?.row?.label?:"", style = typography.titleSmall, modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(10.dp))
            RadioButton(modifier = Modifier.size(10.dp), selected = answer?.isSelected!!.value, onClick = {
                answer?.isSelected?.value=true
                onClick()
            })
        }

    }

    @Preview(showBackground = true)
    @Composable
    fun showMainPreview()
    {


        typography = MaterialTheme.typography;
//        Quiz() //        ErrorScreen(detailMsg = "Failed to parse extra_json ! ")
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
        FailScreen()
    }

    private fun parseJson(json: String?):QuizGK
    {
        if (mQuizGK == null && json != null)
        {
            runCatching {
                mQuizGK = Gson().fromJson(json, QuizGK::class.java)
            }.onFailure {
                Log.e("QuizGatekeeperGuideBlock", "Failed to parse extra_json ERROR : ${it.localizedMessage}")
                return QuizGK().apply {
                    isError=true
                    errorMessage=it.localizedMessage?:""
                }
            }
        }
        return mQuizGK!!
    }

}