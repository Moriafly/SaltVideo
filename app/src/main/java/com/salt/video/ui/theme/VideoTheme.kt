package com.salt.video.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.moriafly.salt.ui.SaltTheme
import com.moriafly.salt.ui.darkSaltColors
import com.moriafly.salt.ui.lightSaltColors

@Composable
fun VideoTheme(
    content: @Composable () -> Unit
) {
    val colors = if (isSystemInDarkTheme()) {
        darkSaltColors()
    } else {
        lightSaltColors()
    }
    SaltTheme(
        colors = colors
    ) {
        content()
    }
}