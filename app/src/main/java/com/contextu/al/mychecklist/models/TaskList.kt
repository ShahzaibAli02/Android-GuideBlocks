package com.contextu.al.mychecklist.models

data class TaskList(
    val guideBlockKey: String,
    val tasks: List<Task>
)