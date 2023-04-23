package com.omersungur.todoapp_android_compose_playstore.domain.util

sealed class TaskOrder {

    object Title : TaskOrder()
    object Date : TaskOrder()
    object Color : TaskOrder()

}