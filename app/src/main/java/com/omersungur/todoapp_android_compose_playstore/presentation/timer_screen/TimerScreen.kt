package com.omersungur.todoapp_android_compose_playstore.presentation.timer_screen

import androidx.compose.runtime.Composable
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Cyan
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.omersungur.todoapp_android_compose_playstore.R
import com.omersungur.todoapp_android_compose_playstore.presentation.platform.service.ServiceHelper
import com.omersungur.todoapp_android_compose_playstore.presentation.platform.service.StopTimerService
import com.omersungur.todoapp_android_compose_playstore.presentation.platform.service.StopTimerState
import com.omersungur.todoapp_android_compose_playstore.presentation.theme.Colors
import com.omersungur.todoapp_android_compose_playstore.util.Constants.ACTION_SERVICE_CANCEL
import com.omersungur.todoapp_android_compose_playstore.util.Constants.ACTION_SERVICE_START
import com.omersungur.todoapp_android_compose_playstore.util.Constants.ACTION_SERVICE_STOP

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TimerScreen(
    stopTimerService: StopTimerService,
) {
    val context = LocalContext.current
    val hours by stopTimerService.hours
    val minutes by stopTimerService.minutes
    val seconds by stopTimerService.seconds
    val currentState by stopTimerService.currentState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Colors.BlueBG)
            .padding(30.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .weight(weight = 8f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            AnimatedContent(
                targetState = hours,
                transitionSpec = {
                    addAnimation()
                }) {
                Text(
                    text = hours, style = TextStyle(
                        fontSize = 60.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (hours == "00") White else Red
                    )
                )
            }
            Text(
                text = ":",
                fontSize = 60.sp,
                fontWeight = FontWeight.Bold,
                color = White
            )
            AnimatedContent(
                targetState = minutes,
                transitionSpec = {
                    addAnimation()
                }) {
                Text(
                    text = minutes, style = TextStyle(
                        fontSize = 60.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (minutes == "00") White else Black
                    )
                )
            }
            Text(
                text = ":",
                fontSize = 60.sp,
                fontWeight = FontWeight.Bold,
                color = White
            )
            AnimatedContent(
                targetState = seconds,
                transitionSpec = {
                    addAnimation()
                }) {
                Text(
                    text = seconds, style = TextStyle(
                        fontSize = 60.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (seconds == "00") White else Black
                    )
                )
            }
        }
        Row(modifier = Modifier.weight(weight = 1f)) {
            Button(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(0.8f), onClick = {
                    ServiceHelper.triggerForegroundService(
                        context = context,
                        action = if (currentState == StopTimerState.Started) ACTION_SERVICE_STOP
                        else ACTION_SERVICE_START
                    )
                }, colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (currentState == StopTimerState.Started) Red else Green,
                    contentColor = Black,
                )
            ) {
                Text(
                    text = if (currentState == StopTimerState.Started) stringResource(id = R.string.timerStop)
                    else if ((currentState == StopTimerState.Stopped)) stringResource(id = R.string.timerResume)
                    else stringResource(id = R.string.timerStart),
                    style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)

                )
            }
            Spacer(modifier = Modifier.width(30.dp))
            Button(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(0.8f),
                onClick = {
                    ServiceHelper.triggerForegroundService(
                        context = context, action = ACTION_SERVICE_CANCEL
                    )
                },
                enabled = seconds != "00" && currentState != StopTimerState.Started,
                colors = ButtonDefaults.buttonColors(
                    disabledBackgroundColor = LightGray,
                    contentColor = White,
                    backgroundColor = Red
                )
            ) {
                Text(
                    text = stringResource(id = R.string.timerReset),
                    style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@ExperimentalAnimationApi
fun addAnimation(duration: Int = 250): ContentTransform {
    return slideInHorizontally(animationSpec = tween(durationMillis = duration)) { height -> height } + fadeIn(
        animationSpec = tween(durationMillis = duration)
    ) with slideOutHorizontally(animationSpec = tween(durationMillis = duration)) { height -> height } + fadeOut(
        animationSpec = tween(durationMillis = duration)
    )
}