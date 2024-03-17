package com.contextu.al.ratingbar

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import com.contextu.al.model.customguide.ContextualContainer
import com.contextu.al.model.customguide.Feedback
import com.contextu.al.common.extensions.getIntegerTag
import com.contextu.al.common.extensions.setTag
import com.trafi.ratingseekbar.RatingSeekBar
import com.google.gson.JsonObject as JsonObject1

class NPSRatingBarGuideBlock : ComponentActivity()
{


    private val NPS_RATING_COMPLETED = "is_nps_rating_completed"

    @Composable
    fun show(
        contextualContainer: ContextualContainer,
        onCancel: () -> Unit,
        onSubmit: (progress: Int) -> Unit,
    )
    {
        val mGuide = contextualContainer.guidePayload.guide
        var mProgress = remember { 0 }
        var isShowing by remember { mutableStateOf(false) }
        LaunchedEffect(key1 =mGuide, block = {
            if (contextualContainer.getIntegerTag(NPS_RATING_COMPLETED, -1) == 1)
            {
                isShowing = false
                onCancel()
                return@LaunchedEffect
            }
            isShowing = true

        })
        if (isShowing)
        {
            Dialog(onDismissRequest = {
                isShowing = false
                contextualContainer.guidePayload.dismissGuide.onClick(null)
                onCancel()
            }) {

                Card(modifier = Modifier.padding(10.dp)) {
                    Column(
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = mGuide.titleText.text
                                ?: "", textAlign = TextAlign.Center, fontSize = MaterialTheme.typography.headlineSmall.fontSize
                        )

                        if (mGuide.feedBackMessage != null || mGuide.contentText.text != null)
                        {
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                text = mGuide.feedBackMessage ?: mGuide.contentText.text
                                ?: "", textAlign = TextAlign.Center, fontSize = MaterialTheme.typography.titleMedium.fontSize
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                        RatingSeekBarWrapper { newProgress ->
                            mProgress = newProgress
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Row {
                            TextButton(onClick = {
                                isShowing = false
                                onCancel()
                            }) {
                                contextualContainer.guidePayload.dismissGuide.onClick(null)
                                Text(
                                    text = mGuide.buttons.prevButton?.text
                                        ?: "Cancel", fontSize = MaterialTheme.typography.titleMedium.fontSize
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            ElevatedButton(onClick = {

                                val jsonObject = JsonObject1()
                                val updatedMultiChoice = arrayListOf<String>()
                                jsonObject.addProperty("user_rating", mProgress)
                                contextualContainer.operations.submitFeedback(
                                    contextualContainer.guidePayload.guide.feedID, Feedback(
                                        contextualContainer.guidePayload.guide.feedBackTitle
                                            ?: "", updatedMultiChoice, jsonObject
                                    )
                                )

                                isShowing = false
                                onSubmit(mProgress)
                                contextualContainer.guidePayload.complete.onClick(null)
                                contextualContainer.guidePayload.nextStep.onClick(null)
                                contextualContainer.setTag(NPS_RATING_COMPLETED,1)
                            }) {
                                Text(
                                    text = mGuide.buttons.nextButton?.text
                                        ?: "Submit", fontSize = MaterialTheme.typography.titleMedium.fontSize
                                )
                            }
                        }

                    }


                }

            }

        }


    }

    @Composable
    fun RatingSeekBarWrapper(onProgressChange: (progress: Int) -> Unit)
    {
        AndroidView(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 16.dp), factory = { context ->
            RatingSeekBar(context).apply {
                max = 5
                this.setOnSeekBarChangeListener(object : RatingSeekBar.OnRatingSeekBarChangeListener
                {
                    override fun onProgressChanged(p0: RatingSeekBar?, p1: Int)
                    {
                        onProgressChange(p1)
                    }

                })
            }
        })
    }


    @Preview
    @Composable
    fun preview()
    { //        show(title = "Rating Bar Title", "Rating Bar Message", "Submit", "Cancel")

    }
}