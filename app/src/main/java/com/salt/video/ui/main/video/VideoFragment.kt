package com.salt.video.ui.main.video

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.salt.video.R
import com.salt.video.databinding.FragmentVideoBinding
import com.salt.video.ui.base.LazyFragment
import com.salt.video.ui.main.MainActivity

class VideoFragment : LazyFragment() {

    private var _binding: FragmentVideoBinding? = null
    private val binding get() = _binding!!

    private val videoViewModel: VideoViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentVideoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun lazyInit() {
        with(binding) {
            val homeItems = listOf(
                HomeItem.SINGLE_LOCAL_VIDEO,
                HomeItem.SINGLE_LOCAL_AUDIO,
                HomeItem.SINGLE_INTERNET_AUDIO_VIDEO
            )
            val homeFooters = listOf(
                HomeFooter.ADD_LOCAL_FOLDER,
                HomeFooter.ADD_WEBDAV_FOLDER
            )

            rvHome.linear().setup {
                addType<HomeItem>(R.layout.rv_home)
                addType<HomeFooter>(R.layout.rv_home_footer)
                onBind {
                    when (itemViewType) {
                        R.layout.rv_home -> {
                            val homeItem = getModel<HomeItem>()
                            val ivIcon = findView<ImageView>(R.id.ivIcon)
                            val tvTitle = findView<TextView>(R.id.tvTitle)
                            ivIcon.setImageResource(
                                when (homeItem) {
                                    HomeItem.SINGLE_LOCAL_VIDEO -> R.drawable.ic_video_file
                                    HomeItem.SINGLE_LOCAL_AUDIO -> R.drawable.ic_audio_file
                                    HomeItem.SINGLE_INTERNET_AUDIO_VIDEO -> R.drawable.ic_wifi_tethering
                                }
                            )
                            tvTitle.text = when (homeItem) {
                                HomeItem.SINGLE_LOCAL_VIDEO -> "单个本地视频"
                                HomeItem.SINGLE_LOCAL_AUDIO -> "单个本地音乐"
                                HomeItem.SINGLE_INTERNET_AUDIO_VIDEO -> "单个网络音视频"
                            }
                        }

                        R.layout.rv_home_footer -> {
                            val homeFooter = getModel<HomeFooter>()
                            val tvTitle = findView<TextView>(R.id.tvTitle)
                            val mainActivity = requireActivity() as MainActivity
                            tvTitle.text = when (homeFooter) {
                                HomeFooter.ADD_LOCAL_FOLDER -> "添加本地文件夹"
                                HomeFooter.ADD_WEBDAV_FOLDER -> "添加 WebDAV"
                            }
                            tvTitle.setOnClickListener {
                                when (homeFooter) {
                                    HomeFooter.ADD_LOCAL_FOLDER -> mainActivity.openDocumentTreeLauncher()
                                    HomeFooter.ADD_WEBDAV_FOLDER -> {}
                                }
                            }
                        }
                    }
                }
                onClick(R.id.item) {
                    val homeItem = getModel<HomeItem>()
                    val mainActivity = requireActivity() as MainActivity
                    when (homeItem) {
                        HomeItem.SINGLE_LOCAL_VIDEO -> mainActivity.openDocumentLauncher("video/*")
                        HomeItem.SINGLE_LOCAL_AUDIO -> mainActivity.openDocumentLauncher("audio/*")
                        HomeItem.SINGLE_INTERNET_AUDIO_VIDEO -> mainActivity.openDialog()
                    }
                }
            }

            lifecycleScope.launchWhenCreated {
                videoViewModel.getAllMediaSource().collect { mediaSources ->
                    rvHome.bindingAdapter.models = homeItems + mediaSources + homeFooters
                }
            }
        }
    }

}

enum class HomeItem {
    SINGLE_LOCAL_VIDEO,
    SINGLE_LOCAL_AUDIO,
    SINGLE_INTERNET_AUDIO_VIDEO
}

enum class HomeFooter {
    ADD_LOCAL_FOLDER,
    ADD_WEBDAV_FOLDER
}