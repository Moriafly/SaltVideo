package com.salt.video.ui.main.video

import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salt.video.App
import com.salt.video.data.entry.MediaSource
import com.salt.video.data.fui.MediaSourceFui
import com.salt.video.data.fui.toFuiList
import com.salt.video.data.repo.MediaSourceRepo
import com.salt.video.util.pinyinString
import com.salt.video.util.sort.SimpleNaturalComparator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _mediaSourceFuiList = MutableStateFlow<List<MediaSourceFui>?>(null)
    val mediaSourceFuiList = _mediaSourceFuiList.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            MediaSourceRepo.getAll()
                .collect { mediaSources ->
                    val fuiList = mediaSources.toFuiList(App.context)
                    // 排序
                    val sortedFuiList = fuiList.sortedWith { o1, o2 -> SimpleNaturalComparator.getInstance<String>().compare(o1.title.pinyinString, o2.title.pinyinString) }
                    _mediaSourceFuiList.emit(sortedFuiList)
                }
        }
    }

}