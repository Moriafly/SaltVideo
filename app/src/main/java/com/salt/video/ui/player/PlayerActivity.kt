package com.salt.video.ui.player

import android.app.Activity
import android.app.PictureInPictureParams
import android.app.PictureInPictureUiState
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.util.Rational
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import com.kongzue.dialogx.dialogs.MessageDialog
import com.salt.video.R
import com.salt.video.core.PlayerState
import com.salt.video.databinding.ActivityPlayerBinding
import com.shuyu.gsyvideoplayer.player.IjkPlayerManager
import com.shuyu.gsyvideoplayer.player.PlayerFactory
import com.shuyu.gsyvideoplayer.player.SystemPlayerManager
import com.shuyu.gsyvideoplayer.utils.GSYVideoType
import eightbitlab.com.blurview.BlurView
import eightbitlab.com.blurview.RenderScriptBlur
import tv.danmaku.ijk.media.exo2.Exo2PlayerManager

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding

    private lateinit var ivShotPic: ImageView

    /** 标题栏和底部操作栏是否处于显示状态 */
    private var titleAndBottomBarVisibility = false

    private val shotPicHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            if (msg.what == HANDLER_MSG_SHOT_PIC) {
                if (titleAndBottomBarVisibility && binding.saltVideoPlayer.currentVideoHeight > 0 && binding.saltVideoPlayer.currentVideoWidth > 0) {
                    binding.saltVideoPlayer.taskShotPic { bitmap ->
                        ivShotPic.setImageBitmap(bitmap)
                        sendEmptyMessageDelayed(HANDLER_MSG_SHOT_PIC, HANDLER_MSG_SHOT_PIC_DELAY)
                    }
                } else {

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

        val data = intent.data
        Log.d(TAG, "intent.data = $data")
        if (data == null) {
            val url = intent.getStringExtra(EXTRA_URL)
            val title = intent.getStringExtra(EXTRA_TITLE)
            if (url != null && title != null) {
                binding.saltVideoPlayer.setUp(url, false, "")
                binding.saltVideoPlayer.startPlayLogic()
                binding.tvTitle.text = title
            }
        } else {
            val url = data.toString()
            val title = url
            if (url != null && title != null) {
                binding.saltVideoPlayer.setUp(url, false, "")
                binding.saltVideoPlayer.startPlayLogic()
                binding.tvTitle.text = title
            }
        }



        initView()
    }

    private fun initView() {
        with(binding) {

            ivBack.setOnClickListener {
                onBackPressed()
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

            tvTitle.setOnClickListener {
                MessageDialog.show(getString(R.string.video), tvTitle.text, getString(R.string.confirm))
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
                    this@PlayerActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE)
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
                    saltVideoPlayer.seekTo(pro.toLong().coerceAtLeast(1L))
                }

            })
        }
    }

    private fun hideTitleAndBottomBar() {
        binding.blurViewTitleBar.visibility = View.INVISIBLE
        binding.blurViewBottomBar.visibility = View.INVISIBLE
        titleAndBottomBarVisibility = false
        shotPicHandler.removeMessages(HANDLER_MSG_SHOT_PIC)
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
        shotPicHandler.sendEmptyMessage(HANDLER_MSG_SHOT_PIC)
    }

    override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean, newConfig: Configuration) {
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
        private const val HANDLER_MSG_SHOT_PIC_DELAY = 100L

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