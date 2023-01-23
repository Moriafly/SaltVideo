package com.salt.video

import android.app.Application
import com.kongzue.dialogx.DialogX
import com.kongzue.dialogx.style.MaterialStyle
import com.kongzue.dialogxmaterialyou.style.MaterialYouStyle
import com.salt.video.data.AppDatabase
import com.salt.video.ui.dialogx.XSMStyle

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        initDialogX()
        initAppDatabase()
    }

    /** 初始化 DialogX */
    private fun initDialogX() {
        DialogX.init(this)
        DialogX.globalStyle = MaterialStyle()
        DialogX.globalTheme = DialogX.THEME.AUTO
    }

    /** 初始化数据库 */
    private fun initAppDatabase() {
        appDatabase = AppDatabase.getDatabase(applicationContext)
    }

    companion object {

        lateinit var appDatabase: AppDatabase
            private set

    }

}