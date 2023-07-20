package com.salt.video.ui.player

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.moriafly.salt.ui.ItemSpacer
import com.moriafly.salt.ui.ItemSwitcher
import com.moriafly.salt.ui.SaltTheme
import com.moriafly.salt.ui.UnstableSaltApi
import com.salt.video.App
import com.salt.video.R
import com.salt.video.ui.theme.VideoTheme
import com.salt.video.util.Config

@Composable
fun PlayerPanel(
    onSpeedChange: (Float) -> Unit,
    playerViewModel: PlayerViewModel
) {
    val playerPanelState = playerViewModel.playerPanelState
    VideoTheme(
        darkTheme = true
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            ItemSpacer()
            ItemSpacer()
            ItemSpacer()
            Text(
                text = when(playerPanelState) {
                    PlayerPanelState.SPEED -> "Speed"
                    else -> "NONE"
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                style = SaltTheme.textStyles.main
            )
            PanelDivider()
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
                when(playerPanelState) {
                    PlayerPanelState.SPEED -> SpeedPanel(
                        onSpeedChange = onSpeedChange
                    )
                    else -> {

                    }
                }
            }
        }
    }
}

@Composable
private fun PanelDivider() {
    Divider(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 12.dp),
        color = SaltTheme.colors.subText.copy(alpha = 0.2f),
        thickness = Dp.Hairline
    )
}

@OptIn(UnstableSaltApi::class)
@Composable
private fun SpeedPanel(
    onSpeedChange: (Float) -> Unit
) {
    var playerSpeed by remember { mutableFloatStateOf(App.mmkv.decodeFloat(Config.PLAYER_SPEED, 1f)) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer { alpha = 0.99f }
            .drawWithContent {
                val colors =
                    listOf(
                        Color.Black, Color.Black, Color.Black, Color.Black, Color.Transparent
                    )
                drawContent()
                drawRect(
                    brush = Brush.verticalGradient(colors),
                    blendMode = BlendMode.DstIn
                )
            }
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center
    ) {
        SpeedItem(
            selected = playerSpeed == 0.25f,
            onClick = {
                onSpeedChange(0.25f)
                playerSpeed = 0.25f
            },
            text = "x0.25"
        )
        SpeedItem(
            selected = playerSpeed == 0.5f,
            onClick = {
                onSpeedChange(0.5f)
                playerSpeed = 0.5f
            },
            text = "x0.50"
        )
        SpeedItem(
            selected = playerSpeed == 0.75f,
            onClick = {
                onSpeedChange(0.75f)
                playerSpeed = 0.75f
            },
            text = "x0.75"
        )
        SpeedItem(
            selected = playerSpeed == 1f,
            onClick = {
                onSpeedChange(1f)
                playerSpeed = 1f
            },
            text = "x1.00"
        )
        SpeedItem(
            selected = playerSpeed == 1.25f,
            onClick = {
                onSpeedChange(1.25f)
                playerSpeed = 1.25f
            },
            text = "x1.25"
        )
        SpeedItem(
            selected = playerSpeed == 1.5f,
            onClick = {
                onSpeedChange(1.5f)
                playerSpeed = 1.5f
            },
            text = "x1.5"
        )
        SpeedItem(
            selected = playerSpeed == 2f,
            onClick = {
                onSpeedChange(2f)
                playerSpeed = 2f
            },
            text = "x2.00"
        )
        SpeedItem(
            selected = playerSpeed == 4f,
            onClick = {
                onSpeedChange(4f)
                playerSpeed = 4f
            },
            text = "x4.00"
        )
        PanelDivider()
        ItemSwitcher(
            state = false,
            onChange = {

            },
            text = "变速不变调"
        )

        ItemSpacer()
        ItemSpacer()
        ItemSpacer()
    }
}

@Composable
private fun SpeedItem(
    selected: Boolean,
    onClick: () -> Unit,
    text: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(16.dp, 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier,
            text = text,
            style = SaltTheme.textStyles.main.copy(
                shadow = if (selected) Shadow(Color.White, Offset(2f, 2f), 16f) else null
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Image(
            painter = painterResource(id = R.drawable.ic_orange),
            contentDescription = null,
            modifier = Modifier
                .size(16.dp)
                .alpha(if (selected) 1f else 0f)
        )
    }
}
