package com.salt.video.ui.main.video

import android.provider.DocumentsContract
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import com.moriafly.salt.ui.Item
import com.moriafly.salt.ui.ItemContainer
import com.moriafly.salt.ui.RoundedColumn
import com.moriafly.salt.ui.SaltTheme
import com.moriafly.salt.ui.TextButton
import com.salt.video.R
import com.salt.video.ui.localfolder.LocalFolderActivity
import com.salt.video.ui.main.MainActivity

@Composable
fun VideoScreen(
    videoViewModel: VideoViewModel
) {
    val context = LocalContext.current
    val mainActivity = LocalContext.current as MainActivity
    val allMediaSource by videoViewModel.getAllMediaSource().collectAsState(initial = null)
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        item {
            RoundedColumn {
                Item(
                    onClick = {
                        mainActivity.openDocumentLauncher("video/*")
                    },
                    iconPainter = painterResource(id = R.drawable.ic_video_file),
                    iconColor = SaltTheme.colors.highlight,
                    text = "单个本地视频"
                )
                Item(
                    onClick = {
                        mainActivity.openDocumentLauncher("audio/*")
                    },
                    iconPainter = painterResource(id = R.drawable.ic_audio_file),
                    iconColor = SaltTheme.colors.highlight,
                    text = "单个本地音乐"
                )
                Item(
                    onClick = {
                        mainActivity.openDialog()
                    },
                    iconPainter = painterResource(id = R.drawable.ic_wifi_tethering),
                    iconColor = SaltTheme.colors.highlight,
                    text = "单个网络音视频"
                )
            }
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                    .background(SaltTheme.colors.subBackground)
            ) {
                ItemContainer {
                    TextButton(
                        onClick = {
                            mainActivity.openDocumentTreeLauncher()
                        },
                        text = "添加本地文件夹"
                    )
                }
            }
        }

        allMediaSource?.let { mediaSources ->
            itemsIndexed(mediaSources) {index, mediaSource ->
                val treeUri = mediaSource.url.toUri()
                val documentFile = remember { DocumentFile.fromTreeUri(context, treeUri) }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clip(
                            if (index == mediaSources.lastIndex) RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp) else RoundedCornerShape(0.dp)
                        )
                        .background(SaltTheme.colors.subBackground)
                ) {
                    Item(
                        onClick = {
                            // mediaSource 是 treeUri
                            // 调用路径页面需要传入普通的 uri （非 treeUri）
                            if (documentFile != null) {
                                val documentId = DocumentsContract.getDocumentId(documentFile.uri)
                                val directoryUri = DocumentsContract.buildChildDocumentsUriUsingTree(treeUri, documentId)
                                LocalFolderActivity.start(mainActivity, directoryUri.toString())
                            }
                        },
                        iconPainter = painterResource(id = R.drawable.ic_folder),
                        iconColor = SaltTheme.colors.highlight,
                        text = documentFile?.name ?: ""
                    )
                }
            }
        }
    }
}