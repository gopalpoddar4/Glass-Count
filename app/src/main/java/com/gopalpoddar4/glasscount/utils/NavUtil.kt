package com.gopalpoddar4.glasscount.utils

import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

object NavUtil {
    fun applyBottomSystemMargin(view: View) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val navBarInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val layoutParams = v.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.bottomMargin = navBarInsets.bottom
            v.layoutParams = layoutParams
            insets
        }
    }
}