package com.omersungur.todoapp_android_compose_playstore.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.omersungur.todoapp_android_compose_playstore.presentation.theme.Colors

@Entity
data class Task(

    val title: String,
    val description: String,
    val timeToFinish: String,
    val alarmTime: String,
    val timestamp: Long,
    val color: Int,
    @PrimaryKey val id: Int? = null
) {

    companion object {

        val taskColors = listOf(
            Colors.Green200,
            Colors.Purple200,
            Colors.White200,
            Colors.Yellow200,
            Colors.Cyan200,
        )
    }
}
class InvalidTaskException(message: String) : Exception(message)