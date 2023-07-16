package com.salt.video.ui.player

import android.app.Activity
import android.app.PictureInPictureParams
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.util.Rational
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.SeekBar
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.Lifecycle
import com.blankj.utilcode.util.BarUtils
import com.salt.video.App
import com.salt.video.R
import com.salt.video.core.PlayerState
import com.salt.video.databinding.ActivityPlayerBinding
import com.salt.video.util.Config
import com.shuyu.gsyvideoplayer.player.PlayerFactory
import com.shuyu.gsyvideoplayer.utils.GSYVideoType
import eightbitlab.com.blurview.RenderScriptBlur
import tv.danmaku.ijk.media.exo2.Exo2PlayerManager
import android.graphics.Color as AndroidColor

class PlayerActivity : AppCompatActivity() {

    private val playerViewModel: PlayerViewModel by viewModels()

    private lateinit var binding: ActivityPlayerBinding

    private lateinit var ivShotPic: ImageView

    /** 标题栏和底部操作栏是否处于显示状态 */
    private var titleAndBottomBarVisibility = false

    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                HANDLER_MSG_SHOT_PIC -> {
                    if (titleAndBottomBarVisibility && binding.saltVideoPlayer.currentVideoHeight > 0 && binding.saltVideoPlayer.currentVideoWidth > 0) {
                        binding.saltVideoPlayer.taskShotPic { bitmap ->
                            ivShotPic.setImageBitmap(bitmap)
                            sendEmptyMessageDelayed(HANDLER_MSG_SHOT_PIC, HANDLER_MSG_SHOT_PIC_DELAY)
                        }
                    } else {

                    }
                }

                HANDLER_MSG_HIDE_UI -> {
                    // TODO 可能用户会点击其他地方
                    // hideTitleAndBottomBar()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 使用 Exo2 内核
        PlayerFactory.setPlayManager(Exo2PlayerManager::class.java)
//        PlayerFactory.setPlayManager(SystemPlayerManager::class.java)
//        PlayerFactory.setPlayManager(IjkPlayerManager::class.java)

        // 支持 HDR https://developer.android.google.cn/guide/topics/media/hdr-playback#set_up_hdr_playback_in_your_app
        GSYVideoType.setRenderType(GSYVideoType.SUFRACE)
        val mediaCodec = App.mmkv.decodeBool(Config.MEDIA_CODEC, false)
        if (mediaCodec) {
            GSYVideoType.enableMediaCodec()
        } else {
            GSYVideoType.disableMediaCodec()
        }

        val radius = 25f

        val decorView = window.decorView
        // ViewGroup you want to start blur from. Choose root as close to BlurView in hierarchy as possible.
        val rootView = decorView.findViewById(android.R.id.content) as ViewGroup


        rootView.setOnApplyWindowInsetsListener { view, windowInsets ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                binding.blurViewTitleBar.updateLayoutParams<ConstraintLayout.LayoutParams> {
                    height = dpToPx(this@PlayerActivity, 56f) + (windowInsets.displayCutout?.safeInsetTop ?: 0)
                }
                binding.blurViewTitleBar.setPadding(
                    windowInsets.displayCutout?.safeInsetLeft ?: 0,
                    0,
                    windowInsets.displayCutout?.safeInsetRight ?: 0,
                    0
                )
                binding.blurViewBottomBar.setPadding(
                    windowInsets.displayCutout?.safeInsetLeft ?: 0,
                    0,
                    windowInsets.displayCutout?.safeInsetRight ?: 0,
                    0
                )
            }
            windowInsets
        }

        // Optional:
        // Set drawable to draw in the beginning of each blurred frame.
        // Can be used in case your layout has a lot of transparent space and your content
        // gets a too low alpha value after blur is applied.
        val windowBackground = decorView.getBackground();
        val flShot = findViewById<FrameLayout>(R.id.flShot)
        ivShotPic = findViewById<ImageView>(R.id.ivShotPic)

        binding.blurViewTitleBar.setupWith(flShot, RenderScriptBlur(this)) // or RenderEffectBlur
            .setFrameClearDrawable(windowBackground) // Optional
            .setBlurRadius(radius)

        val clBottomBar = findViewById<ConstraintLayout>(R.id.clBottomBar)
        binding.blurViewBottomBar.setupWith(flShot, RenderScriptBlur(this)) // or RenderEffectBlur
            .setFrameClearDrawable(windowBackground) // Optional
            .setBlurRadius(radius)

        val data = intent.data
        Log.d(TAG, "intent.data = $data")
        if (data == null) {
            val url = intent.getStringExtra(EXTRA_URL)
            val title = intent.getStringExtra(EXTRA_TITLE)
            if (url != null && title != null) {
                binding.saltVideoPlayer.setUp(url, false, "")
                binding.saltVideoPlayer.startPlayLogic()
                playerViewModel.postTitle(title)
            }
        } else {
            val url = data.toString()
            val title = url
            if (url != null && title != null) {
                binding.saltVideoPlayer.setUp(url, false, "")
                binding.saltVideoPlayer.startPlayLogic()
                playerViewModel.postTitle(title)
            }
        }

