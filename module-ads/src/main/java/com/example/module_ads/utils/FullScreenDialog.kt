package com.example.module_ads.utils

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import com.example.module_ads.R
import com.example.module_ads.databinding.InterstitialLoadingDialogBinding

class FullScreenDialog(
    private val context: Activity,
) : Dialog(context, R.style.FullScreenDialogStyle) {

    private lateinit var binding: InterstitialLoadingDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        binding = InterstitialLoadingDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window?.apply {
            setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
        setCancelable(false)
    }
}
