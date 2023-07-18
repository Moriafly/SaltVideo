package com.salt.video.ui.localfolder

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.blankj.utilcode.util.ActivityUtils
import com.moriafly.salt.ui.BottomBar
import com.moriafly.salt.ui.Item
import com.moriafly.salt.ui.SaltTheme
import com.moriafly.salt.ui.TitleBar
import com.moriafly.salt.ui.UnstableSaltApi
import com.moriafly.salt.ui.fadeClickable
import com.salt.video.R
import com.salt.video.data.entry.Video
import com.salt.video.ui.main.MainActivity
import com.salt.video.ui.player.PlayerActivity
import com.salt.video.ui.theme.VideoTheme
import com.skydoves.landscapist.glide.GlideImage
import java.text.SimpleDateFormat
import java.util.Date

class LocalFolderActivity : AppCompatActivity() {

    // private lateinit var binding: ActivityLocalFolderBinding

    private val localFolderViewModel: LocalFolderViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VideoTheme {
                LocalFolderUI(localFolderViewModel = localFolderViewModel)
            }
        }
//        binding = ActivityLocalFolderBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        binding.root.setOnApplyWindowInsetsListener { v, insets ->
//            binding.clTitleBar.updateLayoutParams<ConstraintLayout.LayoutParams> {
//                topMargin = insets.systemWindowInsetTop
//            }
//            binding.clBottomBar.updateLayoutParams<ConstraintLayout.LayoutParams> {
//                bottomMargin = insets.systemWindowInsetBottom
//            }
//            return@setOnApplyWindowInsetsListener insets
//        }
//
        val treeUriPath = intent.getStringExtra(EXTRA_TREE_URI_PATH)
        if (treeUriPath != null) {
            localFolderViewModel.load(this, treeUriPath)
        }
//
//        initView()
    }

    private fun initView() {
//        with (binding) {
//            ivBack.setOnClickListener {
//                finish()
//            }
//            ivHome.setOnClickListener {
//                ActivityUtils.finishToActivity(MainActivity::class.java, false, true)
//            }
//            rvLocalFolder.linear().setup {
//                addType<LocalFolder>(R.layout.rv_media_source)
//                addType<Video>(R.layout.rv_video)
//                onBind {
//                    when (itemViewType) {
//                        R.layout.rv_media_source -> {
//                            val localFolder = getModel<LocalFolder>()
//                            val clBase = findView<ConstraintLayout>(R.id.clBase)
//                            val ivIcon = findView<ImageView>(R.id.ivIcon)
//                            val tvTitle = findView<TextView>(R.id.tvTitle)
//                            ivIcon.setImageResource(R.drawable.ic_folder)
//                            tvTitle.text = localFolder.name
//                            clBase.setOnClickListener {
//                                start(this@LocalFolderActivity, localFolder.url)
//                            }
//                        }
//                        R.layout.rv_video -> {
//                            val video = getModel<Video>()
//                            val clBase = findView<ConstraintLayout>(R.id.clBase)
//                            val ivCover = findView<ImageView>(R.id.ivCover)
//                            val tvTitle = findView<TextView>(R.id.tvTitle)
//                            ivCover.load(video.url) {
//
//                            }
//                            tvTitle.text = video.title
//                            clBase.setOnClickListener {
//                                PlayerActivity.start(this@LocalFolderActivity, video.url, video.title)
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        lifecycleScope.launchWhenCreated {
//            localFolderViewModel.title
//                .filter { it != null }
//                .collect {
//                    binding.tvTitle.text = it
//                }
//        }
//        lifecycleScope.launchWhenCreated {
//            localFolderViewModel.files
//                .filter { it != null }
//                .collect {
//                    binding.rvLocalFolder.bindingAdapter.models = it
//                }
//        }
    }

    companion object {

        fun start(activity: Activity, treeUriPath: String) {
            val intent = Intent(activity, LocalFolderActivity::class.java)
            intent.putExtra(EXTRA_TREE_URI_PATH, treeUriPath)
            activity.startActivity(intent)
        }

        private const val EXTRA_TREE_URI_PATH = "extra_tree_uri_path"

    }

}

@OptIn(UnstableSaltApi::class)
@Composable
private fun LocalFolderUI(
    localFolderViewModel: LocalFolderViewModel
) {
    val title by localFolderViewModel.title.collectAsState()
    val files by localFolderViewModel.files.collectAsState()
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
                text = title ?: "",
                showBackBtn = true
            )
            Icon(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(56.dp)
                    .fadeClickable {
                        ActivityUtils.finishToActivity(MainActivity::class.java, false, true)
                    }
                    .padding(16.dp),
                painter = painterResource(id = R.drawable.ic_home),
                contentDescription = null,
                tint = SaltTheme.colors.highlight
            )
        }
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .background(SaltTheme.colors.background)
        ) {
            files?.let { files ->
                items(files) { file ->
                    FileItem(file = file)
                }
            }
        }
        val scope = rememberCoroutineScope()
        BottomBar {
//            BottomBarItem(
//                state = pagerState.currentPage == 0,
//                onClick = {
//                    scope.launch {
//                        pagerState.animateScrollToPage(0)
//                    }
//                },
//                painter = painterResource(id = R.drawable.ic_main_bottom_video),
//                text = stringResource(id = R.string.video)
//            )
//            BottomBarItem(
//                state = pagerState.currentPage == 1,
//                onClick = {
//                    scope.launch {
//                        pagerState.animateScrollToPage(1)
//                    }
//                },
//                painter = painterResource(id = R.drawable.ic_kayaking),
//                text = stringResource(id = R.string.my)
//            )
        }
    }
}

@Composable
private fun FileItem(file: Any) {
    val activity = LocalContext.current as Activity
    when (file) {
        is LocalFolder -> {
            Item(
                onClick = {
                    LocalFolderActivity.start(activity, file.url)
                },
                iconPainter = painterResource(id = R.drawable.ic_folder),
                iconColor = SaltTheme.colors.highlight,
                text = file.name,
                sub = file.dateModified.toDateTimeFormat()
            )
        }

        is Video -> {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        PlayerActivity.start(activity, file.url, file.title)
                    }
                    .padding(16.dp, 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                GlideImage(
                    imageModel = { file.url },
                    modifier = Modifier
                        .size(135.dp, 90.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = file.title,
                        style = SaltTheme.textStyles.main
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = file.dateModified.toDateTimeFormat(),
                        style = SaltTheme.textStyles.sub
                    )
                }
            }
        }
    }
}

/**
 * 转换成日期时间格式
 */
@SuppressLint("SimpleDateFormat")
fun Long.toDateTimeFormat(): String {
    val date = Date().apply {
        time = this@toDateTimeFormat
    }
    return SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(date)
}