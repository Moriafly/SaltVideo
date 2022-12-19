package com.salt.video.core

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import com.salt.video.R
import com.shuyu.gsyvideoplayer.listener.VideoAllCallBack
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

    override fun getLayoutId(): Int {
        return R.layout.layout_salt_video_player
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
        this.setVideoAllCallBack(object : VideoAllCallBack {
            override fun onStartPrepared(url: String?, vararg objects: Any?) {
                
            }

            override fun onPrepared(url: String?, vararg objects: Any?) {
                
            }

            override fun onClickStartIcon(url: String?, vararg objects: Any?) {
                
            }

            override fun onClickStartError(url: String?, vararg objects: Any?) {
                
            }

            override fun onClickStop(url: String?, vararg objects: Any?) {
                
            }

            override fun onClickStopFullscreen(url: String?, vararg objects: Any?) {
                
            }

            override fun onClickResume(url: String?, vararg objects: Any?) {
                
            }

            override fun onClickResumeFullscreen(url: String?, vararg objects: Any?) {
                
            }

            override fun onClickSeekbar(url: String?, vararg objects: Any?) {
                
            }

            override fun onClickSeekbarFullscreen(url: String?, vararg objects: Any?) {
                
            }

            override fun onAutoComplete(url: String?, vararg objects: Any?) {
                
            }

            override fun onComplete(url: String?, vararg objects: Any?) {
                
            }

            override fun onEnterFullscreen(url: String?, vararg objects: Any?) {
                
            }

            override fun onQuitFullscreen(url: String?, vararg objects: Any?) {
                
            }

            override fun onQuitSmallWidget(url: String?, vararg objects: Any?) {
                
            }

            override fun onEnterSmallWidget(url: String?, vararg objects: Any?) {
                
            }

            override fun onTouchScreenSeekVolume(url: String?, vararg objects: Any?) {
                
            }

            override fun onTouchScreenSeekPosition(url: String?, vararg objects: Any?) {
                
            }

            override fun onTouchScreenSeekLight(url: String?, vararg objects: Any?) {
                
            }

            override fun onPlayError(url: String?, vararg objects: Any?) {
                
            }

            override fun onClickStartThumb(url: String?, vararg objects: Any?) {
                
            }

            override fun onClickBlank(url: String?, vararg objects: Any?) {
                
            }

            override fun onClickBlankFullscreen(url: String?, vararg objects: Any?) {
                
            }

        })
    }

}