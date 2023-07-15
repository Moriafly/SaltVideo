package com.salt.video.ui.player

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class PlayerViewModel : ViewModel() {

    var title by mutableStateOf<String?>(null)

    fun postTitle(value: String) {
        title = value
    }

}