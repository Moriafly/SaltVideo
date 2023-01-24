package com.salt.video.ui.main

import android.os.Bundle
import androidx.activity.viewModels
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
import com.kongzue.dialogx.dialogs.MessageDialog
import com.kongzue.dialogx.dialogs.PopMenu
import com.salt.video.R
import com.salt.video.databinding.ActivityMainBinding
import com.salt.video.ui.main.my.MyFragment
import com.salt.video.ui.main.video.VideoFragment
import com.salt.video.ui.main.video.VideoViewModel
import com.salt.video.ui.player.PlayerActivity
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val videoViewModel: VideoViewModel by viewModels()

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

        initView()
    }

    private fun initView() {
        with(binding) {
            viewPager.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
                override fun getCount(): Int {
                    return 2
                }

                override fun getItem(position: Int): Fragment {
                    return when (position) {
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

    fun openDocumentTreeLauncher() {
        try {
            openDocumentTreeLauncher.launch { uri ->
                if (uri != null) {
                    val documentFile = DocumentFile.fromTreeUri(this, uri)
                    MessageDialog.show(getString(R.string.error), "功能开发中，敬请期待", getString(R.string.confirm))
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