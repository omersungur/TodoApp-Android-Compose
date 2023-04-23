package com.omersungur.todoapp_android_compose_playstore.presentation.create_task_screen

import androidx.compose.ui.focus.FocusState

sealed class CreateTaskEvent {

    data class EnteredTitle(val value: String) : CreateTaskEvent()
    data class ChangeTitleFocus(val focusState: FocusState) : CreateTaskEvent()
    data class EnteredDescription(val value: String) : CreateTaskEvent()
    data class ChangeDescriptionFocus(val focusState: FocusState) : CreateTaskEvent()
    data class ChangeColor(val color: Int) : CreateTaskEvent()
    data class SetTimeToFinishTask(val value: String): CreateTaskEvent()
    data class SetAlarmTime(val value: String): CreateTaskEvent()
    object SaveNote : CreateTaskEvent()

}