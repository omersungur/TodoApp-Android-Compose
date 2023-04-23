package com.omersungur.todoapp_android_compose_playstore.domain.use_case

import com.omersungur.todoapp_android_compose_playstore.domain.model.Task
import com.omersungur.todoapp_android_compose_playstore.domain.repository.TaskRepository
import com.omersungur.todoapp_android_compose_playstore.domain.util.TaskOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetTaskList(
    private val repository: TaskRepository,
) {

    operator fun invoke(
        taskOrder: TaskOrder = TaskOrder.Date,
    ): Flow<List<Task>> {
        return repository.getTaskList().map { tasks ->
            when (taskOrder) {
                is TaskOrder.Title -> tasks.sortedBy { it.title.lowercase() }
                is TaskOrder.Date -> tasks.sortedBy { it.timeToFinish }
                is TaskOrder.Color -> tasks.sortedByDescending { it.color }
            }
        }
    }
}

