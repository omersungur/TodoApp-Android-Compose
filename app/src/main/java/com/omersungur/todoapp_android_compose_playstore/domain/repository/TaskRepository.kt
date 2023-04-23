package com.omersungur.todoapp_android_compose_playstore.domain.repository

import com.omersungur.todoapp_android_compose_playstore.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    suspend fun getTask(id: Int) : Task?

    suspend fun deleteTask(task: Task)

    suspend fun addTask(task: Task)

    fun getTaskList() : Flow<List<Task>>
}