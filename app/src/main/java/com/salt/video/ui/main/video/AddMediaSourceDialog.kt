package com.salt.video.ui.main.video

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.moriafly.salt.ui.Item
import com.moriafly.salt.ui.ItemSpacer
import com.moriafly.salt.ui.ItemText
import com.moriafly.salt.ui.RoundedColumn
import com.moriafly.salt.ui.SaltTheme
import com.moriafly.salt.ui.UnstableSaltApi
import com.moriafly.salt.ui.dialog.BottomSheetDialog
import com.salt.video.R
import com.salt.video.ui.main.MainActivity

@OptIn(UnstableSaltApi::class)
@Composable
fun AddMediaSourceDialog(
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
            DialogTitle(title = "添加媒体资源")
            RoundedColumn {
                Item(
                    onClick = {
                        mainActivity.openDocumentTreeLauncher()
                        onDismissRequest()
                    },
                    iconPainter = painterResource(id = R.drawable.ic_folder),
                    iconColor = SaltTheme.colors.highlight,
                    text = "本地文件夹"
                )
                Item(
                    onClick = {
                        // mainActivity.openDocumentTreeLauncher()
                        onDismissRequest()
                    },
                    iconPainter = painterResource(id = R.drawable.ic_cloud),
                    iconColor = SaltTheme.colors.highlight,
                    text = "WebDAV 服务器"
                )
            }
            Spacer(modifier = Modifier.navigationBarsPadding())
        }

    }
}