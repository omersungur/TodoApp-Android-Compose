package com.omersungur.todoapp_android_compose_playstore.domain.use_case

data class TaskUseCases(
    val getTaskList: GetTaskList,
    val deleteTask: DeleteTask,
    val addTask: AddTask,
    val getTask: GetTask
)