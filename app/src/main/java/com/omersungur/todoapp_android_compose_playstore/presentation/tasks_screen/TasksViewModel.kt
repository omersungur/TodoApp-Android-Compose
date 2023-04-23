package com.omersungur.todoapp_android_compose_playstore.presentation.tasks_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omersungur.todoapp_android_compose_playstore.domain.model.Task
import com.omersungur.todoapp_android_compose_playstore.domain.use_case.TaskUseCases
import com.omersungur.todoapp_android_compose_playstore.domain.util.TaskOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
   private val taskUseCases: TaskUseCases
) : ViewModel() {

   private val _state = mutableStateOf(TaskState())
   val state: State<TaskState> = _state

   private var recentlyDeletedTask: Task? = null

   private var getTasksJob: Job? = null

   init {
      getTasks(TaskOrder.Date)
   }

   fun onEvent(event: TaskEvent) {
      when (event) {
         is TaskEvent.Order -> {
            if (state.value.taskOrder::class == event.taskOrder::class) {
               return
            }
            getTasks(event.taskOrder)
         }
         is TaskEvent.DeleteTask -> {
            viewModelScope.launch {
               taskUseCases.deleteTask(event.task)
               recentlyDeletedTask = event.task
            }
         }
         is TaskEvent.RestoreTask -> {
            viewModelScope.launch {
               taskUseCases.addTask(recentlyDeletedTask ?: return@launch)
               recentlyDeletedTask = null
            }
         }
         is TaskEvent.ToggleOrderSection -> {
            _state.value = state.value.copy(
               isOrderSectionVisible = !state.value.isOrderSectionVisible
            )
         }
         is TaskEvent.ClickCheckBox -> {
            _state.value = state.value.copy(
               isCheckBoxSelected = !state.value.isCheckBoxSelected
            )
         }
      }
   }

   private fun getTasks(noteOrder: TaskOrder) {
      getTasksJob?.cancel()
      getTasksJob = taskUseCases.getTaskList(noteOrder)
         .onEach { tasks ->
            _state.value = state.value.copy(
               tasks = tasks,
               taskOrder = noteOrder
            )
         }
         .launchIn(viewModelScope)
   }
}
