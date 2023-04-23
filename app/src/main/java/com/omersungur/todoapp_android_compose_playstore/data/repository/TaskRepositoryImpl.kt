package com.omersungur.todoapp_android_compose_playstore.data.repository

import com.omersungur.todoapp_android_compose_playstore.data.data_source.TaskDao
import com.omersungur.todoapp_android_compose_playstore.domain.model.Task
import com.omersungur.todoapp_android_compose_playstore.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

class TaskRepositoryImpl(
    private val dao: TaskDao
) : TaskRepository {

    override suspend fun getTask(id: Int): Task? {
        return dao.getTask(id)
    }

    override suspend fun deleteTask(task: Task) {
        dao.deleteTask(task)
    }

    override suspend fun addTask(task: Task) {
        dao.addTask(task)
    }

    override fun getTaskList(): Flow<List<Task>> {
        return dao.getTaskList()
    }
}