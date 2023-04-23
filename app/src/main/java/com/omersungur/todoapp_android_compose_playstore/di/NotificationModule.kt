package com.omersungur.todoapp_android_compose_playstore.di

import android.app.NotificationManager
import android.content.Context
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.core.app.NotificationCompat
import com.omersungur.todoapp_android_compose_playstore.R
import com.omersungur.todoapp_android_compose_playstore.presentation.platform.service.ServiceHelper
import com.omersungur.todoapp_android_compose_playstore.util.Constants.NOTIFICATION_CHANNEL_ID
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped

@ExperimentalAnimationApi
@Module
@InstallIn(ServiceComponent::class)
object NotificationModule {

    @ServiceScoped
    @Provides
    fun provideNotificationBuilder(
        @ApplicationContext context: Context
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setContentTitle((context.getString(R.string.titleTimerNotification)))
            .setContentText("00:00:00")
            .setSmallIcon(R.drawable.notif_icon)
            .setOngoing(true)
            .addAction(0, (context.getString(R.string.timerStopNotification)), ServiceHelper.stopPendingIntent(context))
            .addAction(0, (context.getString(R.string.timerCancelNotification)), ServiceHelper.cancelPendingIntent(context))
            .setContentIntent(ServiceHelper.clickPendingIntent(context))
    }

    @ServiceScoped
    @Provides
    fun provideNotificationManager(
        @ApplicationContext context: Context
    ): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

}