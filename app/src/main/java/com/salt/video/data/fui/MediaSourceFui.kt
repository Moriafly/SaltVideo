package com.salt.video.data.fui

import android.content.Context
import android.provider.DocumentsContract
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import com.salt.video.data.entry.MediaSource

data class  MediaSourceFui(
    val title: String,
    val directoryUri: String,
    val mediaSource: MediaSource
)

fun MediaSource.toFui(
    context: Context
): MediaSourceFui {
    val treeUri = url.toUri()
    val documentFile = DocumentFile.fromTreeUri(context, treeUri)
    var directoryUri = ""
    if (documentFile != null) {
        // mediaSource 是 treeUri
        // 调用路径页面需要传入普通的 uri （非 treeUri）
        val documentId = DocumentsContract.getDocumentId(documentFile.uri)
        directoryUri = DocumentsContract.buildChildDocumentsUriUsingTree(treeUri, documentId).toString()
    }

    return MediaSourceFui(
        title = documentFile?.name ?: "",
        directoryUri = directoryUri,
        mediaSource = this
    )
}

fun List<MediaSource>.toFuiList(
    context: Context
): List<MediaSourceFui> {
    val list = ArrayList<MediaSourceFui>()
    this.forEach {
        list.add(it.toFui(context))
    }
    return list
}