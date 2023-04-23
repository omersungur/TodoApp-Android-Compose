package com.omersungur.todoapp_android_compose_playstore.presentation.platform.service

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.compose.animation.ExperimentalAnimationApi
import com.omersungur.todoapp_android_compose_playstore.presentation.MainActivity
import com.omersungur.todoapp_android_compose_playstore.util.Constants.CANCEL_REQUEST_CODE
import com.omersungur.todoapp_android_compose_playstore.util.Constants.CLICK_REQUEST_CODE
import com.omersungur.todoapp_android_compose_playstore.util.Constants.RESUME_REQUEST_CODE
import com.omersungur.todoapp_android_compose_playstore.util.Constants.TIMER_STATE
import com.omersungur.todoapp_android_compose_playstore.util.Constants.STOP_REQUEST_CODE

@ExperimentalAnimationApi
object ServiceHelper {

    private const val flag =
        PendingIntent.FLAG_IMMUTABLE

    fun clickPendingIntent(context: Context): PendingIntent {
        val clickIntent = Intent(context, MainActivity::class.java).apply {
            putExtra(TIMER_STATE, StopTimerState.Started.name)
        }
        return PendingIntent.getActivity(
            context, CLICK_REQUEST_CODE, clickIntent, flag
        )
    }

    fun stopPendingIntent(context: Context): PendingIntent {
        val stopIntent = Intent(context, StopTimerService::class.java).apply {
            putExtra(TIMER_STATE, StopTimerState.Stopped.name)
        }
        return PendingIntent.getService(
            context, STOP_REQUEST_CODE, stopIntent, flag
        )
    }

    fun resumePendingIntent(context: Context): PendingIntent {
        val resumeIntent = Intent(context, StopTimerService::class.java).apply {
            putExtra(TIMER_STATE, StopTimerState.Started.name)
        }
        return PendingIntent.getService(
            context, RESUME_REQUEST_CODE, resumeIntent, flag
        )
    }

    fun cancelPendingIntent(context: Context): PendingIntent {
        val cancelIntent = Intent(context, StopTimerService::class.java).apply {
            putExtra(TIMER_STATE, StopTimerState.Canceled.name)
        }
        return PendingIntent.getService(
            context, CANCEL_REQUEST_CODE, cancelIntent, flag
        )
    }

    fun triggerForegroundService(context: Context, action: String) {
        Intent(context, StopTimerService::class.java).apply {
            this.action = action
            context.startService(this)
        }
    }
}