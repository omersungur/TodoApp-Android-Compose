package com.omersungur.todoapp_android_compose_playstore.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.omersungur.todoapp_android_compose_playstore.R
import com.omersungur.todoapp_android_compose_playstore.presentation.platform.service.StopTimerService
import com.omersungur.todoapp_android_compose_playstore.presentation.create_task_screen.CreateTaskScreen
import com.omersungur.todoapp_android_compose_playstore.presentation.tasks_screen.TaskScreen
import com.omersungur.todoapp_android_compose_playstore.presentation.timer_screen.TimerScreen
import com.omersungur.todoapp_android_compose_playstore.presentation.util.Screen
import com.omersungur.todoapp_android_compose_playstore.presentation.navigation.bottom_nav_bar.BottomNavItem
import com.omersungur.todoapp_android_compose_playstore.presentation.navigation.bottom_nav_bar.BottomNavigationBar
import com.omersungur.todoapp_android_compose_playstore.presentation.theme.TodoAppAndroidComposeTheme
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var isBound by mutableStateOf(false)

    @OptIn(ExperimentalAnimationApi::class)
    private lateinit var stopTimerService: StopTimerService

    @OptIn(ExperimentalAnimationApi::class)
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as StopTimerService.StopTimerBinder
            stopTimerService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            isBound = false
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @OptIn(ExperimentalAnimationApi::class)
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TodoAppAndroidComposeTheme {
                Surface {
                    val navController = rememberNavController()
                    Scaffold(
                        bottomBar = {
                            BottomNavigationBar(
                                items = listOf(
                                    BottomNavItem(
                                        name = stringResource(id = R.string.taskNavName),
                                        route = Screen.TaskScreen.route,
                                        icon = R.drawable.task_screen_icon
                                    ),
                                    BottomNavItem(
                                        name = stringResource(id = R.string.createTaskNavName),
                                        route = Screen.CreateTaskScreen.route +
                                                "?taskId={taskId}&taskColor={taskColor}",
                                        icon = R.drawable.create_task_bottom_icon
                                    ),
                                    BottomNavItem(
                                        name = stringResource(id = R.string.timerNavName),
                                        route = Screen.TimerScreen.route,
                                        icon = R.drawable.timer_screen_icon,
                                    ),
                                ),
                                navController = navController,
                                onItemClick = {
                                    navController.navigate(it.route)
                                }
                            )
                        }
                    ) { innerPadding ->
                        NavHost(
                            modifier = Modifier.padding(innerPadding),
                            navController = navController,
                            startDestination = Screen.TaskScreen.route,

                            ) {

                            composable(route = Screen.TaskScreen.route) {
                                TaskScreen(navController = navController)
                            }

                            composable(
                                route = Screen.CreateTaskScreen.route +
                                        "?taskId={taskId}&taskColor={taskColor}",
                                //"create_task_screen/{taskId}/{taskColor}
                                arguments = listOf(
                                    navArgument(
                                        name = "taskId"
                                    ) {
                                        type = NavType.IntType
                                        defaultValue = -1

                                    },
                                    navArgument(
                                        name = "taskColor"
                                    ) {
                                        type = NavType.IntType
                                        defaultValue = -1
                                    },
                                )
                            ) {
                                val color = it.arguments?.getInt("taskColor") ?: -1
                                CreateTaskScreen(
                                    navController = navController,
                                    taskColor = color,

                                )
                            }

                            composable(route = Screen.TimerScreen.route) {
                                if (isBound) {
                                    TimerScreen(stopTimerService = stopTimerService)
                                }
                            }
                        }
                    }
                }
            }
        }
        requestPermissions(Manifest.permission.POST_NOTIFICATIONS)
    }

    @OptIn(ExperimentalAnimationApi::class)
    override fun onStart() {
        super.onStart()
        Intent(this, StopTimerService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
        isBound = false
    }

    private fun requestPermissions(vararg permissions: String) {
        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { result ->
            result.entries.forEach {
                Log.d("MainActivity", "${it.key} = ${it.value}")
            }
        }
        requestPermissionLauncher.launch(permissions.asList().toTypedArray())
    }
}
