package com.salt.video.util

import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract
import androidx.documentfile.provider.DocumentFile

/**
 * SAF Document
 */
object DocumentFileUtil {

    /**
     * 从 [treeUri] 获取 [DocumentFile]，[treeUri] 可以是父级 [treeUri] 的 childUri，tree 是相对的
     */
    fun getDocumentFileFormTreeUri(context: Context, treeUri: Uri): DocumentFile? {
        val documentUri = DocumentsContract.buildDocumentUriUsingTree(treeUri, DocumentsContract.getDocumentId(treeUri))
        return DocumentFile.fromSingleUri(context, documentUri)
    }

}