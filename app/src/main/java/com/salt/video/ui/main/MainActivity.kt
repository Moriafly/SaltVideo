package com.salt.video.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.dylanc.activityresult.launcher.OpenDocumentLauncher
import com.dylanc.activityresult.launcher.OpenDocumentTreeLauncher
import com.kongzue.dialogx.dialogs.InputDialog
import com.kongzue.dialogx.dialogs.MessageDialog
import com.kongzue.dialogx.dialogs.PopMenu
import com.moriafly.salt.ui.BottomBar
import com.moriafly.salt.ui.BottomBarItem
import com.moriafly.salt.ui.SaltTheme
import com.moriafly.salt.ui.TitleBar
import com.moriafly.salt.ui.UnstableSaltApi
import com.salt.video.R
import com.salt.video.databinding.ActivityMainBinding
import com.salt.video.ui.main.my.MyFragment
import com.salt.video.ui.main.my.MyScreen
import com.salt.video.ui.main.video.VideoFragment
import com.salt.video.ui.main.video.VideoScreen
import com.salt.video.ui.main.video.VideoViewModel
import com.salt.video.ui.player.PlayerActivity
import com.salt.video.ui.theme.VideoTheme
import kotlinx.coroutines.launch
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    // private lateinit var binding: ActivityMainBinding

    private val videoViewModel: VideoViewModel by viewModels()

    /** SAF 选择 */
    private val openDocumentTreeLauncher = OpenDocumentTreeLauncher(this)
    private val openDocumentLauncher = OpenDocumentLauncher(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VideoTheme {
                MainUI(videoViewModel = videoViewModel)
            }
        }
//        binding = ActivityMainBinding.inflate(layoutInflater)
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
//        initView()
    }

    private fun initView() {
//        with(binding) {
//            viewPager.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
//                override fun getCount(): Int {
//                    return 2
//                }
//
//                override fun getItem(position: Int): Fragment {
//                    return when (position) {
//                        0 -> VideoFragment()
//                        else -> MyFragment()
//                    }
//                }
//            }
//            viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
//                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//
//                }
//
//                override fun onPageSelected(position: Int) {
//                    smoothBottomBar.itemActiveIndex = position
//                    tvTitle.text = when (position) {
//                        0 -> getString(R.string.video)
//                        else -> getString(R.string.my)
//                    }
//                }
//
//                override fun onPageScrollStateChanged(state: Int) {
//
//                }
//            })
//
//            smoothBottomBar.setOnItemSelectedListener {
//                viewPager.setCurrentItem(it, true)
//            }
//        }
    }

    fun openDocumentTreeLauncher() {
        try {
            openDocumentTreeLauncher.launch { uri ->
                if (uri != null) {
                    // 请求永久 URI 授权
                    contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                    // val documentFile = DocumentFile.fromTreeUri(this, uri)
                    videoViewModel.addLocalFolder(uri)
                }
            }
        } catch (e: Exception) {
            MessageDialog.show(getString(R.string.error), "无法调用系统文件选择", getString(R.string.confirm))
        }
    }

    fun openDocumentLauncher(vararg input: String) {
        try {
            openDocumentLauncher.launch(*input) { uri ->
                if (uri != null) {
                    val documentFile = DocumentFile.fromSingleUri(this, uri)
                    PlayerActivity.start(this, uri.toString(), documentFile?.name ?: "")
                }
            }
        } catch (e: Exception) {
            MessageDialog.show(getString(R.string.error), "无法调用系统文件选择", "确定")
        }
    }

    fun openDialog() {
        InputDialog(
            "网络视频",
            "输入网络地址",
            getString(R.string.confirm),
            getString(R.string.cancel),
            ""
        )
            .setOkButton { dialog, v, inputStr ->
                val url = inputStr
                PlayerActivity.start(this, url, url)
                return@setOkButton false
            }
            .show()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }
}

@OptIn(UnstableSaltApi::class, ExperimentalFoundationApi::class)
@Composable
fun MainUI(
    videoViewModel: VideoViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        val pagerState = rememberPagerState {
            2
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .background(SaltTheme.colors.background),
            beyondBoundsPageCount = 1,
            userScrollEnabled = false
        ) {page ->
            when (page) {
                0 -> VideoScreen(videoViewModel = videoViewModel)
                1 -> MyScreen()
            }
        }
        val scope = rememberCoroutineScope()
        BottomBar {
            BottomBarItem(
                state = pagerState.currentPage == 0,
                onClick = {
                    scope.launch {
                        pagerState.scrollToPage(0)
                    }
                },
                painter = painterResource(id = R.drawable.ic_main_bottom_video),
                text = stringResource(id = R.string.video)
            )
            BottomBarItem(
                state = pagerState.currentPage == 1,
                onClick = {
                    scope.launch {
                        pagerState.scrollToPage(1)
                    }
                },
                painter = painterResource(id = R.drawable.ic_kayaking),
                text = stringResource(id = R.string.my)
            )
        }
    }
}