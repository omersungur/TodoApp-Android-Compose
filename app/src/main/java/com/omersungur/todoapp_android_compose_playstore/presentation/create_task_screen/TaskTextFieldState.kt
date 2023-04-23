package com.omersungur.todoapp_android_compose_playstore.presentation.create_task_screen

data class TaskTextFieldState(
    val text: String = "",
    val hint: String = "",
    val isHintVisible: Boolean = true
)