package com.shivam.flappybird

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.shivam.flappybird.sound.rememberSoundEffects
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Bird(
    gameStatus: GameStatus,
    onGameOver: () -> Unit,
    onGameStart: () -> Unit
) {
    val soundEffects = rememberSoundEffects()
    var upWings by remember {
        mutableStateOf(false)
    }
    var goingDown by remember {
        mutableStateOf(true)
    }
    var screenHeight by remember { mutableIntStateOf(0) }
    val birdHeight = with(LocalDensity.current) {
        50.dp.toPx()
    }
    val degree by animateFloatAsState(if (goingDown) 30f else -30f, label = "")
    val coroutineScope = rememberCoroutineScope()
    var birdY by remember { mutableFloatStateOf(0f) }
    val gravity = 0.3f
    val jumpHeight = -15f
    var velocity by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(gameStatus) {
        while (gameStatus == GameStatus.Running) {
            if (birdY >= screenHeight/2f) {
                onGameOver()
                birdY = (screenHeight-birdHeight)/2f
            }
            birdY += velocity
            velocity += gravity
            delay(16L)
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned {
                screenHeight = it.size.height
            }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                if (gameStatus == GameStatus.Running) {
                    coroutineScope.launch {
                        velocity = jumpHeight
                        goingDown = false
                        upWings = true
                        soundEffects.playBirdSound()
                        delay(150)
                        upWings = false
                        goingDown = true
                    }
                } else {
                    onGameStart()
                    birdY = 0f
                    velocity = 0f
                }
            },
        contentAlignment = Alignment.CenterStart
    ) {
        Image(
            modifier = Modifier
                .size(50.dp)
                .graphicsLayer {
                    rotationZ = degree
                    translationY = birdY
                },
            painter = if (upWings) painterResource(R.drawable.bird_up) else painterResource(R.drawable.bird_down),
            contentDescription = null
        )
    }
    DisposableEffect(Unit) {
        onDispose {
            soundEffects.onDispose()
        }
    }
}