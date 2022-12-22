package com.salt.video.ui.base

import androidx.fragment.app.Fragment

/**
 * 延迟加载 Fragment
 */
abstract class LazyFragment: Fragment() {

    var isLoaded = false
        private set

    override fun onResume() {
        super.onResume()
        if (!isLoaded && !isHidden) {
            lazyInit()
            isLoaded = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isLoaded = false
    }

    abstract fun lazyInit()

}