        initView()
    }

    private fun initView() {
        with(binding) {

            composeViewTitleBar.setContent {
                TitleBarUI(
                    onBack = {
                        onBackPressed()
                    },
                    playerViewModel = playerViewModel
                )
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

            // Listener is called immediately after the user exits PiP but before animating.
//            saltVideoPlayer.addOnLayoutChangeListener { _, left, top, right, bottom,
//                                                   oldLeft, oldTop, oldRight, oldBottom ->
//                if (left != oldLeft || right != oldRight || top != oldTop
//                    || bottom != oldBottom) {
//                    // The playerView's bounds changed, update the source hint rect to
//                    // reflect its new bounds.
//                    val sourceRectHint = Rect()
//                    saltVideoPlayer.getGlobalVisibleRect(sourceRectHint)
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                        setPictureInPictureParams(
//                            PictureInPictureParams.Builder()
//                                .setSourceRectHint(sourceRectHint)
//                                .build()
//                        )
//                    }
//                }
//            }
            saltVideoPlayer.onPlayerStateChange = {
                when (it) {
                    PlayerState.RESUME -> ivPlayerState.setImageResource(R.drawable.ic_pause)
                    PlayerState.PAUSE -> ivPlayerState.setImageResource(R.drawable.ic_play)
                }
            }
            saltVideoPlayer.onVideoSizeChangeListener = { width: Int, height: Int, numerator: Int, denominator: Int ->
                Log.d(TAG, "video w = $width, h = $height")
                if (width > height) {
                    this@PlayerActivity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                } else {
                    this@PlayerActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT)
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    setPictureInPictureParams(
                        PictureInPictureParams.Builder()
                            .setAspectRatio(Rational(width, height))
                            .build()
                    )
                }
                handler.sendEmptyMessageDelayed(HANDLER_MSG_SHOT_PIC, HANDLER_MSG_SHOT_PIC_DELAY)
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


            seekBar.thumb = ContextCompat.getDrawable(this@PlayerActivity, R.drawable.ic_orange)
            // seekBar.thumb.colorFilter = PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC)
            seekBar.progressDrawable.colorFilter = PorterDuffColorFilter(AndroidColor.WHITE, PorterDuff.Mode.SRC_IN)
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
                    saltVideoPlayer.seekTo(pro.toLong().coerceAtLeast(1L))
                }

            })
        }
    }

    private fun hideTitleAndBottomBar() {
        binding.blurViewTitleBar.visibility = View.INVISIBLE
        binding.blurViewBottomBar.visibility = View.INVISIBLE
        titleAndBottomBarVisibility = false
        handler.removeMessages(HANDLER_MSG_SHOT_PIC)
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
        handler.sendEmptyMessageDelayed(HANDLER_MSG_HIDE_UI, HANDLER_MSG_HIDE_UI_DELAY)
        handler.sendEmptyMessage(HANDLER_MSG_SHOT_PIC)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean, newConfig: Configuration) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        if (isInPictureInPictureMode) {
            hideTitleAndBottomBar()
            // Hide the full-screen UI (controls, etc.) while in picture-in-picture mode.
        } else {
            // Restore the full-screen UI.
        }

        if (lifecycle.currentState == Lifecycle.State.CREATED) {
            finishAndRemoveTask();
            // when user click on Close button of PIP this will trigger, do what you want here
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume()")
        hideNavigationBar()
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause(), isInPictureInPictureMode = $isInPictureInPictureMode")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop(), isInPictureInPictureMode = $isInPictureInPictureMode")

        if (isInPictureInPictureMode) {
            // onDestroy()
        }
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

            Configuration.ORIENTATION_SQUARE -> {

            }

            Configuration.ORIENTATION_UNDEFINED -> {

            }
        }
//        setContentView(R.layout.activity_player)
//        saltVideoPlayer = findViewById<SaltVideoPlayer>(R.id.saltVideoPlayer)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.saltVideoPlayer.currentPlayer.release()
    }

    private fun hideNavigationBar() {
        BarUtils.setStatusBarVisibility(this, false)
        BarUtils.setNavBarVisibility(this, false)
    }

    companion object {
        private const val TAG = "PlayerActivity"

        const val EXTRA_URL = "extra_url"
        const val EXTRA_TITLE = "extra_title"

        private const val HANDLER_MSG_SHOT_PIC = 1001
        private const val HANDLER_MSG_SHOT_PIC_DELAY = 100L

        private const val HANDLER_MSG_HIDE_UI = 1002
        private const val HANDLER_MSG_HIDE_UI_DELAY = 5000L

        fun start(activity: Activity, url: String, title: String) {
            val intent = Intent(activity, PlayerActivity::class.java)
            intent.putExtra(EXTRA_URL, url)
            intent.putExtra(EXTRA_TITLE, title)
            activity.startActivity(intent)
        }
    }

}

/**
 * 转换成时间格式
 */
private fun Long.toTimeFormat(): String {
    // 时，分，秒
    val hour = this / (60 * 60 * 1000)
    val min = this % (60 * 60 * 1000) / (60 * 1000)
    val sec = this % (60 * 1000) / 1000
    return if (hour == 0L) {
        String.format("%02d:%02d", min, sec)
    } else {
        String.format("%02d:%02d:%02d", hour, min, sec)
    }
}

private fun dpToPx(context: Context, dp: Float): Int {
    val density: Float = context.resources.getDisplayMetrics().density
    return Math.round(dp * density)
}
