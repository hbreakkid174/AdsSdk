package com.example.module_ads.views

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class AdDiagonalTextView : AppCompatTextView {
    private val angle = 45f // Adjust the angle as needed

    constructor(context: Context?) : super(context!!)
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    )

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!, attrs, defStyleAttr
    )

    override fun onDraw(canvas: Canvas) {
        canvas.save()
        canvas.rotate(angle, width / 2f, height / 2f) // Rotate around the center
        super.onDraw(canvas)
        canvas.restore()
    }
}