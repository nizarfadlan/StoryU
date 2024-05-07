package com.nizarfadlan.storyu.presentation.ui.base.component

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.android.material.textfield.TextInputLayout
import com.nizarfadlan.storyu.R
import com.nizarfadlan.storyu.utils.helpers.dpToPx

class InputLayout : TextInputLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        boxBackgroundMode = BOX_BACKGROUND_OUTLINE
        isExpandedHintEnabled = false

        val shapeAppearanceModel = ShapeAppearanceModel.builder()
            .setAllCorners(CornerFamily.ROUNDED, 16.dpToPx(context))
            .build()
        setShapeAppearanceModel(shapeAppearanceModel)


        setHintTextAppearance(R.style.textBold)
        setHelperTextTextAppearance(R.style.textBold)
        setCounterOverflowTextAppearance(R.style.textBold)
        setCounterTextAppearance(R.style.textBold)
        placeholderTextAppearance = R.style.textMuted
    }
}