package com.salt.video.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.dylanc.activityresult.launcher.OpenDocumentLauncher
import com.dylanc.activityresult.launcher.OpenDocumentTreeLauncher
import com.kongzue.dialogx.dialogs.InputDialog
import com.kongzue.dialogx.dialogs.PopMenu
import com.salt.video.R
import com.salt.video.databinding.ActivityMainBinding
import com.salt.video.ui.main.my.MyFragment
import com.salt.video.ui.main.video.VideoFragment
import com.salt.video.ui.player.PlayerActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    /** SAF 选择 */
    private val openDocumentTreeLauncher = OpenDocumentTreeLauncher(this)
    private val openDocumentLauncher = OpenDocumentLauncher(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.root.setOnApplyWindowInsetsListener { v, insets ->
            binding.clTitleBar.updateLayoutParams<ConstraintLayout.LayoutParams> {
                topMargin = insets.systemWindowInsetTop
            }
            binding.clBottomBar.updateLayoutParams<ConstraintLayout.LayoutParams> {
                bottomMargin = insets.systemWindowInsetBottom
            }
            return@setOnApplyWindowInsetsListener insets
        }

        binding.ivAdd.setOnClickListener {
            PopMenu.build()
                .setMenuList(
                    listOf(
                        "单个本地视频",
                        "单个本地音乐",
                        "单个网络音视频",
//                        "本地音视频文件夹",
//                        "WebDAV 服务器"
                    )
                )
                .setOnMenuItemClickListener { dialog, text, index ->
                    when (index) {
                        0 -> openDocumentLauncher("video/*")
                        1 -> openDocumentLauncher("audio/*")
                        2 -> openDialog()
                    }
                    return@setOnMenuItemClickListener false
                }
                .show()
        }

        initView()
    }

    private fun initView() {
        with(binding) {
            viewPager.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
                override fun getCount(): Int {
                    return 2
                }

                override fun getItem(position: Int): Fragment {
                    return when(position) {
                        0 -> VideoFragment()
                        else -> MyFragment()
                    }
                }
            }
            viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                }

                override fun onPageSelected(position: Int) {
                    smoothBottomBar.itemActiveIndex = position
                    tvTitle.text = when (position) {
                        0 -> getString(R.string.video)
                        else -> getString(R.string.my)
                    }
                }

                override fun onPageScrollStateChanged(state: Int) {

                }
            })

            smoothBottomBar.setOnItemSelectedListener {
                viewPager.setCurrentItem(it, true)
            }
        }
    }

    private fun openDocumentLauncher(vararg input: String) {
        openDocumentLauncher.launch(*input) { uri ->
            if (uri != null) {
                val documentFile = DocumentFile.fromSingleUri(this, uri)
                PlayerActivity.start(this, uri.toString(), documentFile?.name ?: "")
            }
        }
    }

    private fun openDialog() {
        InputDialog(
            "网络视频",
            "输入网络地址",
            "确定",
            "取消",
            ""
        )
            .setOkButton { dialog, v, inputStr ->
                val url = inputStr
                PlayerActivity.start(this, url, url)
                return@setOkButton false
            }
            .show()
    }
}