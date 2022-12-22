package com.salt.video.ui.main.cloud

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.salt.video.databinding.FragmentCloudBinding
import com.salt.video.ui.base.LazyFragment

class CloudFragment: LazyFragment() {

    private var _binding: FragmentCloudBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCloudBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun lazyInit() {

    }

}