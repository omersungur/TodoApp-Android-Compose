package com.omersungur.todoapp_android_compose_playstore.presentation.create_task_screen

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.flowlayout.MainAxisAlignment
import com.omersungur.todoapp_android_compose_playstore.R
import com.omersungur.todoapp_android_compose_playstore.domain.model.Task
import com.omersungur.todoapp_android_compose_playstore.presentation.platform.broadcast_receiver.AlarmReceiver
import com.omersungur.todoapp_android_compose_playstore.presentation.create_task_screen.components.TransparentHintTextField
import com.omersungur.todoapp_android_compose_playstore.presentation.tasks_screen.components.DateMaterialDialog
import com.omersungur.todoapp_android_compose_playstore.presentation.tasks_screen.components.TimeMaterialDialog
import com.omersungur.todoapp_android_compose_playstore.presentation.util.Screen
import kotlinx.coroutines.flow.collectLatest
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun CreateTaskScreen(
    navController: NavController,
    taskColor: Int,
    createTaskViewModel: CreateTaskViewModel = hiltViewModel(),
) {
    val titleState = createTaskViewModel.taskTitle.value
    val descriptionState = createTaskViewModel.taskDescription.value
    val state = createTaskViewModel.state.value

    val context = LocalContext.current
    var isAlarmActive = false

    var pickedDateForTaskTime by remember {
        mutableStateOf(LocalDate.now())
    }
    var pickedTimeForTaskTime by remember {
        mutableStateOf(LocalTime.NOON)
    }
    val formattedDateForTaskTime by remember {
        derivedStateOf {
            DateTimeFormatter
                .ofPattern("MMM dd yyyy")
                .format(pickedDateForTaskTime)
        }
    }
    val formattedTimeForTaskTime by remember {
        derivedStateOf {
            DateTimeFormatter
                .ofPattern("hh:mm a")
                .format(pickedTimeForTaskTime)
        }
    }

    var pickedDateForAlarm by remember {
        mutableStateOf(LocalDate.now())
    }
    var pickedTimeForAlarm by remember {
        mutableStateOf(LocalTime.NOON)
    }
    val formattedDateForAlarm by remember {
        derivedStateOf {
            DateTimeFormatter
                .ofPattern("MMM dd yyyy")
                .format(pickedDateForAlarm)
        }
    }
    val formattedTimeForAlarm by remember {
        derivedStateOf {
            DateTimeFormatter
                .ofPattern("hh:mm a")
                .format(pickedTimeForAlarm)
        }
    }

    val dateDialogStateForTimeToFinish = rememberMaterialDialogState()
    val timeDialogStateForTimeToFinish = rememberMaterialDialogState()

    val dateDialogStateForTimeToAlarm = rememberMaterialDialogState()
    val timeDialogStateForTimeToAlarm = rememberMaterialDialogState()

    val scaffoldState = rememberScaffoldState()

    val taskBackgroundAnimatable = remember {
        Animatable(
            Color(if (taskColor != -1) taskColor else createTaskViewModel.taskColor.value)
        )
    }

    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        createTaskViewModel.eventFlow.collectLatest { event ->
            when (event) {
                is CreateTaskViewModel.UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }

                is CreateTaskViewModel.UiEvent.SaveNote -> {
                    navController.navigate(Screen.TaskScreen.route)
                }
            }
        }
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    createTaskViewModel.onEvent(CreateTaskEvent.SaveNote)

                    if (isAlarmActive) {
                        val alarmManager =
                            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

                        val intent = Intent(context, AlarmReceiver::class.java)
                        val pendingIntent = PendingIntent.getBroadcast(
                            context,
                            0,
                            intent,
                            PendingIntent.FLAG_IMMUTABLE
                        )

                        val calendar = Calendar.getInstance()
                        calendar.set(
                            pickedDateForAlarm.year,
                            pickedDateForAlarm.monthValue - 1,
                            pickedDateForAlarm.dayOfMonth,
                            pickedTimeForAlarm.hour,
                            pickedTimeForAlarm.minute
                        )

                        val alarmTime = calendar.timeInMillis

                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent)
                    }
                },
                backgroundColor = Color.Cyan
            ) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = "Save task",
                    tint = Color.Black
                )
            }
        },
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(taskBackgroundAnimatable.value)
                .padding(10.dp)
        ) {
            com.google.accompanist.flowlayout.FlowRow(
                mainAxisSpacing = 10.dp,
                crossAxisSpacing = 10.dp,
                mainAxisAlignment = MainAxisAlignment.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Task.taskColors.forEach { color ->
                    val colorInt = color.toArgb()
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .shadow(15.dp, RectangleShape)
                            .clip(RectangleShape)
                            .background(color)
                            .border(
                                width = 2.dp,
                                color = if (createTaskViewModel.taskColor.value == colorInt) {
                                    Color.Black
                                } else Color.Transparent,
                                shape = RectangleShape,

                                )
                            .clickable {
                                scope.launch {
                                    taskBackgroundAnimatable.animateTo(
                                        targetValue = Color(colorInt),
                                        animationSpec = tween(
                                            durationMillis = 750,
                                            easing = LinearEasing,
                                        )
                                    )
                                }
                                createTaskViewModel.onEvent(CreateTaskEvent.ChangeColor(colorInt))
                            }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            TransparentHintTextField(
                text = titleState.text,
                hint = titleState.hint,
                onValueChange = {
                    createTaskViewModel.onEvent(CreateTaskEvent.EnteredTitle(it))
                },
                onFocusChange = {
                    createTaskViewModel.onEvent(CreateTaskEvent.ChangeTitleFocus(it))
                },
                isHintVisible = titleState.isHintVisible,
                singleLine = true,
                textStyle = MaterialTheme.typography.h4,
                contentDescr = context.getString(R.string.taskTitleContentDescription)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                modifier = Modifier
                    .clickable(onClick = {

                        dateDialogStateForTimeToFinish.show()
                        timeDialogStateForTimeToFinish.show()

                    })
                    .semantics {
                        contentDescription = context.getString(R.string.timeToFinishTask)
                    },

                text = state.timeToFinishTaskText,
                color = Color.Black,
                style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold)
            )

            TimeMaterialDialog(
                dialogState = timeDialogStateForTimeToFinish,
                title = stringResource(id = R.string.pickTaskTime),
                onPositiveButtonClick = {
                    createTaskViewModel.onEvent(CreateTaskEvent.SetTimeToFinishTask("$formattedDateForTaskTime - $formattedTimeForTaskTime"))
                },
                onNegativeButtonClick = {},
            ) {
                pickedTimeForTaskTime = it
            }.also {
                DateMaterialDialog(
                    dialogState = dateDialogStateForTimeToFinish,
                    title = stringResource(id = R.string.pickTaskDate),
                    onPositiveButtonClick = {
                        createTaskViewModel.onEvent(CreateTaskEvent.SetTimeToFinishTask("$formattedDateForTaskTime - $formattedTimeForTaskTime"))
                    },
                    onNegativeButtonClick = {
                    },
                    onDateChange = {
                        pickedDateForTaskTime = it
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                modifier = Modifier
                    .clickable(onClick = {

                        timeDialogStateForTimeToAlarm
                            .show()
                            .also {
                                dateDialogStateForTimeToAlarm.show()
                            }

                    })
                    .semantics {
                        contentDescription = context.getString(R.string.alarmTime)
                    },

                text = state.alarmTime,
                color = Color.Black,
                style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
            )

            if (state.alarmTime != stringResource(id = R.string.alarmTime)) {
                isAlarmActive = true
            }

            TimeMaterialDialog(
                dialogState = timeDialogStateForTimeToAlarm,
                title = stringResource(id = R.string.pickAlarmTime),
                onPositiveButtonClick = {
                    createTaskViewModel.onEvent(CreateTaskEvent.SetAlarmTime("$formattedDateForAlarm - $formattedTimeForAlarm"))
                },
                onNegativeButtonClick = {
                    createTaskViewModel.onEvent(CreateTaskEvent.SetAlarmTime(context.getString(R.string.alarmTime)))
                },
                onTimeChange = {
                    pickedTimeForAlarm = it
                }
            )

            DateMaterialDialog(
                dialogState = dateDialogStateForTimeToAlarm,
                title = stringResource(id = R.string.pickAlarmDate),
                onPositiveButtonClick = {
                    createTaskViewModel.onEvent(CreateTaskEvent.SetAlarmTime("$formattedDateForAlarm - $formattedTimeForAlarm"))
                    Toast.makeText(
                        context,
                        context.getString(R.string.alarm_warning),
                        Toast.LENGTH_LONG
                    ).show()
                },
                onNegativeButtonClick = {
                    createTaskViewModel.onEvent(CreateTaskEvent.SetAlarmTime(context.getString(R.string.alarmTime)))
                },
                onDateChange = {
                    pickedDateForAlarm = it
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            TransparentHintTextField(
                text = descriptionState.text,
                hint = descriptionState.hint,
                onValueChange = {
                    createTaskViewModel.onEvent(CreateTaskEvent.EnteredDescription(it))
                },
                onFocusChange = {
                    createTaskViewModel.onEvent(CreateTaskEvent.ChangeDescriptionFocus(it))
                },
                isHintVisible = descriptionState.isHintVisible,
                textStyle = MaterialTheme.typography.h5,
                modifier = Modifier.fillMaxHeight(),
                contentDescr = context.getString(R.string.taskDescriptionContentDescription)
            )
        }
    }
}