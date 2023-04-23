package com.omersungur.todoapp_android_compose_playstore.presentation.tasks_screen

import com.omersungur.todoapp_android_compose_playstore.domain.model.Task
import com.omersungur.todoapp_android_compose_playstore.domain.util.TaskOrder

sealed class TaskEvent {

    data class Order(val taskOrder: TaskOrder) : TaskEvent()
    data class DeleteTask(val task: Task) : TaskEvent()
    object RestoreTask : TaskEvent()
    object ToggleOrderSection : TaskEvent()
    object ClickCheckBox : TaskEvent()
}