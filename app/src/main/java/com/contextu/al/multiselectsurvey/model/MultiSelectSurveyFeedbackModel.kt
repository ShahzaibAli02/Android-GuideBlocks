package com.contextu.al.multiselectsurvey.model

data class MultiSelectSurveyFeedbackModel(
    val c: List<String>,
    val i: Int,
    val message: String,
    val title: String
)