package com.nizarfadlan.storyu.presentation.ui.welcome

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowInsets
import android.view.WindowManager
import com.nizarfadlan.storyu.databinding.ActivityWelcomeBinding
import com.nizarfadlan.storyu.presentation.ui.auth.AuthActivity
import com.nizarfadlan.storyu.presentation.ui.base.BaseActivity

class WelcomeActivity : BaseActivity<ActivityWelcomeBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivityWelcomeBinding =
        ActivityWelcomeBinding::inflate

    override fun onViewBindingCreated(savedInstanceState: Bundle?) {
        super.onViewBindingCreated(savedInstanceState)

        binding.apply {
            startNow.setOnClickListener {
                startActivity(Intent(this@WelcomeActivity, AuthActivity::class.java))
            }
        }
    }

    public override fun onResume() {
        super.onResume()
        setupView()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}