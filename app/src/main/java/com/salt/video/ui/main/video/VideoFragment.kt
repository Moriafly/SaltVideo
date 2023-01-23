package com.salt.video.ui.main.video

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.salt.video.R
import com.salt.video.databinding.FragmentVideoBinding
import com.salt.video.ui.base.LazyFragment
import com.salt.video.ui.main.MainActivity

class VideoFragment: LazyFragment() {

    private var _binding: FragmentVideoBinding? = null
    private val binding get() = _binding!!

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
                HomeItemType.LOCAL_AUDIO_VIDEO_FOLDER,
                HomeItemType.SINGLE_LOCAL_VIDEO,
                HomeItemType.SINGLE_LOCAL_AUDIO,
                HomeItemType.SINGLE_INTERNET_AUDIO_VIDEO
            )

            rvHome.linear().setup {
                addType<HomeItemType>(R.layout.rv_home)
                onBind {
                    val homeItemType = getModel<HomeItemType>()
                    val ivIcon = findView<ImageView>(R.id.ivIcon)
                    val tvTitle = findView<TextView>(R.id.tvTitle)
                    ivIcon.setImageResource(
                        when (homeItemType) {
                            HomeItemType.LOCAL_AUDIO_VIDEO_FOLDER -> R.drawable.ic_folder
                            HomeItemType.SINGLE_LOCAL_VIDEO -> R.drawable.ic_video_file
                            HomeItemType.SINGLE_LOCAL_AUDIO -> R.drawable.ic_audio_file
                            HomeItemType.SINGLE_INTERNET_AUDIO_VIDEO -> R.drawable.ic_wifi_tethering
                        }
                    )
                    tvTitle.text = when (homeItemType) {
                        HomeItemType.LOCAL_AUDIO_VIDEO_FOLDER -> "本地音视频文件夹"
                        HomeItemType.SINGLE_LOCAL_VIDEO -> "单个本地视频"
                        HomeItemType.SINGLE_LOCAL_AUDIO -> "单个本地音乐"
                        HomeItemType.SINGLE_INTERNET_AUDIO_VIDEO -> "单个网络音视频"
                    }
                }
                onClick(R.id.item) {
                    val homeItemType = getModel<HomeItemType>()
                    val mainActivity = requireActivity() as MainActivity
                    when (homeItemType) {
                        HomeItemType.LOCAL_AUDIO_VIDEO_FOLDER -> mainActivity.openDocumentTreeLauncher()
                        HomeItemType.SINGLE_LOCAL_VIDEO -> mainActivity.openDocumentLauncher("video/*")
                        HomeItemType.SINGLE_LOCAL_AUDIO -> mainActivity.openDocumentLauncher("audio/*")
                        HomeItemType.SINGLE_INTERNET_AUDIO_VIDEO -> mainActivity.openDialog()
                    }
                }
            }
            rvHome.bindingAdapter.models = homeItems
        }
    }

}

enum class HomeItemType {
    LOCAL_AUDIO_VIDEO_FOLDER,
    SINGLE_LOCAL_VIDEO,
    SINGLE_LOCAL_AUDIO,
    SINGLE_INTERNET_AUDIO_VIDEO
}