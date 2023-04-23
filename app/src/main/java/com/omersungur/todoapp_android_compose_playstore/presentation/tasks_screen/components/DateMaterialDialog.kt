package com.omersungur.todoapp_android_compose_playstore.presentation.tasks_screen.components

import androidx.compose.runtime.Composable
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import java.time.LocalDate

@Composable
fun DateMaterialDialog(
    dialogState: MaterialDialogState,
    title: String,
    onPositiveButtonClick: () -> Unit,
    onNegativeButtonClick: () -> Unit,
    onDateChange: (LocalDate) -> Unit
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
        datepicker(
            initialDate = LocalDate.now(),
            title = title,
            onDateChange = onDateChange
        )
    }
}