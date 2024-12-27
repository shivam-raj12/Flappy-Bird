package com.shivam.flappybird

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import com.shivam.flappybird.ui.theme.FlappyBirdTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlappyBirdTheme {
                FlappyBirdGame()
            }
        }
    }
}

@Composable
fun FlappyBirdGame() {
    var gameStatus by remember { mutableStateOf(GameStatus.Idle) }
    var screenHeight by remember { mutableIntStateOf(0) }
    var screenWidth by remember { mutableIntStateOf(0) }
    val pipes = remember { mutableStateListOf<Pipe>(
        Pipe(
            xPosition = Animatable(200f),
            topSpaceHeight = ((screenHeight * 0.2f).toInt()..(screenHeight * 0.8f).toInt()).random()
                .toFloat(),
            gapHeight = 400f
        )
    ) }

    LaunchedEffect(gameStatus) {
        while (gameStatus == GameStatus.Running) {
            pipes.add(
                Pipe(
                    xPosition = Animatable(screenWidth.toFloat()),
                    topSpaceHeight = ((screenHeight * 0.2f).toInt()..(screenHeight * 0.8f).toInt()).random()
                        .toFloat(),
                    gapHeight = 400f
                )
            )
            delay(4000L)
        }
    }
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(gameStatus) {
        while (gameStatus == GameStatus.Running) {
            withFrameMillis {
                pipes.forEach { pipe ->
                    coroutineScope.launch(Dispatchers.Main) {
                        if (pipe.xPosition.value < -screenWidth) {
                            pipes.remove(pipe)
                            Log.d("TAG", "FlappyBirdGame: Pipe removed")
                        }
                        pipe.xPosition.animateTo(
                            targetValue = pipe.xPosition.value - 10f
                        )

                    }
                }
            }
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .onGloballyPositioned {
                screenHeight = it.size.height
                screenWidth = it.size.width
            }
            .clickable {
                gameStatus = GameStatus.Running
            },
        contentAlignment = Alignment.CenterEnd
    ) {
        Bird(
            gameStatus = gameStatus,
            onGameOver = {
                gameStatus = GameStatus.Over
            },
            onGameStart = {
                pipes.clear()
                gameStatus = GameStatus.Running
            }
        )
        pipes.forEach {
            Pipe(
                Modifier
                    .fillMaxHeight()
                    .width(50.dp)
                    .graphicsLayer {
                        translationX = it.xPosition.value
                    },
                topSpaceHeight = it.topSpaceHeight,
                gapHeight = it.gapHeight
            )
        }

        if (gameStatus == GameStatus.Idle || gameStatus == GameStatus.Over) {
            Text(
                text = "Click anywhere to start the game",
                style = MaterialTheme.typography.headlineMedium,
            )
        }
    }
}