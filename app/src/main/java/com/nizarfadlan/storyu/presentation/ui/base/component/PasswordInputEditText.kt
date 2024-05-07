package com.nizarfadlan.storyu.presentation.ui.base.component

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputEditText
import com.nizarfadlan.storyu.R
import com.nizarfadlan.storyu.utils.constant.AppConstant.PASSWORD_LENGTH
import com.nizarfadlan.storyu.utils.helpers.isLengthPasswordCorrect

class PasswordInputEditText : TextInputEditText {
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
                //Do Nothing
            }

            override fun onTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                error = if (s.isNotEmpty() && !s.toString().isLengthPasswordCorrect)
                    String.format(
                        context.getString(R.string.validation_password),
                        PASSWORD_LENGTH
                    )
                else null
            }

            override fun afterTextChanged(edt: Editable?) {
                //Do Nothing
            }
        })

        setSingleLine()
        setTextAppearance(R.style.textNormal)
        inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
    }
}