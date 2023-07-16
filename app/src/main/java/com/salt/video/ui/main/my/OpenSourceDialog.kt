package com.salt.video.ui.main.my

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.moriafly.salt.ui.ItemSpacer
import com.moriafly.salt.ui.ItemText
import com.moriafly.salt.ui.RoundedColumn
import com.moriafly.salt.ui.UnstableSaltApi
import com.moriafly.salt.ui.dialog.BottomSheetDialog
import com.salt.video.ui.main.video.DialogTitle

@OptIn(UnstableSaltApi::class)
@Composable
fun OpenSourceDialog(
    onDismissRequest: () -> Unit
) {
    BottomSheetDialog(
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            ItemSpacer()
            DialogTitle(title = "开放源代码许可")
            RoundedColumn {
                ItemSpacer()
                ItemText(
                    text = """
                        material
                        GSYVideoPlayer
                        ActivityResultLauncher
                        utilcodex
                        BlurView
                        DialogX
                        DsoKotlinExtensions
                        SmoothBottomBar
                        BRV
                        androidx
                        coil
                        landscapist
                        glide
                        SaltUI
                        mmkv
                        C2Pinyin
                    """.trimIndent()
                )
                ItemSpacer()
            }
            Spacer(modifier = Modifier.navigationBarsPadding())
        }

    }
}