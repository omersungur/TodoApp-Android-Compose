package com.omersungur.todoapp_android_compose_playstore.presentation.tasks_screen.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TextShadow(
    text: String,
    modifier: Modifier,
) {
    Box(modifier = modifier) {
        Text(
            text = text,
            color = Color.DarkGray,
            modifier = Modifier
                .offset(
                    x = 1.dp,
                    y = 1.dp
                )
                .alpha(0.5f)
                .align(Alignment.Center)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 26.sp,
            maxLines = 2,
        )
        Text(
            text = text,
            color = Color.Blue,
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 26.sp,
            maxLines = 2,
        )
    }
}