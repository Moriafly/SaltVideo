package com.salt.video.ui.settings

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.moriafly.salt.ui.ItemSwitcher
import com.moriafly.salt.ui.ItemTitle
import com.moriafly.salt.ui.RoundedColumn
import com.moriafly.salt.ui.UnstableSaltApi
import com.salt.video.App
import com.salt.video.R
import com.salt.video.ui.base.BasicActivityColumn
import com.salt.video.ui.theme.VideoTheme
import com.salt.video.util.Config

class SettingsActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VideoTheme {
                SettingsUI()
            }
        }
    }

}

@OptIn(UnstableSaltApi::class)
@Composable
private fun SettingsUI() {
    BasicActivityColumn(
        text = "设置"
    ) {
        RoundedColumn {
            ItemTitle(text = "资源")

            var fileListShowHiddenFolders by remember {
                mutableStateOf(App.mmkv.decodeBool(Config.FILE_LIST_SHOW_HIDDEN_FOLDERS, Config.FILE_LIST_SHOW_HIDDEN_FOLDERS_DEFAULT))
            }
            ItemSwitcher(
                state = fileListShowHiddenFolders,
                onChange = {
                    fileListShowHiddenFolders = it
                    App.mmkv.encode(Config.FILE_LIST_SHOW_HIDDEN_FOLDERS, it)
                },
                text = stringResource(id = R.string.file_list_show_hidden_folders),
                sub = "显示以 . 开头的隐藏文件夹",
                iconPainter = painterResource(id = R.drawable.ic_hide_video),
                iconColor = Color(0xFF75878A)
            )

//            ItemSwitcher(
//                state = false,
//                onChange = {
//
//                },
//                text = "文件列表显示隐藏文件",
//                sub = "显示以 . 开头的隐藏文件",
//                iconPainter = painterResource(id = R.drawable.ic_hide_file),
//                iconColor = Color(0xFF75878A)
//            )
        }

        RoundedColumn {
            ItemTitle(text = stringResource(id = R.string.video))

            var mediaCodec by remember { mutableStateOf(App.mmkv.decodeBool(Config.MEDIA_CODEC, Config.MEDIA_CODEC_DEFAULT)) }
            ItemSwitcher(
                state = mediaCodec,
                onChange = {
                    mediaCodec = it
                    App.mmkv.encode(Config.MEDIA_CODEC, it)
                },
                text = "硬件解码",
                iconPainter = painterResource(id = R.drawable.ic_cpu),
                iconColor = Color(0xFFC9DD22)
            )

//            Item(
//                onClick = {
//
//                },
//                text = "渲染控件",
//                sub = "SurfaceView",
//                iconPainter = painterResource(id = R.drawable.ic_loading),
//                iconColor = Color(0xFF1BD1A5)
//            )
        }
    }
}