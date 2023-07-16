package com.salt.video

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.VideoFrameDecoder
import com.kongzue.dialogx.DialogX
import com.kongzue.dialogx.style.IOSStyle
import com.kongzue.dialogx.style.MaterialStyle
import com.kongzue.dialogxmaterialyou.style.MaterialYouStyle
import com.salt.video.data.AppDatabase
import com.salt.video.ui.dialogx.XSMStyle

class App: Application(), ImageLoaderFactory {

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        initDialogX()
        initAppDatabase()
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


    companion object {

        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
            private set

        lateinit var appDatabase: AppDatabase
            private set

    }

}