package com.salt.video.ui.main.my

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.moriafly.salt.ui.Item
import com.moriafly.salt.ui.ItemSpacer
import com.moriafly.salt.ui.ItemText
import com.moriafly.salt.ui.ItemTitle
import com.moriafly.salt.ui.RoundedColumn
import com.moriafly.salt.ui.SaltTheme
import com.moriafly.salt.ui.TitleBar
import com.moriafly.salt.ui.UnstableSaltApi
import com.moriafly.salt.ui.noRippleClickable
import com.salt.video.R
import com.salt.video.ui.about.AboutActivity
import com.salt.video.ui.settings.SettingsActivity
import com.salt.video.ui.user.UserLoginActivity

@OptIn(UnstableSaltApi::class)
@Composable
fun MyScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        TitleBar(
            onBack = { },
            text = stringResource(id = R.string.my),
            showBackBtn = false
        )
        MyScreenContent()
    }
}

@OptIn(UnstableSaltApi::class)
@Composable
fun ColumnScope.MyScreenContent() {
    Column(
        modifier = Modifier
            .weight(1f)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        UserItem()

        val activity = LocalContext.current as Activity
        RoundedColumn {
            Item(
                onClick = {
                    activity.startActivity(Intent(activity, SettingsActivity::class.java))
                },
                iconPainter = painterResource(id = R.drawable.ic_settings),
                iconColor = SaltTheme.colors.highlight,
                text = "设置"
            )
            Item(
                onClick = {
                    activity.startActivity(Intent(activity, AboutActivity::class.java))
                },
                iconPainter = painterResource(id = R.drawable.ic_about),
                iconColor = SaltTheme.colors.highlight,
                text = "关于"
            )
        }

        RoundedColumn {
            ItemTitle(text = "椒盐视频开发体验版本")
            ItemSpacer()
            ItemText(text = "此为开发体验版本，所有功能都可能在后续版本变更或移除，加入 QQ 群聊 639298754")
            ItemSpacer()
        }
    }
}

@Composable
private fun UserItem() {
    val activity = LocalContext.current as Activity
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .noRippleClickable {
                activity.startActivity(Intent(activity, UserLoginActivity::class.java))
            }
            .padding(16.dp, 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_avatar_test),
            contentDescription = null,
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Text(
                text = "不要糖醋放椒盐",
                style = SaltTheme.textStyles.main
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Love",
                style = SaltTheme.textStyles.sub
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            painter = painterResource(id = com.moriafly.salt.ui.R.drawable.ic_chevron_right),
            contentDescription = null,
            modifier = Modifier
                .size(20.dp),
            tint = SaltTheme.colors.subText.copy(alpha = 0.5f)
        )
    }
}