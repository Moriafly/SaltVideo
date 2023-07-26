package com.salt.video.ui.player

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.moriafly.salt.ui.SaltTheme
import com.salt.video.R
import com.salt.video.ui.theme.VideoTheme

@Composable
fun PlayerFloatUI() {
    VideoTheme(
        darkTheme = true
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_fast_forward),
                contentDescription = null,
                modifier = Modifier
                    .size(28.dp),
                tint = SaltTheme.colors.text
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "X2",
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                style = SaltTheme.textStyles.main
            )
        }
    }
}