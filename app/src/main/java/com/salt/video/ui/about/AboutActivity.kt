package com.salt.video.ui.about

import android.app.Activity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.moriafly.salt.ui.Item
import com.moriafly.salt.ui.ItemSpacer
import com.moriafly.salt.ui.ItemTitle
import com.moriafly.salt.ui.RoundedColumn
import com.moriafly.salt.ui.SaltTheme
import com.moriafly.salt.ui.SaltUILogo
import com.moriafly.salt.ui.TitleBar
import com.moriafly.salt.ui.UnstableSaltApi
import com.salt.video.BuildConfig
import com.salt.video.R
import com.salt.video.ui.main.my.OpenSourceDialog
import com.salt.video.ui.theme.VideoTheme

class AboutActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VideoTheme {
                AboutUI()
            }
        }
    }

}

@OptIn(UnstableSaltApi::class)
@Composable
private fun AboutUI() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        val activity = LocalContext.current as Activity
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            TitleBar(
                onBack = {
                    activity.finish()
                },
                text = "关于",
                showBackBtn = true
            )
//            Icon(
//                modifier = Modifier
//                    .align(Alignment.CenterEnd)
//                    .size(56.dp)
//                    .fadeClickable {
//                        ActivityUtils.finishToActivity(MainActivity::class.java, false, true)
//                    }
//                    .padding(16.dp),
//                painter = painterResource(id = R.drawable.ic_home),
//                contentDescription = null,
//                tint = SaltTheme.colors.highlight
//            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(SaltTheme.colors.background)
        ) {
            RoundedColumn {
                ItemTitle(text = "法律信息")
//            Item(
//                onClick = {
//
//                },
//                text = "软件使用条例",
//                iconPainter = painterResource(id = R.drawable.ic_license),
//                iconColor = SaltTheme.colors.highlight
//            )
//
//            Item(
//                onClick = {
//
//                },
//                text = "隐私协议",
//                iconPainter = painterResource(id = R.drawable.ic_license),
//                iconColor = SaltTheme.colors.highlight
//            )

                var openSourceDialog by remember { mutableStateOf(false) }
                Item(
                    onClick = {
                        openSourceDialog = true
                    },
                    text = "开放源代码许可",
                    iconPainter = painterResource(id = R.drawable.ic_copyright),
                    iconColor = SaltTheme.colors.highlight
                )
                if (openSourceDialog) {
                    OpenSourceDialog(
                        onDismissRequest = {
                            openSourceDialog = false
                        }
                    )
                }
            }

            SaltUILogo()

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ItemSpacer()
                Icon(
                    painter = painterResource(id = R.drawable.ic_salt_video),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(color = SaltTheme.colors.highlight)
                        .padding(10.dp),
                    tint = Color.White
                )
                ItemSpacer()
                Text(
                    text = "${BuildConfig.VERSION_NAME}(${BuildConfig.VERSION_CODE})",
                    style = SaltTheme.textStyles.sub
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Copyright © 2022-2023 Moriafly. All Rights Reserved.",
                    style = SaltTheme.textStyles.sub
                )
                ItemSpacer()
            }
        }
    }
}