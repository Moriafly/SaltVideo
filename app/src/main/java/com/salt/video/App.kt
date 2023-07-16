@file:Suppress("UNUSED")

package com.salt.video

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.annotation.Keep
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.VideoFrameDecoder
import com.kongzue.dialogx.DialogX
import com.kongzue.dialogx.style.IOSStyle
import com.salt.video.data.AppDatabase
import com.tencent.mmkv.MMKV

class App: Application(), ImageLoaderFactory {

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        initMMKV()
        initDialogX()
        initAppDatabase()
    }

    private fun initMMKV() {
        MMKV.initialize(context)
        mmkv = MMKV.defaultMMKV()!!
    }

    /** 初始化 DialogX */
    private fun initDialogX() {
        DialogX.init(this)
        DialogX.globalStyle = IOSStyle()
        DialogX.globalTheme = DialogX.THEME.AUTO
    }

    /** 初始化数据库 */
    private fun initAppDatabase() {
        appDatabase = AppDatabase.getDatabase(applicationContext)
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .components {
                add(VideoFrameDecoder.Factory())
            }
            .build()
    }

    /**
     * 屏蔽魅族 Flyme 反色
     */
    @Keep
    fun mzNightModeUseOf(): Int = 2

    companion object {

        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
            private set

        lateinit var mmkv: MMKV
            private set

        lateinit var appDatabase: AppDatabase
            private set

    }

}