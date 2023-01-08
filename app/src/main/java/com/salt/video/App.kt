package com.salt.video

import android.app.Application
import com.kongzue.dialogx.DialogX
import com.salt.video.ui.dialogx.XSMStyle

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        //
        initDialogX()
    }

    /** 初始化 DialogX */
    private fun initDialogX() {
        DialogX.init(this)
        DialogX.globalStyle = XSMStyle()
        DialogX.globalTheme = DialogX.THEME.DARK
    }

}