package com.salt.video.ui.player

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moriafly.salt.ui.SaltTheme
import com.moriafly.salt.ui.UnstableSaltApi
import com.moriafly.salt.ui.fadeClickable
import com.salt.video.R
import com.salt.video.core.PlayerState
import com.salt.video.ui.theme.VideoTheme

@OptIn(UnstableSaltApi::class)
@Composable
fun BottomBarUI(
    onPlayPauseClick: () -> Unit,
    onSpeedClick: () -> Unit,
    onPictureInPictureClick: () -> Unit,
    onScreenRotationClick: () -> Unit,
    playerViewModel: PlayerViewModel
) {
    VideoTheme(darkTheme = true) {
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

            Row(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp)
                    .height(48.dp)
            ) {

                Box(
                    modifier = Modifier
                        .height(48.dp)
                        .fadeClickable {
                            onSpeedClick()
                        }
                        .padding(horizontal = 14.dp)
                ) {
                    Text(
                        text = "倍数",
                        modifier = Modifier
                            .align(Alignment.Center),
                        fontSize = 14.sp,
                        style = SaltTheme.textStyles.main
                    )
                }


                Icon(
                    painter = painterResource(id = R.drawable.ic_picture_in_picture),
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .fadeClickable {
                            onPictureInPictureClick()
                        }
                        .padding(14.dp),
                    tint = SaltTheme.colors.text
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_screen_rotation),
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .fadeClickable {
                            onScreenRotationClick()
                        }
                        .padding(14.dp),
                    tint = SaltTheme.colors.text
                )
            }
        }
    }
}