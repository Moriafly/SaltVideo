package com.salt.video.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class ProtectView: View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        return true
    }
}