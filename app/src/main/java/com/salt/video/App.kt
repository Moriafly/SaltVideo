package com.salt.video

import android.app.Application
import com.kongzue.dialogx.DialogX
import com.kongzue.dialogx.style.IOSStyle
import com.kongzue.dialogx.style.MIUIStyle
import com.kongzue.dialogx.style.MaterialStyle
import com.kongzue.dialogxmaterialyou.style.MaterialYouStyle

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        // 初始化 DialogX
        DialogX.init(this)
        DialogX.globalStyle = IOSStyle()
        DialogX.globalTheme = DialogX.THEME.DARK
    }

}