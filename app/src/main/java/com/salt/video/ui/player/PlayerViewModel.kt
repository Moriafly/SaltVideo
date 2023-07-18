package com.salt.video.ui.player

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class PlayerViewModel : ViewModel() {

    /** 用户手动调整了屏幕旋转 */
    var userScreenRotation = false

    var title by mutableStateOf<String?>(null)

    fun postTitle(value: String) {
        title = value
    }

}