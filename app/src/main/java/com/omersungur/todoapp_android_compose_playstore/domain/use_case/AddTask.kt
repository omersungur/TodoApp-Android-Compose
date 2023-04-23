package com.omersungur.todoapp_android_compose_playstore.domain.use_case

import com.omersungur.todoapp_android_compose_playstore.R
import com.omersungur.todoapp_android_compose_playstore.di.AppModule
import com.omersungur.todoapp_android_compose_playstore.domain.model.InvalidTaskException
import com.omersungur.todoapp_android_compose_playstore.domain.model.Task
import com.omersungur.todoapp_android_compose_playstore.domain.repository.TaskRepository
import javax.inject.Inject


class AddTask @Inject constructor(
    resourcesProvider: AppModule.ResourcesProvider,
    private val repository: TaskRepository,

    ) {

    private val titleWarning = resourcesProvider.getString(R.string.emptyTitleWarning)
    private val dateWarning = resourcesProvider.getString(R.string.emptyDateWarning)
    private val descriptionWarning = resourcesProvider.getString(R.string.emptyDescriptionWarning)
    private val timeToFinishTask = resourcesProvider.getString(R.string.timeToFinishTask)

    @Throws(InvalidTaskException::class)
    suspend operator fun invoke(task: Task) {

        if (task.title.isBlank()) {
            throw InvalidTaskException(titleWarning)
        }
        if (task.timeToFinish == timeToFinishTask)
        //task.timeToFinish == "Time to Complete" || task.timeToFinish == "Tamamlanması Gereken Tarih"
        {
            throw InvalidTaskException(dateWarning)
        }
        if (task.description.isBlank()) {
            throw InvalidTaskException(descriptionWarning)
        }
        repository.addTask(task)
    }
}