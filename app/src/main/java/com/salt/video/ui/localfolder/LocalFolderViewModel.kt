package com.salt.video.ui.localfolder

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.provider.DocumentsContract
import android.util.Log
import androidx.core.database.getStringOrNull
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salt.video.data.entry.Video
import com.salt.video.data.ext.toVideo
import com.salt.video.util.DocumentFileUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LocalFolderViewModel: ViewModel() {

    private val _title = MutableStateFlow<String?>(null)
    val title = _title.asStateFlow()

    private val _files = MutableStateFlow<List<Any>?>(null)
    val files = _files.asStateFlow()

    fun load(context: Context, treeUriPath: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val treeUri = treeUriPath.toUri()

            val documentFile = DocumentFileUtil.getDocumentFileFormTreeUri(context, treeUri)
            if (documentFile != null) {
                _title.emit(documentFile.name)
            }

            val folders = ArrayList<LocalFolder>()
            val videos = ArrayList<Video>()

            val contentResolver: ContentResolver = context.contentResolver
            val cursor: Cursor? = try {
                contentResolver.query(
                    treeUri,
                    arrayOf(
                        DocumentsContract.Document.COLUMN_DOCUMENT_ID,
                        DocumentsContract.Document.COLUMN_DISPLAY_NAME,
                        DocumentsContract.Document.COLUMN_MIME_TYPE,
                        DocumentsContract.Document.COLUMN_SIZE,
                        DocumentsContract.Document.COLUMN_LAST_MODIFIED
                    ),
                    null,
                    null,
                    null
                )
            } catch (e: SecurityException) {
                // 丢失了对此 Uri 的权限
                null
            }
            when {
                cursor == null -> {
                    // TODO error cursor = null
                }
                !cursor.moveToFirst() -> {
                    // TODO error
                }
                else -> {
                    do {
                        val documentId = cursor.getString(0)
                        // displayName 可能为 null java.lang.NullPointerException: cursor.getString(1) must not be null
                        val displayName: String = cursor.getStringOrNull(1) ?: ""
                        val mimeType: String = cursor.getString(2)
                        val size = cursor.getLong(3)
                        val dateModified = cursor.getLong(4)
                        // 如果是文件夹且文件夹不是隐藏文件夹（即不以 . 开头）
                        if (isDirectory(mimeType) && !displayName.startsWith(".")) {
                            val directoryUri = DocumentsContract.buildChildDocumentsUriUsingTree(treeUri, documentId)
                            folders.add(
                                LocalFolder(displayName, directoryUri.toString())
                            )
                        } else {
                            videos.add(
                                Video(
                                    url = DocumentsContract.buildDocumentUriUsingTree(treeUri, documentId).toString(),
                                    title = displayName
                                )
                            )
                        }
                    } while (cursor.moveToNext())
                }
            }
            cursor?.close()

            _files.emit(folders + videos)
        }
    }

    /**
     * Util method to check if the mime type is a directory
     */
    private fun isDirectory(mimeType: String): Boolean {
        return mimeType == DocumentsContract.Document.MIME_TYPE_DIR
    }

}