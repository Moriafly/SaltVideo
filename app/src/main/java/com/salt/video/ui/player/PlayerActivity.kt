package com.salt.video.ui.player

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.blankj.utilcode.util.ScreenUtils
import com.dso.ext.toTimeFormat
import com.salt.video.R
import com.salt.video.core.PlayerState
import com.salt.video.core.SaltVideoPlayer
import com.salt.video.databinding.ActivityPlayerBinding
import com.shuyu.gsyvideoplayer.player.PlayerFactory
import com.shuyu.gsyvideoplayer.video.base.GSYVideoView
import eightbitlab.com.blurview.BlurView
import eightbitlab.com.blurview.RenderScriptBlur
import tv.danmaku.ijk.media.exo2.Exo2PlayerManager


class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding

    private lateinit var ivShotPic: ImageView

    private val shotPicHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            if (msg.what == HANDLER_MSG_SHOT_PIC) {
                if (binding.saltVideoPlayer.currentVideoHeight > 0 && binding.saltVideoPlayer.currentVideoWidth > 0) {
                    binding.saltVideoPlayer.taskShotPic {
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
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 使用 Exo2 内核
        PlayerFactory.setPlayManager(Exo2PlayerManager::class.java)

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

        val blurViewTitleBar = findViewById<BlurView>(R.id.blurViewTitleBar)
        blurViewTitleBar.setupWith(flShot, RenderScriptBlur(this)) // or RenderEffectBlur
            .setFrameClearDrawable(windowBackground) // Optional
            .setBlurRadius(radius)

        val clBottomBar = findViewById<ConstraintLayout>(R.id.clBottomBar)
        val blurViewBottomBar = findViewById<BlurView>(R.id.blurViewBottomBar)
        blurViewBottomBar.setupWith(flShot, RenderScriptBlur(this)) // or RenderEffectBlur
            .setFrameClearDrawable(windowBackground) // Optional
            .setBlurRadius(radius)

        val url = intent.getStringExtra(EXTRA_URL)
        val title = intent.getStringExtra(EXTRA_TITLE)
        if (url != null && title != null) {
            binding.saltVideoPlayer.setUp(url, false, "")
            binding.saltVideoPlayer.startPlayLogic()
            binding.tvTitle.text = title
        }

        initView()
    }

    private fun initView() {
        with(binding) {
            ivBack.setOnClickListener {
                finish()
            }

            ivPlayerState.setOnClickListener {
                if (saltVideoPlayer.gsyVideoManager.isPlaying) {
                    saltVideoPlayer.onVideoPause()
                } else {
                    saltVideoPlayer.onVideoResume()
                }
            }

            ivPictureInPicture.setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    this@PlayerActivity.enterPictureInPictureMode()
                }
            }

            ivRotation.setOnClickListener {
                if (this@PlayerActivity.requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT) {
                    this@PlayerActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE)
                } else {
                    this@PlayerActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT)
                }
            }

            saltVideoPlayer.onPlayerStateChange = {
                when (it) {
                    PlayerState.RESUME -> ivPlayerState.setImageResource(R.drawable.ic_pause)
                    PlayerState.PAUSE -> ivPlayerState.setImageResource(R.drawable.ic_play)
                }
            }
            saltVideoPlayer.onVideoSizeChangeListener = { width, height ->
                Log.d(TAG, "video w = $width, h = $height")
                if (width > height) {
                    this@PlayerActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE)
                } else {
                    this@PlayerActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT)
                }
                shotPicHandler.sendEmptyMessageDelayed(HANDLER_MSG_SHOT_PIC, HANDLER_MSG_SHOT_PIC_DELAY)
            }
            saltVideoPlayer.onSetProgressAndTime = { currentTime, totalTime ->
                seekBar.max = totalTime.toInt()
                seekBar.progress = currentTime.toInt()
                tvProgress.text = currentTime.toTimeFormat()
                tvDuration.text = totalTime.toTimeFormat()
            }

            hideTitleAndBottomBar()
            saltVideoPlayer.onClickUiToggle = {
                if (titleAndBottomBarVisibility) {
                    hideTitleAndBottomBar()
                } else {
                    showTitleAndBottomBar()
                }
            }

            seekBar.thumb = getDrawable(R.drawable.ic_orange)
            // seekBar.thumb.colorFilter = PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC)
            seekBar.progressDrawable.colorFilter = PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN)
            // 歌曲进度条变化的监听
            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

                var pro = 0

                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    // 判断是否为用户
                    if (fromUser) {
                        pro = progress
                        // binding.tvProgress.text = progress.toLong().toTimeFormat()
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {
                    // playerViewModel.seeking = true
                    pro = seekBar.progress
                }

                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    // playerViewModel.seeking = false
//                playerViewModel.setProgress(pro)
//                playerViewModel.refreshProgress()
                    saltVideoPlayer.seekTo(pro.toLong())
                }

            })
        }
    }

    private var titleAndBottomBarVisibility = false

    private fun hideTitleAndBottomBar() {
        binding.blurViewTitleBar.visibility = View.INVISIBLE
        binding.blurViewBottomBar.visibility = View.INVISIBLE
        titleAndBottomBarVisibility = false
    }

    private fun showTitleAndBottomBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (isInPictureInPictureMode) {
                return
            }
        }
        binding.blurViewTitleBar.visibility = View.VISIBLE
        binding.blurViewBottomBar.visibility = View.VISIBLE
        titleAndBottomBarVisibility = true
    }

    override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean,
                                               newConfig: Configuration) {
        if (isInPictureInPictureMode) {
            hideTitleAndBottomBar()
            // Hide the full-screen UI (controls, etc.) while in picture-in-picture mode.
        } else {
            // Restore the full-screen UI.
        }
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
        binding.saltVideoPlayer.currentPlayer.release()
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
        const val EXTRA_TITLE = "extra_title"

        private const val HANDLER_MSG_SHOT_PIC = 1001
        private const val HANDLER_MSG_SHOT_PIC_DELAY = 42L

        fun start(activity: Activity, url: String, title: String) {
            val intent = Intent(activity, PlayerActivity::class.java)
            intent.putExtra(EXTRA_URL, url)
            intent.putExtra(EXTRA_TITLE, title)
            activity.startActivity(intent)
        }
    }

}