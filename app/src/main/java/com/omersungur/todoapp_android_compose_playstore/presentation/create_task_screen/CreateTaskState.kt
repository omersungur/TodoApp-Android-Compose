package com.omersungur.todoapp_android_compose_playstore.presentation.create_task_screen

import android.content.Context
import com.omersungur.todoapp_android_compose_playstore.R


data class CreateTaskState(
    private val context: Context,
    var timeToFinishTaskText: String = context.getString(R.string.timeToFinishTask),
    var alarmTime: String = context.getString(R.string.alarmTime)
) {
}