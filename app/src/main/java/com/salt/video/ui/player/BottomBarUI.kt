package com.salt.video.ui.player

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kongzue.dialogx.dialogs.MessageDialog
import com.moriafly.salt.ui.SaltTheme
import com.moriafly.salt.ui.UnstableSaltApi
import com.moriafly.salt.ui.darkSaltColors
import com.moriafly.salt.ui.fadeClickable
import com.salt.video.R
import com.salt.video.core.PlayerState

@OptIn(UnstableSaltApi::class)
@Composable
fun BottomBarUI(
    onPlayPauseClick: () -> Unit,
    playerViewModel: PlayerViewModel
) {
    SaltTheme(
        colors = darkSaltColors()
    ) {
        val playerState = playerViewModel.playerState
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                painter = when (playerState) {
                    PlayerState.PAUSE -> painterResource(id = R.drawable.ic_play)
                    PlayerState.RESUME -> painterResource(id = R.drawable.ic_pause)
                },
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 16.dp)
                    .size(48.dp)
                    .fadeClickable {
                        onPlayPauseClick()
                    }
                    .padding(12.dp),
                tint = SaltTheme.colors.text
            )
        }
    }
}