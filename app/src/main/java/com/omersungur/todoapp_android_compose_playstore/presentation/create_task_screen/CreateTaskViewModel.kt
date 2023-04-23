package com.omersungur.todoapp_android_compose_playstore.presentation.create_task_screen

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omersungur.todoapp_android_compose_playstore.R
import com.omersungur.todoapp_android_compose_playstore.di.AppModule
import com.omersungur.todoapp_android_compose_playstore.domain.model.InvalidTaskException
import com.omersungur.todoapp_android_compose_playstore.domain.model.Task
import com.omersungur.todoapp_android_compose_playstore.domain.use_case.TaskUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CreateTaskViewModel @Inject constructor(
    private val taskUseCases: TaskUseCases,
    savedStateHandle: SavedStateHandle,
    resourcesProvider: AppModule.ResourcesProvider,
    context: Context
) : ViewModel() {

    private val titleHint: String = resourcesProvider.getString(R.string.title_hint)
    private val titleDescription: String = resourcesProvider.getString(R.string.title_description)

    private val _taskTitle = mutableStateOf(
        TaskTextFieldState(
            hint = titleHint
        )
    )
    val taskTitle: State<TaskTextFieldState> = _taskTitle

    private val _taskDescription = mutableStateOf(
        TaskTextFieldState(
            hint = titleDescription
        )
    )
    val taskDescription: State<TaskTextFieldState> = _taskDescription

    private val _taskColor = mutableStateOf(Task.taskColors.random().toArgb())
    val taskColor: State<Int> = _taskColor

    private val _state = mutableStateOf(CreateTaskState(context = context))
    val state: State<CreateTaskState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentTaskId: Int? = null

    init {
        savedStateHandle.get<Int>("taskId")?.let { taskId ->
            if (taskId != -1) {
                viewModelScope.launch {
                    taskUseCases.getTask(taskId)?.also { task ->
                        currentTaskId = task.id
                        _taskTitle.value = taskTitle.value.copy(
                            text = task.title,
                            isHintVisible = false
                        )
                        _taskDescription.value = _taskDescription.value.copy(
                            text = task.description,
                            isHintVisible = false
                        )
                        _taskColor.value = task.color
                        _state.value.timeToFinishTaskText = task.timeToFinish
                        _state.value.alarmTime = task.alarmTime
                    }
                }
            }
        }
    }

    fun onEvent(event: CreateTaskEvent) {
        when (event) {
            is CreateTaskEvent.EnteredTitle -> {
                _taskTitle.value = taskTitle.value.copy(
                    text = event.value
                )
            }

            is CreateTaskEvent.ChangeTitleFocus -> {
                _taskTitle.value = taskTitle.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            taskTitle.value.text.isBlank()
                )
            }

            is CreateTaskEvent.EnteredDescription -> {
                _taskDescription.value = _taskDescription.value.copy(
                    text = event.value
                )
            }

            is CreateTaskEvent.ChangeDescriptionFocus -> {
                _taskDescription.value = _taskDescription.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            _taskDescription.value.text.isBlank()
                )
            }

            is CreateTaskEvent.ChangeColor -> {
                _taskColor.value = event.color
            }

            is CreateTaskEvent.SetTimeToFinishTask -> {
                _state.value = state.value.copy(
                    timeToFinishTaskText = event.value
                )
            }

            is CreateTaskEvent.SetAlarmTime -> {
                _state.value = state.value.copy(
                    alarmTime = event.value
                )
            }

            is CreateTaskEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                        taskUseCases.addTask(
                            Task(
                                title = taskTitle.value.text,
                                description = taskDescription.value.text,
                                timestamp = System.currentTimeMillis(),
                                color = taskColor.value,
                                id = currentTaskId,
                                timeToFinish = state.value.timeToFinishTaskText,
                                alarmTime = state.value.alarmTime
                            )
                        )
                        _eventFlow.emit(UiEvent.SaveNote)
                    } catch (e: InvalidTaskException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "Couldn't save task"
                            )
                        )
                    }
                }
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        object SaveNote : UiEvent()
    }
}
