package com.omersungur.todoapp_android_compose_playstore.domain.use_case

import com.omersungur.todoapp_android_compose_playstore.domain.model.Task
import com.omersungur.todoapp_android_compose_playstore.domain.repository.TaskRepository

class DeleteTask(
    private val repository: TaskRepository
) {

    suspend operator fun invoke(task: Task) {
        repository.deleteTask(task)
    }
}