package com.contextu.al.multiselectsurvey

import android.app.Activity
import androidx.appcompat.app.AlertDialog
import com.contextu.al.R
import com.contextu.al.common.extensions.clickedOutside
import com.contextu.al.common.extensions.complete
import com.contextu.al.model.customguide.ContextualContainer
import com.contextu.al.model.customguide.Feedback
import com.contextu.al.multiselectsurvey.model.MultiSelectSurveyFeedbackModel
import com.google.gson.Gson
import com.google.gson.JsonObject

class MultiSelectSurveyGuideBlock (val activity:Activity,val contextualContainer: ContextualContainer,)
{

    val multiSelectSurvey = "MultiSelectSurvey"
    var hasShown = false
    fun show()
    {
        if(contextualContainer.guidePayload.guide.guideBlock.contentEquals(multiSelectSurvey) && !hasShown){
            val (feedBackData, checkedItems) = parseData()
            hasShown = true
            val multiChoiceItems = feedBackData.c.toTypedArray()
            AlertDialog.Builder(activity)
                .setTitle(contextualContainer.guidePayload.guide.feedBackTitle ?: "")
                .setMultiChoiceItems(multiChoiceItems, checkedItems) { dialog, which, isChecked ->
                }
                .setPositiveButton("Submit") { dialog, which ->
                    val jsonObject = JsonObject()
                    val updatedMultiChoice = arrayListOf<String>()
                    checkedItems.forEachIndexed { index, check ->
                        if (check){
                            updatedMultiChoice.add(multiChoiceItems[index])
                        }
                    }
                    contextualContainer.complete()
                    jsonObject.addProperty("any-other-custom-data", "Example custom data")
                    contextualContainer.operations.submitFeedback(contextualContainer.guidePayload.guide.feedID,
                        Feedback(contextualContainer.guidePayload.guide.feedBackTitle ?: "", updatedMultiChoice, jsonObject)
                    )

                    dialog.dismiss()
                    if (feedBackData.i == 1) {
                        promptUserForInput("Please explain why you chose this ?")
                    }
                }
                .setOnDismissListener {
                    contextualContainer.clickedOutside()
                }
                .create()
                .show()
        }
    }

    private fun parseData(): Pair<MultiSelectSurveyFeedbackModel, BooleanArray>
    {
        val feedBackData = Gson().fromJson(
            contextualContainer.guidePayload.guide.feedBackData, MultiSelectSurveyFeedbackModel::class.java
        )
        val checkedItems = List<Boolean>(feedBackData.c.size) { false }.toBooleanArray()
        return Pair(feedBackData, checkedItems)
    }

    private fun promptUserForInput(textTitle: String){
        AlertDialog.Builder(activity)
            .setTitle(textTitle)
            .setView(R.layout.dialog_guideblock)
            .setPositiveButton("Send") { dialog, which ->

                dialog.dismiss()
            }
            .create()
            .show()
    }


}