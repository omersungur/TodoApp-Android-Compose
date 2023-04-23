package com.omersungur.todoapp_android_compose_playstore.presentation.tasks_screen

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sort
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.omersungur.todoapp_android_compose_playstore.R
import com.omersungur.todoapp_android_compose_playstore.presentation.tasks_screen.components.OrderSection
import com.omersungur.todoapp_android_compose_playstore.presentation.tasks_screen.components.TaskCardView
import com.omersungur.todoapp_android_compose_playstore.presentation.util.GradientBackground
import com.omersungur.todoapp_android_compose_playstore.presentation.util.Screen
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@ExperimentalAnimationApi
@Composable
fun TaskScreen(
    navController: NavController,
    tasksViewModel: TasksViewModel = hiltViewModel(),
) {

    val state = tasksViewModel.state.value
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    val gradientBackground = GradientBackground()

    Surface {

        Scaffold(
            modifier = Modifier
                .fillMaxSize(),

            scaffoldState = scaffoldState

        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(brush = gradientBackground.gradientBrush)
                    .padding(5.dp)

            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween

                ) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .weight(1f)
                            .wrapContentWidth(Alignment.CenterHorizontally),
                        text = stringResource(id = R.string.title_main_screen),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.h4,
                        color = Color.White,
                        fontFamily = FontFamily.Serif)
                    IconButton(

                        onClick = {
                            tasksViewModel.onEvent(TaskEvent.ToggleOrderSection)
                        },
                    ) {
                        Icon(
                            modifier = Modifier.align(Alignment.CenterVertically),
                            imageVector = Icons.Default.Sort,
                            tint = Color.Black,
                            contentDescription = "Sort"
                        )
                    }
                }
                AnimatedVisibility(
                    visible = state.isOrderSectionVisible,
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut() + slideOutVertically()
                ) {
                    OrderSection(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        taskOrder = state.taskOrder,
                        onOrderChange = {
                            tasksViewModel.onEvent(TaskEvent.Order(it))
                        }
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(state.tasks) { task ->
                        TaskCardView(
                            task = task,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    navController.navigate(
                                        Screen.CreateTaskScreen.route
                                                + "?taskId=${task.id}&taskColor=${task.color}"
                                        //"/${task.id}/${task.color}"
                                    )
                                },
                            onDeleteClick = {
                                tasksViewModel.onEvent(TaskEvent.DeleteTask(task))
                                scope.launch {
                                    val result = scaffoldState.snackbarHostState.showSnackbar(
                                        message = "Task Deleted!",
                                        actionLabel = "Undo"
                                    )
                                    if (result == SnackbarResult.ActionPerformed) {
                                        tasksViewModel.onEvent(TaskEvent.RestoreTask)
                                    }
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }
    }
}