package com.salt.video.data.ext

import android.provider.DocumentsContract
import androidx.documentfile.provider.DocumentFile
import com.salt.video.data.entry.Video

fun DocumentFile.toVideo(): Video {
    return Video(
        url = uri.toString(),
        title = name ?: ""
    )
}

