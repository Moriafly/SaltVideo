package com.salt.video.ui.main.video

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salt.video.data.entry.MediaSource
import com.salt.video.data.repo.MediaSourceRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class VideoViewModel: ViewModel() {

    fun addLocalFolder(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            val mediaSource = MediaSource(
                url = uri.toString()
            )
            MediaSourceRepo.insert(mediaSource)
        }
    }

    fun getAllMediaSource(): Flow<List<MediaSource>> = MediaSourceRepo.getAll()

}