package com.salt.video.ui.webdav

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import com.salt.video.ui.base.BasicActivityColumn
import com.salt.video.ui.theme.VideoTheme

class AddWebDAVActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VideoTheme {
                AddWebDAVUI()
            }
        }
    }

}

@Composable
private fun AddWebDAVUI() {
    BasicActivityColumn(
        text = "添加 WebDAV"
    ) {

    }
}