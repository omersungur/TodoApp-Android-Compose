package com.omersungur.todoapp_android_compose_playstore.presentation.tasks_screen.components

import androidx.compose.runtime.Composable
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.time.timepicker

import java.time.LocalTime

@Composable
fun TimeMaterialDialog(
    dialogState: MaterialDialogState,
    title: String,
    onPositiveButtonClick: () -> Unit,
    onNegativeButtonClick: () -> Unit,
    onTimeChange: (LocalTime) -> Unit
) {

    MaterialDialog(
        dialogState = dialogState,
        buttons = {
            positiveButton(text = "Ok") {
                onPositiveButtonClick.invoke()
            }
            negativeButton(text = "Cancel") {
                onNegativeButtonClick.invoke()
            }
        }) {
        timepicker(
            initialTime = LocalTime.NOON,
            title = title,
            is24HourClock = true,
            onTimeChange = onTimeChange
        )
    }
}