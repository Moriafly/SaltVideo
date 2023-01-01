package com.salt.video.ui.main.video

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.salt.video.databinding.FragmentVideoBinding
import com.salt.video.ui.base.LazyFragment

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

    }

}