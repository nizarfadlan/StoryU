package com.nizarfadlan.storyu.presentation.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.viewbinding.ViewBinding
import com.nizarfadlan.storyu.domain.model.Language
import com.nizarfadlan.storyu.domain.model.ThemeMode
import com.nizarfadlan.storyu.presentation.ui.main.setting.SettingViewModel
import com.nizarfadlan.storyu.utils.helpers.observe
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

abstract class BaseActivity<viewBinding : ViewBinding> : AppCompatActivity() {
    protected lateinit var binding: viewBinding
        private set

    private val settingViewModel: SettingViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = bindingInflater.invoke(layoutInflater).apply {
            setContentView(root)
        }

        setupEdgeToEdge()
        onViewBindingCreated(savedInstanceState)
        observe(settingViewModel.getThemeSettings(), ::setTheme)
        observe(settingViewModel.getLanguageSettings(), ::setLanguage)
    }

    protected open fun onViewBindingCreated(savedInstanceState: Bundle?) {}

    private fun setupEdgeToEdge() {
        enableEdgeToEdge()
        binding.apply {
            ViewCompat.setOnApplyWindowInsetsListener(root) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.updatePadding(
                    systemBars.left,
                    systemBars.top,
                    systemBars.right,
                    systemBars.bottom
                )

                WindowInsetsCompat.CONSUMED
            }
        }
    }

    private fun setTheme(themeMode: ThemeMode) {
        when (themeMode) {
            ThemeMode.DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            ThemeMode.LIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }

    private fun setLanguage(language: Language) {
        val valueLanguage = if (language == Language.SYSTEM) {
            Locale.getDefault().language
        } else {
            language.value
        }

        val localeList = LocaleListCompat.forLanguageTags(valueLanguage)
        AppCompatDelegate.setApplicationLocales(localeList)
    }

    abstract val bindingInflater: (LayoutInflater) -> viewBinding

}