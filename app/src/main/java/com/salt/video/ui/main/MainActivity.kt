package com.salt.video.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dylanc.activityresult.launcher.OpenDocumentLauncher
import com.dylanc.activityresult.launcher.OpenDocumentTreeLauncher
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
            openDocumentLauncher.launch(
                "video/*",
            ) { uri ->
                Toast.makeText(this, "${uri.toString()}", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, PlayerActivity::class.java)
                intent.putExtra(PlayerActivity.EXTRA_URL, uri.toString())
                startActivity(intent)
            }
        }
    }
}