package com.salt.video.core

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import com.salt.video.App
import com.salt.video.R
import com.salt.video.util.Config
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer

/**
 * SAR 表示 Sample Aspect Ratio，含义是采样宽高比。
 * DAR（Display Aspect Ratio）表示显示宽高比，也就是我们经常说的 16:9，4:3。
 */
class SaltVideoPlayer: StandardGSYVideoPlayer {
    constructor(context: Context?, fullFlag: Boolean?) : super(context, fullFlag)
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    /**
     * @param
     */
    var onVideoSizeChangeListener: (width: Int, height: Int, numerator: Int, denominator: Int) -> Unit = { _, _, _, _ -> }

    var onClickUiToggle: () -> Unit = {}

    var onPlayerStateChange: (PlayerState) -> Unit = {}

    var onSetProgressAndTime: (currentTime: Long, totalTime: Long) -> Unit = { _, _ -> }

    var onLongTouchUp: () -> Unit = {}

    var onPrepared: () -> Unit = {}

    /** 是否正在使用手势拖动 */
    private var isDragSeeking = false

    override fun onPrepared() {
        super.onPrepared()
        onPrepared.invoke()
    }

    override fun startAfterPrepared() {
        super.startAfterPrepared()
    }

    override fun getLayoutId(): Int {
        return R.layout.layout_salt_video_player
    }

    override fun touchDoubleUp(e: MotionEvent?) {
        super.touchDoubleUp(e)
    }

    override fun showProgressDialog(deltaX: Float, seekTime: String?, seekTimePosition: Long, totalTime: String?, totalTimeDuration: Long) {
        super.showProgressDialog(deltaX, seekTime, seekTimePosition, totalTime, totalTimeDuration)
        isDragSeeking = true
        onSetProgressAndTime(seekTimePosition, totalTimeDuration)
        Log.d(TAG, "showProgressDialog seekTime = $seekTime, seekTimePosition = $seekTimePosition, totalTime = $totalTime, totalTimeDuration = $totalTimeDuration")
    }

    override fun dismissProgressDialog() {
        super.dismissProgressDialog()
        isDragSeeking = false
    }

    override fun resolveUIState(state: Int) {
        super.resolveUIState(state)
        when(state) {
            CURRENT_STATE_PLAYING -> onPlayerStateChange(PlayerState.RESUME)
            CURRENT_STATE_PAUSE -> onPlayerStateChange(PlayerState.PAUSE)
            CURRENT_STATE_AUTO_COMPLETE -> onVideoReset()
        }
    }

    override fun onVideoSizeChanged() {
        super.onVideoSizeChanged()
        onVideoSizeChangeListener.invoke(currentVideoWidth, currentVideoHeight, videoSarNum, videoSarDen)
    }

    override fun onClickUiToggle(e: MotionEvent?) {
        super.onClickUiToggle(e)
        onClickUiToggle()
    }

    override fun init(context: Context?) {
        super.init(context)
        gestureDetector = GestureDetector(getContext().applicationContext, object : SimpleOnGestureListener() {
            override fun onDoubleTap(e: MotionEvent): Boolean {
                touchDoubleUp(e)
                return super.onDoubleTap(e)
            }

            override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                if (!mChangePosition && !mChangeVolume && !mBrightness) {
                    onClickUiToggle(e)
                }
                return super.onSingleTapConfirmed(e)
            }

            override fun onLongPress(e: MotionEvent) {
                super.onLongPress(e)
                touchLongPress(e)
            }
        })
    }

    /** 是否进行了长按倍数播放 */
    private var isLongPressSpeed = false

    override fun touchLongPress(e: MotionEvent?) {
        super.touchLongPress(e)
        isLongPressSpeed = true
        speed = 2f
        Log.d(TAG, "touchLongPress(e)")
    }

    override fun touchSurfaceUp() {
        super.touchSurfaceUp()
        if (isLongPressSpeed) {
            isLongPressSpeed = false
            onLongTouchUp()
        }
        Log.d(TAG, "touchSurfaceUp()")
    }

//    override fun getProgressDialogLayoutId(): Int {
//        return super.getProgressDialogLayoutId()
//    }


    init {
        setGSYVideoProgressListener { progress, secProgress, currentPosition, duration ->
            Log.d(TAG, "p: $progress, sp: $secProgress, c: $currentPosition, d: $duration")
            if (!isDragSeeking) {
                onSetProgressAndTime(currentPosition, duration)
            }
        }
        isLooping = true
        isReleaseWhenLossAudio = false
    }

    companion object {
        private const val TAG = "SaltVideoPlayer"
    }

}