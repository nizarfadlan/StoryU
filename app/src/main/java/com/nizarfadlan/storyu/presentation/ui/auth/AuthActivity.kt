package com.nizarfadlan.storyu.presentation.ui.auth

import android.view.LayoutInflater
import com.nizarfadlan.storyu.databinding.ActivityAuthBinding
import com.nizarfadlan.storyu.presentation.ui.base.BaseActivity

class AuthActivity : BaseActivity<ActivityAuthBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityAuthBinding =
        ActivityAuthBinding::inflate
}