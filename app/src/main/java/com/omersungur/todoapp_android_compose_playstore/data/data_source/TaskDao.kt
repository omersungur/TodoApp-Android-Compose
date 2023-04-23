package com.omersungur.todoapp_android_compose_playstore.data.data_source

import androidx.room.*
import com.omersungur.todoapp_android_compose_playstore.domain.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTask (task: Task)

    @Query("SELECT * FROM Task")
    fun getTaskList() : Flow<List<Task>>

    @Query("SELECT * FROM Task WHERE id = :id")
    suspend fun getTask(id : Int) : Task?

    @Delete
    suspend fun deleteTask(task: Task)
}