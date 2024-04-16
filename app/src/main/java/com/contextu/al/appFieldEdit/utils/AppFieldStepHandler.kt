package com.contextu.al.appFieldEdit.utils

import com.contextu.al.Contextual
import com.contextu.al.Player
import com.contextu.al.previewMode.PreviewMode
import kotlin.math.max

class AppFieldStepHandler(val feedId:String)
{


    fun moveToNextStep()
    {

        if (hasNextStep())
        {

            var currentStep: Int = getCurrentStep()
            updateStepCount(++currentStep)
            Player().playNextTour(feedId)

        }
    }

     fun nextStepOrDismiss(onDismiss:()->Unit)
    {
        if(hasNextStep())
            moveToNextStep()
        else
        {
            onDismiss.invoke()
        }
    }
    fun getCurrentStep() =   max(Player().getStepCount(feedId), 0)

    fun updateStepCount(currentStep: Int)
    {

        Player().setCurrentStep(currentStep)
        if (!PreviewMode.running)
        {
            Contextual.state.player.feeds.realMode[feedId]?.stepCount = currentStep
            Contextual.state.player.feeds.realMode.apply()
        }
    }

    fun getTotalStepsCount() = Player().getTourLength(feedId)
    fun hasNextStep():Boolean {

        val step: Int = max(
                Player().getStepCount(feedId),
                0
        )
        val tourLength: Int = getTotalStepsCount()
        return (step + 1 < tourLength && tourLength > 1)
    }
}