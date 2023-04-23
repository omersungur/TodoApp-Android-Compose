package com.omersungur.todoapp_android_compose_playstore.di

import android.app.Application
import android.content.Context
import androidx.annotation.StringRes
import androidx.room.Room
import com.omersungur.todoapp_android_compose_playstore.data.data_source.TaskDatabase
import com.omersungur.todoapp_android_compose_playstore.data.repository.TaskRepositoryImpl
import com.omersungur.todoapp_android_compose_playstore.domain.repository.TaskRepository
import com.omersungur.todoapp_android_compose_playstore.domain.use_case.*
import com.omersungur.todoapp_android_compose_playstore.util.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTaskDatabase(application: Application): TaskDatabase {
        return Room.databaseBuilder(
            application,
            TaskDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideTaskRepository(db: TaskDatabase): TaskRepository {
        return TaskRepositoryImpl(db.taskDao)
    }

    @Provides
    fun provideContext(
        @ApplicationContext context: Context,
    ): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideUseCases(
        repository: TaskRepository,
        resourcesProvider: ResourcesProvider,
    ): TaskUseCases {
        return TaskUseCases(
            getTaskList = GetTaskList(repository),
            deleteTask = DeleteTask(repository),
            addTask = AddTask(resourcesProvider = resourcesProvider, repository),
            getTask = GetTask(repository)
        )
    }

    @Singleton
    class ResourcesProvider @Inject constructor(
        @ApplicationContext private val context: Context,
    ) {
        fun getString(@StringRes stringResId: Int): String {
            return context.getString(stringResId)
        }
    }
}