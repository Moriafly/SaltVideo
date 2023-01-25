package com.salt.video.ui.localfolder

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.lifecycleScope
import coil.load
import com.blankj.utilcode.util.ActivityUtils
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.salt.video.R
import com.salt.video.data.entry.Video
import com.salt.video.databinding.ActivityLocalFolderBinding
import com.salt.video.ui.main.MainActivity
import com.salt.video.ui.player.PlayerActivity
import kotlinx.coroutines.flow.filter

class LocalFolderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLocalFolderBinding

    private val localFolderViewModel: LocalFolderViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocalFolderBinding.inflate(layoutInflater)
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

        val treeUriPath = intent.getStringExtra(EXTRA_TREE_URI_PATH)
        if (treeUriPath != null) {
            localFolderViewModel.load(this, treeUriPath)
        }

        initView()
    }

    private fun initView() {
        with (binding) {
            ivHome.setOnClickListener {
                ActivityUtils.finishToActivity(MainActivity::class.java, false, true)
            }
            rvLocalFolder.linear().setup {
                addType<LocalFolder>(R.layout.rv_media_source)
                addType<Video>(R.layout.rv_video)
                onBind {
                    when (itemViewType) {
                        R.layout.rv_media_source -> {
                            val localFolder = getModel<LocalFolder>()
                            val clBase = findView<ConstraintLayout>(R.id.clBase)
                            val ivIcon = findView<ImageView>(R.id.ivIcon)
                            val tvTitle = findView<TextView>(R.id.tvTitle)
                            ivIcon.setImageResource(R.drawable.ic_folder)
                            tvTitle.text = localFolder.name
                            clBase.setOnClickListener {
                                start(this@LocalFolderActivity, localFolder.url)
                            }
                        }
                        R.layout.rv_video -> {
                            val video = getModel<Video>()
                            val clBase = findView<ConstraintLayout>(R.id.clBase)
                            val ivCover = findView<ImageView>(R.id.ivCover)
                            val tvTitle = findView<TextView>(R.id.tvTitle)
                            ivCover.load(video.url) {

                            }
                            tvTitle.text = video.title
                            clBase.setOnClickListener {
                                PlayerActivity.start(this@LocalFolderActivity, video.url, video.title)
                            }
                        }
                    }
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            localFolderViewModel.title
                .filter { it != null }
                .collect {
                    binding.tvTitle.text = it
                }
        }
        lifecycleScope.launchWhenCreated {
            localFolderViewModel.files
                .filter { it != null }
                .collect {
                    binding.rvLocalFolder.bindingAdapter.models = it
                }
        }
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