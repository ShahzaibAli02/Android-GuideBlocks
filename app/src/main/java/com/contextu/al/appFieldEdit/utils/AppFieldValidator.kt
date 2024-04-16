package com.contextu.al.appFieldEdit.utils

import android.util.Log
import com.contextu.al.ContextualBase
import com.contextu.al.Temp
import com.contextu.al.appFieldEdit.interfaces.AppFieldValidationInterface
import com.contextu.al.appFieldEdit.model.AppFieldEditValidation

class AppFieldValidator: AppFieldValidationInterface
{
    companion object{
        private var  appFieldValidation: AppFieldValidationInterface? = null
        private var validSteps:HashMap<Int,Boolean> = hashMapOf()
    }
    fun clear()
    {
        validSteps.clear()
    }

    fun isStepValid(value:Int):Boolean {
        var step=value+1
        if(getValidation(step)?.regex.isNullOrEmpty())
            return true
       return validSteps[step]==true
    }
    fun init(value: AppFieldValidationInterface)
    {
        appFieldValidation = value

    }

    override fun onDismiss()
    {
        appFieldValidation?.onDismiss()
    }

    override fun nextOrDismiss()
    {
        appFieldValidation?.nextOrDismiss()
    }

    override fun setValidValue(step:Int,value: Boolean) {
        validSteps[step] = value
    }

    override fun getValidation(step: Int): AppFieldEditValidation? {
       return  appFieldValidation?.getValidation(step)
    }


    override fun getCurrentStep(): Int =   appFieldValidation?.getCurrentStep()?:0

    override fun getTotalSteps(): Int =  appFieldValidation?.getTotalSteps()?:0

    override fun getGuide(): ContextualBase? =  appFieldValidation?.getGuide()


}