package com.nizarfadlan.storyu.presentation.ui.base.component

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputEditText
import com.nizarfadlan.storyu.R
import com.nizarfadlan.storyu.utils.helpers.isFullNameCorrect

class FullNameInputEditText : TextInputEditText {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                error = if (s.isNotEmpty() && !s.toString().isFullNameCorrect)
                    context.getString(R.string.validation_full_name)
                else null
            }

            override fun afterTextChanged(s: Editable) {
                // Do nothing.
            }
        })

        setTextAppearance(R.style.textNormal)
        setSingleLine()
    }
}