package com.salt.video.ui.player

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
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

@OptIn(UnstableSaltApi::class)
@Composable
fun TitleBarUI(
    onBack: () -> Unit,
    playerViewModel: PlayerViewModel
) {
    SaltTheme(
        colors = darkSaltColors()
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .padding(12.dp)
                    .fadeClickable {
                        onBack()
                    },
                tint = Color.White
            )
            val context = LocalContext.current
            val title = playerViewModel.title
            Text(
                text = title ?: "",
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .fadeClickable {
                        MessageDialog.show(context.getString(R.string.video), (title ?: ""), context.getString(R.string.confirm))
                    },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = SaltTheme.textStyles.main
            )
        }
    }
}