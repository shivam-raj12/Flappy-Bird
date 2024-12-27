package com.shivam.flappybird

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp


@Composable
fun Pipe(
    modifier: Modifier = Modifier,
    topSpaceHeight: Float = 300f,
    gapHeight: Float = 200f
) {
    Canvas(modifier) {
        drawRect(
            color = Color.Black,
            topLeft = Offset(0f, 0f),
            size = Size(size.width, topSpaceHeight),
            style = Stroke(2.dp.toPx())
        )
        drawRect(
            brush = Brush.horizontalGradient(
                listOf(
                    Color(0xFF32CD32),
                    Color(0xFF228B22),
                    Color(0xFF7CFC00)
                ), endX = size.width - 5f
            ),
            topLeft = Offset(0f, 0f),
            size = Size(size.width, topSpaceHeight),
        )

        drawRect(
            color = Color.Black,
            topLeft = Offset(0f, topSpaceHeight + gapHeight),
            size = Size(size.width, size.height),
            style = Stroke(2.dp.toPx())
        )
        drawRect(
            brush = Brush.horizontalGradient(
                listOf(
                    Color(0xFF32CD32),
                    Color(0xFF228B22),
                    Color(0xFF7CFC00)
                ), endX = size.width - 5f
            ),
            topLeft = Offset(0f, topSpaceHeight + gapHeight),
            size = Size(size.width, size.height),
        )
    }
}


data class Pipe(
    var xPosition: Animatable<Float, *>,
    val topSpaceHeight: Float,
    val gapHeight: Float
)