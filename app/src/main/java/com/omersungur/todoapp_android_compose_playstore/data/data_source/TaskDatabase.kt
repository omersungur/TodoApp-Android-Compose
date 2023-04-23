package com.omersungur.todoapp_android_compose_playstore.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.omersungur.todoapp_android_compose_playstore.domain.model.Task

@Database(
    entities = [Task::class],
    version = 1
)
abstract class TaskDatabase : RoomDatabase() {

    abstract val taskDao : TaskDao

}