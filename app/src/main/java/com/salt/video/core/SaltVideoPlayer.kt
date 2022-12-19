package com.salt.video.core

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import com.salt.video.R
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer

/**
 * SAR表示Sample Aspect Ratio，含义是采样宽高比。
 * DAR（Display Aspect Ratio）表示显示宽高比，也就是我们经常说的16:9，4:3。
 */
class SaltVideoPlayer: StandardGSYVideoPlayer {
    constructor(context: Context?, fullFlag: Boolean?) : super(context, fullFlag)
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    var onVideoSizeChangeListener: (width: Int, height: Int) -> Unit = { _, _ -> }

    var onClickUiToggle: () -> Unit = {}

    var onPlayerStateChange: (PlayerState) -> Unit = {}

    var onSetProgressAndTime: (progress: Long, secProgress: Long, currentTime: Long, totalTime: Long) -> Unit = {
            progress, secProgress, currentTime, totalTime ->
    }

    override fun getLayoutId(): Int {
        return R.layout.layout_salt_video_player
    }

    override fun touchDoubleUp(e: MotionEvent?) {
        super.touchDoubleUp(e)
    }

    override fun setProgressAndTime(progress: Long, secProgress: Long, currentTime: Long, totalTime: Long, forceChange: Boolean) {
        super.setProgressAndTime(progress, secProgress, currentTime, totalTime, forceChange)
        // onSetProgressAndTime(progress, secProgress, currentTime, totalTime, forceChange)
    }

    override fun resolveUIState(state: Int) {
        super.resolveUIState(state)
        when(state) {
            CURRENT_STATE_PLAYING -> onPlayerStateChange(PlayerState.RESUME)
            CURRENT_STATE_PAUSE -> onPlayerStateChange(PlayerState.PAUSE)
            CURRENT_STATE_AUTO_COMPLETE -> onVideoReset()
        }
    }

    override fun onPrepared() {
        super.onPrepared()
        onVideoSizeChangeListener.invoke(currentVideoWidth, currentVideoHeight)
    }

    override fun onClickUiToggle(e: MotionEvent?) {
        super.onClickUiToggle(e)
        onClickUiToggle()
    }

//    override fun getProgressDialogLayoutId(): Int {
//        return super.getProgressDialogLayoutId()
//    }


    init {
        setGSYVideoProgressListener { progress, secProgress, currentPosition, duration ->
            Log.d(TAG, "p: $progress, sp: $secProgress, c: $currentPosition, d: $duration")
            onSetProgressAndTime(progress, secProgress, currentPosition, duration)
        }
        isLooping = true
        isReleaseWhenLossAudio = false
    }

    companion object {
        private const val TAG = "SaltVideoPlayer"
    }

}