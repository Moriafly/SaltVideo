package com.salt.video.ui.main.video

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moriafly.salt.ui.Item
import com.moriafly.salt.ui.ItemSpacer
import com.moriafly.salt.ui.RoundedColumn
import com.moriafly.salt.ui.SaltTheme
import com.moriafly.salt.ui.UnstableSaltApi
import com.moriafly.salt.ui.dialog.BottomSheetDialog
import com.salt.video.R
import com.salt.video.ui.main.MainActivity

@OptIn(UnstableSaltApi::class)
@Composable
fun QuickPlayDialog(
    onDismissRequest: () -> Unit,
    mainActivity: MainActivity
) {
    BottomSheetDialog(
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            ItemSpacer()
            DialogTitle(title = "快速播放")
            RoundedColumn {
                Item(
                    onClick = {
                        mainActivity.openDocumentLauncher("video/*")
                        onDismissRequest()
                    },
                    iconPainter = painterResource(id = R.drawable.ic_video_file),
                    iconColor = SaltTheme.colors.highlight,
                    text = "单个本地视频"
                )
                Item(
                    onClick = {
                        mainActivity.openDocumentLauncher("audio/*")
                        onDismissRequest()
                    },
                    iconPainter = painterResource(id = R.drawable.ic_audio_file),
                    iconColor = SaltTheme.colors.highlight,
                    text = "单个本地音乐"
                )
                Item(
                    onClick = {
                        mainActivity.openDialog()
                        onDismissRequest()
                    },
                    iconPainter = painterResource(id = R.drawable.ic_wifi_tethering),
                    iconColor = SaltTheme.colors.highlight,
                    text = "单个网络音视频",
                    sub = "支持 HTTP / HTTPS / M3U8"
                )
            }
            Spacer(modifier = Modifier.navigationBarsPadding())
        }

    }
}

@Composable
fun DialogTitle(title: String) {
    Text(
        modifier = Modifier.padding(16.dp, 8.dp),
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = SaltTheme.colors.text
    )
}