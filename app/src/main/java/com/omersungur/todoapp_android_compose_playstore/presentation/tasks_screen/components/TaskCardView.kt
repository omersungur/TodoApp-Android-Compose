package com.omersungur.todoapp_android_compose_playstore.presentation.tasks_screen.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.omersungur.todoapp_android_compose_playstore.R
import com.omersungur.todoapp_android_compose_playstore.domain.model.Task

@Composable
fun TaskCardView(
    task: Task,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 16.dp,
    onDeleteClick: () -> Unit,
) {

    val fontForDescription = FontFamily(
        Font(
            R.font.varela,
            FontWeight.Bold
        )
    )
    val fontForDate = FontFamily(
        Font(
            R.font.chakra_petch_bold,
            FontWeight.Bold
        )
    )

    Box(
        modifier = modifier
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val clipPath = Path().apply {
                lineTo(size.width, 0f)
                lineTo(size.width, size.height)
                lineTo(size.width, size.height)
                lineTo(0f, size.height)
                close()
            }

            clipPath(clipPath) {
                drawRoundRect(
                    color = Color(task.color),
                    //color = Color(task.color).copy(alpha = 0.9f) -> Opacity
                    size = size,
                    cornerRadius = CornerRadius(cornerRadius.toPx())
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp)

        ) {

            Box(modifier = modifier.fillMaxWidth()) {

                TextShadow(
                    text = task.title,
                    modifier = modifier
                        .fillMaxWidth()
                        .align(Alignment.Center),
                    )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(modifier = Modifier.padding(10.dp),
                text = task.description,
                color = Color.Black,
                maxLines = 10,
                overflow = TextOverflow.Ellipsis,
                fontFamily = fontForDescription,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        IconButton(
            onClick = onDeleteClick,
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete Task",
                tint = Color.Black
            )
        }
        Text(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(5.dp),
            text = task.timeToFinish,
            color = Color.Red,
            fontFamily = fontForDate,
            style = TextStyle(
                fontSize = 18.sp
            )
        )


        if (task.alarmTime == stringResource(id = R.string.alarmTime)) {
            Icon(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp),
                painter = painterResource(id = R.drawable.notification_off_icon),
                contentDescription = "Alarm is not active",
                tint = Color.Red
            )
        } else {
            Icon(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp),
                painter = painterResource(id = R.drawable.notification_active_icon),
                contentDescription = "Alarm is active",
                tint = Color.Blue
            )
        }
    }
}
