package com.nizarfadlan.storyu.presentation.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.nizarfadlan.storyu.R
import com.nizarfadlan.storyu.databinding.ActivityMainBinding
import com.nizarfadlan.storyu.presentation.ui.auth.AuthViewModel
import com.nizarfadlan.storyu.presentation.ui.base.BaseActivity
import com.nizarfadlan.storyu.presentation.ui.welcome.WelcomeActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<ActivityMainBinding>() {
    private val authViewModel: AuthViewModel by viewModel()

    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding =
        ActivityMainBinding::inflate

    override fun onViewBindingCreated(savedInstanceState: Bundle?) {
        super.onViewBindingCreated(savedInstanceState)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        binding.bottomNavigation.apply {
            setupWithNavController(
                navHostFragment.navController
            )
        }
    }

    override fun onStart() {
        super.onStart()

        authViewModel.getSession().observe(this) {
            if (it.id === "" || it === null) {
                moveToWelcome()
            }
        }
    }

    private fun moveToWelcome() {
        startActivity(Intent(this, WelcomeActivity::class.java))
        finish()
    }
}