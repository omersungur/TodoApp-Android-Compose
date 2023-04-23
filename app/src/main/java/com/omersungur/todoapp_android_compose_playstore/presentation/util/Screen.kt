package com.omersungur.todoapp_android_compose_playstore.presentation.util

sealed class Screen(val route: String) {

    object TaskScreen: Screen("task_screen")
    object CreateTaskScreen: Screen("create_task_screen")
    object TimerScreen: Screen("timer_screen")

}