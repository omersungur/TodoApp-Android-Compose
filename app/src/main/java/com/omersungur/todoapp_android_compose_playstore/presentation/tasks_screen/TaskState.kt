package com.omersungur.todoapp_android_compose_playstore.presentation.tasks_screen

import com.omersungur.todoapp_android_compose_playstore.domain.model.Task
import com.omersungur.todoapp_android_compose_playstore.domain.util.TaskOrder

data class TaskState(
    val tasks: List<Task> = emptyList(),
    val taskOrder: TaskOrder = TaskOrder.Date,
    val isOrderSectionVisible: Boolean = false,
    val isCheckBoxSelected: Boolean = false
)
