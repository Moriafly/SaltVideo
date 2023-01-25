package com.salt.video.ui.localfolder

import android.net.Uri
import android.provider.DocumentsContract
import android.util.Log
import androidx.documentfile.provider.DocumentFile

data class LocalFolder(
    val name: String,
    val url: String
)

fun DocumentFile.toLocalFolder(treeUri: Uri): LocalFolder {
    val documentId = DocumentsContract.getDocumentId(uri)
    val url = DocumentsContract.buildChildDocumentsUriUsingTree(treeUri, documentId).toString()
    Log.d("DocumentFile.toLocalFolder", "documentId = $documentId, url = $url")
    return LocalFolder(
        name = name ?: "",
        url = url
    )
}
