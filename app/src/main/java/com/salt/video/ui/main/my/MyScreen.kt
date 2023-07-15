package com.salt.video.ui.main.my

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.moriafly.salt.ui.ItemTitle
import com.moriafly.salt.ui.RoundedColumn
import com.moriafly.salt.ui.SaltUILogo
import com.salt.video.R

@Composable
fun MyScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        SaltUILogo()

        RoundedColumn {
            ItemTitle(text = stringResource(id = R.string.app_name))
        }
    }
}