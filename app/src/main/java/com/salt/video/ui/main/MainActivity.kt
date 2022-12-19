package com.salt.video.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.documentfile.provider.DocumentFile
import com.dylanc.activityresult.launcher.OpenDocumentLauncher
import com.dylanc.activityresult.launcher.OpenDocumentTreeLauncher
import com.kongzue.dialogx.dialogs.BottomMenu
import com.kongzue.dialogx.dialogs.InputDialog
import com.salt.video.R
import com.salt.video.ui.player.PlayerActivity

class MainActivity : AppCompatActivity() {

    /** SAF 选择 */
    private val openDocumentTreeLauncher = OpenDocumentTreeLauncher(this)
    private val openDocumentLauncher = OpenDocumentLauncher(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnTest = findViewById<Button>(R.id.btnTest)
        btnTest.setOnClickListener {
            BottomMenu.show(
                listOf("本地视频", "网络视频")
            )
                .setOnMenuItemClickListener { dialog, text, index ->
                    when (index) {
                        0 -> openDocumentLauncher()
                        1 -> openDialog()
                    }
                    return@setOnMenuItemClickListener false
                }

        }
    }

    private fun openDocumentLauncher() {
        openDocumentLauncher.launch(
            "video/*",
        ) { uri ->
            if (uri != null) {
                val documentFile = DocumentFile.fromSingleUri(this, uri)
                PlayerActivity.start(this, uri.toString(), documentFile?.name ?: "")
            }
        }
    }

    private fun openDialog() {
        InputDialog(
            "网络视频",
            "输入网络地址",
            "确定",
            "取消",
            ""
        )
            .setOkButton { dialog, v, inputStr ->
                val url = inputStr
                PlayerActivity.start(this, url, url)
                return@setOkButton false
            }
            .show()
    }
}