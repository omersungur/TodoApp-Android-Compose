package com.omersungur.todoapp_android_compose_playstore.presentation.tasks_screen.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.omersungur.todoapp_android_compose_playstore.domain.util.TaskOrder
import com.omersungur.todoapp_android_compose_playstore.R

@Composable
fun OrderSection(
    modifier: Modifier = Modifier,
    taskOrder: TaskOrder = TaskOrder.Date,
    onOrderChange: (TaskOrder) -> Unit,
) {
    Column(
        modifier = modifier,
    ) {
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center

    ) {
        SpecialRadioButton(
            text = stringResource(id = R.string.radioButtonDate),
            selected = taskOrder is TaskOrder.Date,
            onSelect = {
                onOrderChange(TaskOrder.Date)
            },
            contentDesc = stringResource(id = R.string.radioButtonDate)
        )

        Spacer(modifier = Modifier.width(6.dp))

        SpecialRadioButton(
            text = stringResource(id = R.string.radioButtonTitle),
            selected = taskOrder is TaskOrder.Title,
            onSelect = {
                onOrderChange(TaskOrder.Title)
            },
            contentDesc = stringResource(id = R.string.radioButtonTitle)
        )
        Spacer(modifier = Modifier.width(6.dp))

        SpecialRadioButton(
            text = stringResource(id = R.string.radioButtonColor),
            selected = taskOrder is TaskOrder.Color,
            onSelect = {
                onOrderChange(TaskOrder.Color)
            },
            contentDesc = stringResource(id = R.string.radioButtonColor)
        )
    }
}