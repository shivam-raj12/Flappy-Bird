package com.shivam.flappybird.sound

import android.content.Context
import android.media.SoundPool
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.shivam.flappybird.R

class SoundEffects(
    context: Context
) {
    private val soundPool = SoundPool.Builder().setMaxStreams(1).build()
    private val birdSoundId = soundPool.load(context, R.raw.bird, 1)

    fun playBirdSound(){
        soundPool.play(birdSoundId, 1f, 1f, 1, 0, 1f)
    }

    fun onDispose(){
        soundPool.release()
    }
}

@Composable
fun rememberSoundEffects() : SoundEffects {
    val context = LocalContext.current
    return remember { SoundEffects(context) }
}