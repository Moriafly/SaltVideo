package com.salt.video.ui.main.video

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.moriafly.salt.ui.Item
import com.moriafly.salt.ui.SaltTheme
import com.moriafly.salt.ui.TitleBar
import com.moriafly.salt.ui.UnstableSaltApi
import com.moriafly.salt.ui.fadeClickable
import com.salt.video.R
import com.salt.video.data.fui.MediaSourceFui
import com.salt.video.ui.localfolder.LocalFolderActivity
import com.salt.video.ui.main.MainActivity

@OptIn(UnstableSaltApi::class)
@Composable
fun VideoScreen(
    videoViewModel: VideoViewModel
) {
    val mainActivity = LocalContext.current as MainActivity
    val allMediaSource by videoViewModel.mediaSourceFuiList.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            TitleBar(
                onBack = { },
                text = stringResource(id = R.string.video),
                showBackBtn = false
            )
            var quickPlayDialog by remember { mutableStateOf(false) }
            Icon(
                modifier = Modifier
                    .fadeClickable {
                        quickPlayDialog = true
                    }
                    .size(56.dp)
                    .padding(16.dp),
                painter = painterResource(id = R.drawable.ic_play),
                contentDescription = null,
                tint = SaltTheme.colors.highlight
            )
            if (quickPlayDialog) {
                QuickPlayDialog(
                    onDismissRequest = {
                        quickPlayDialog = false
                    },
                    mainActivity = mainActivity
                )
            }

            var addMediaSourceDialog by remember { mutableStateOf(false) }
            Icon(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fadeClickable {
                        addMediaSourceDialog = true
                    }
                    .size(56.dp)
                    .padding(16.dp),
                painter = painterResource(id = R.drawable.ic_add),
                contentDescription = null,
                tint = SaltTheme.colors.highlight
            )
            if (addMediaSourceDialog) {
                AddMediaSourceDialog(
                    onDismissRequest = {
                        addMediaSourceDialog = false
                    },
                    mainActivity = mainActivity
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        ) {
            allMediaSource?.let { allMediaSource ->
                itemsIndexed(allMediaSource) { index, mediaSourceFui ->
                    MediaSourceItem(
                        onClick = {
                            val directoryUri = mediaSourceFui.directoryUri

                            if (directoryUri != "") {
                                LocalFolderActivity.start(mainActivity, directoryUri)
                            }
                        },
                        mediaSourceFui = mediaSourceFui
                    )
                }
            }

        }
    }
}

@Composable
private fun MediaSourceItem(
    onClick: () -> Unit,
    mediaSourceFui: MediaSourceFui
) {
    Item(
        onClick = onClick,
        iconPainter = painterResource(id = R.drawable.ic_folder),
        iconColor = SaltTheme.colors.highlight,
        text = mediaSourceFui.title
    )
}