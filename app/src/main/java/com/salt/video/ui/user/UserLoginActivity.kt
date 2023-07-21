package com.salt.video.ui.user

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.moriafly.salt.ui.Item
import com.moriafly.salt.ui.ItemCheck
import com.moriafly.salt.ui.ItemContainer
import com.moriafly.salt.ui.ItemEdit
import com.moriafly.salt.ui.ItemSpacer
import com.moriafly.salt.ui.RoundedColumn
import com.moriafly.salt.ui.TextButton
import com.moriafly.salt.ui.UnstableSaltApi
import com.salt.video.R
import com.salt.video.ui.base.BasicActivityColumn
import com.salt.video.ui.theme.VideoTheme

class UserLoginActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VideoTheme {
                UserLoginUI()
            }
        }
    }

}

@OptIn(UnstableSaltApi::class)
@Composable
private fun UserLoginUI() {
    BasicActivityColumn(
        text = "登录"
    ) {
        RoundedColumn {
            ItemSpacer()
            ItemSpacer()
            Image(
                painter = painterResource(id = R.drawable.ic_launcher),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(48.dp)
                    .clip(CircleShape)
            )
            ItemSpacer()
            var username by remember { mutableStateOf("") }
            ItemEdit(
                text = username,
                onChange = {
                    username = it
                },
                hint = "用户名"
            )

            var password by remember { mutableStateOf("") }
            ItemEdit(
                text = password,
                onChange = {
                    password = it
                },
                hint = "密码"
            )

            ItemContainer {
                TextButton(
                    onClick = {

                    },
                    text = "登录"
                )
            }
        }

        RoundedColumn {
            Item(
                onClick = {

                },
                text = "忘记用户名？"
            )
            Item(
                onClick = {

                },
                text = "忘记密码？"
            )
        }

        RoundedColumn {
            ItemCheck(
                state = true,
                onChange = {

                },
                text = "我已阅读并同意隐私政策"
            )
        }

    }
}