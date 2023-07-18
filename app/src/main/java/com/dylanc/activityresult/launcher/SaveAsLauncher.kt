package com.dylanc.activityresult.launcher

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.contract.ActivityResultContract

/**
 * @param type mimeType, text/plain etc.
 */
class SaveAsRequest(
    val fileName: String,
    val type: String,
)

class SaveAsResultContract : ActivityResultContract<SaveAsRequest, Uri?>() {
    override fun createIntent(context: Context, input: SaveAsRequest): Intent {
        return Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = input.type
            putExtra(Intent.EXTRA_TITLE, input.fileName)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        return if (resultCode == Activity.RESULT_OK && intent != null) {
            intent.data
        } else {
            null
        }
    }
}

class SaveAsLauncher(caller: ActivityResultCaller) :
    BaseActivityResultLauncher<SaveAsRequest, Uri?>(caller, SaveAsResultContract())