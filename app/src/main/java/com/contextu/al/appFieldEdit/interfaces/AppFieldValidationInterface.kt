package com.contextu.al.appFieldEdit.interfaces

import com.contextu.al.ContextualBase
import com.contextu.al.appFieldEdit.model.AppFieldEditValidation

interface AppFieldValidationInterface
{

    fun onDismiss()
    fun nextOrDismiss()
    fun setValidValue(step:Int,value:Boolean){}
    fun getValidation(step:Int): AppFieldEditValidation? = null
    fun getCurrentStep():Int
    fun getTotalSteps():Int
    fun getGuide(): ContextualBase?
}