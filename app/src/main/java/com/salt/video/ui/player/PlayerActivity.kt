package com.salt.video.ui.player

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.blankj.utilcode.util.ScreenUtils
import com.salt.video.R
import com.salt.video.core.SaltVideoPlayer
import eightbitlab.com.blurview.BlurView
import eightbitlab.com.blurview.RenderEffectBlur
import eightbitlab.com.blurview.RenderScriptBlur
import kotlinx.coroutines.launch


class PlayerActivity : AppCompatActivity() {

    private var showUI = false

    private lateinit var saltVideoPlayer: SaltVideoPlayer

    private lateinit var ivShotPic: ImageView

    private val shotPicHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            if (msg.what == HANDLER_MSG_SHOT_PIC) {
                if (saltVideoPlayer.currentVideoHeight > 0 && saltVideoPlayer.currentVideoWidth > 0) {
                    saltVideoPlayer.taskShotPic {
                        ivShotPic.setImageBitmap(it)
                        // Log.d(TAG, "shot")
                    }
                }

                sendEmptyMessageDelayed(HANDLER_MSG_SHOT_PIC, HANDLER_MSG_SHOT_PIC_DELAY)
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ScreenUtils.isScreenLock()

        setContentView(R.layout.activity_player)
        saltVideoPlayer = findViewById<SaltVideoPlayer>(R.id.saltVideoPlayer)

        val radius = 25f

        val decorView = getWindow().getDecorView();
        // ViewGroup you want to start blur from. Choose root as close to BlurView in hierarchy as possible.
        val rootView = decorView.findViewById(android.R.id.content) as ViewGroup

        // Optional:
        // Set drawable to draw in the beginning of each blurred frame.
        // Can be used in case your layout has a lot of transparent space and your content
        // gets a too low alpha value after blur is applied.
        val windowBackground = decorView.getBackground();
        val flShot = findViewById<FrameLayout>(R.id.flShot)
        ivShotPic = findViewById<ImageView>(R.id.ivShotPic)

        val clTitleBar = findViewById<ConstraintLayout>(R.id.clTitleBar)
        val blurView = findViewById<BlurView>(R.id.blurView)
        blurView.setupWith(flShot, RenderScriptBlur(this)) // or RenderEffectBlur
            .setFrameClearDrawable(windowBackground) // Optional
            .setBlurRadius(radius)

        val clBottomBar = findViewById<ConstraintLayout>(R.id.clBottomBar)
        val blurViewBottomBar = findViewById<BlurView>(R.id.blurViewBottomBar)
        blurViewBottomBar.setupWith(flShot, RenderScriptBlur(this)) // or RenderEffectBlur
            .setFrameClearDrawable(windowBackground) // Optional
            .setBlurRadius(radius)


        val url = intent.getStringExtra(EXTRA_URL)

        saltVideoPlayer.setUp(url, false, "")
        saltVideoPlayer.onVideoSizeChangeListener = { width, height ->
            Log.d(TAG, "video w = $width, h = $height")
            if (width > height) {
                this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE)
            } else {
                this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT)
            }
            shotPicHandler.sendEmptyMessageDelayed(HANDLER_MSG_SHOT_PIC, HANDLER_MSG_SHOT_PIC_DELAY)
        }

        clTitleBar.visibility = View.INVISIBLE
        clBottomBar.visibility = View.INVISIBLE
        saltVideoPlayer.onClickUiToggle = {
            if (clTitleBar.visibility == View.VISIBLE) clTitleBar.visibility = View.INVISIBLE else clTitleBar.visibility = View.VISIBLE
            if (clBottomBar.visibility == View.VISIBLE) clBottomBar.visibility = View.INVISIBLE else clBottomBar.visibility = View.VISIBLE
        }
//        saltVideoPlayer.isRotateViewAuto = true
//        saltVideoPlayer.isAutoFullWithSize = true
//        saltVideoPlayer.isOnlyRotateLand = false

        saltVideoPlayer.startPlayLogic()
    }

    override fun onResume() {
        super.onResume()
        hideNavigationBar()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        hideNavigationBar()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        when (newConfig.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {

                Log.d(TAG, "onConfigurationChanged LANDSCAPE")
            }
            Configuration.ORIENTATION_PORTRAIT -> {
                Log.d(TAG, "onConfigurationChanged PORTRAIT")
            }
        }
//        setContentView(R.layout.activity_player)
//        saltVideoPlayer = findViewById<SaltVideoPlayer>(R.id.saltVideoPlayer)
    }

    override fun onDestroy() {
        super.onDestroy()
        saltVideoPlayer.currentPlayer.release()
    }

    private fun hideNavigationBar() {
        val decorView: View = window.decorView
        val uiOptions: Int = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
        decorView.setSystemUiVisibility(uiOptions)
    }

    companion object {
        private const val TAG = "PlayerActivity"

        const val EXTRA_URL = "extra_url"

        private const val HANDLER_MSG_SHOT_PIC = 1001
        private const val HANDLER_MSG_SHOT_PIC_DELAY = 42L
    }

}