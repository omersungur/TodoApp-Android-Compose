package com.omersungur.todoapp_android_compose_playstore.presentation.util

import androidx.compose.ui.graphics.Brush
import com.omersungur.todoapp_android_compose_playstore.presentation.theme.Colors

class GradientBackground {

    val gradientBrush = Brush.verticalGradient(
        colors = listOf(Colors.BlueBG, Colors.WhiteBG)
    )

}