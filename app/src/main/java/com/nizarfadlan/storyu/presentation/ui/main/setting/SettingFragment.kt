package com.nizarfadlan.storyu.presentation.ui.main.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nizarfadlan.storyu.R
import com.nizarfadlan.storyu.databinding.FragmentSettingBinding
import com.nizarfadlan.storyu.domain.common.ResultState
import com.nizarfadlan.storyu.domain.model.Language
import com.nizarfadlan.storyu.domain.model.ThemeMode
import com.nizarfadlan.storyu.presentation.ui.auth.AuthViewModel
import com.nizarfadlan.storyu.presentation.ui.base.BaseFragment
import com.nizarfadlan.storyu.presentation.ui.base.component.dialog.CustomDialogFragment
import com.nizarfadlan.storyu.utils.helpers.capitalizeWords
import com.nizarfadlan.storyu.utils.helpers.gone
import com.nizarfadlan.storyu.utils.helpers.observe
import com.nizarfadlan.storyu.utils.helpers.show
import com.nizarfadlan.storyu.utils.helpers.showSnackBar
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingFragment : BaseFragment<FragmentSettingBinding>() {
    private val settingViewModel: SettingViewModel by viewModel()
    private val authViewModel: AuthViewModel by viewModel()

    private var selectedThemeMode: ThemeMode = ThemeMode.SYSTEM
    private var selectedLocale: Language = Language.SYSTEM

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSettingBinding =
        FragmentSettingBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initActions()
        initToolbar()
        initObserver()
    }

    private fun initObserver() {
        observe(settingViewModel.getThemeSettings(), ::setTextTheme)
        observe(settingViewModel.getLanguageSettings(), ::setTextLanguage)
    }

    private fun initActions() {
        binding.apply {
            switchTheme.setOnClickListener {
                showThemeDialog()
            }

            switchLanguage.setOnClickListener {
                showLanguageDialog()
            }

            actionLogout.setOnClickListener {
                observe(authViewModel.signOut, ::onSignOutResult)
            }
        }
    }

    private fun initToolbar() {
        binding.apply {
            toolbar.apply {
                tvPage.text = getString(R.string.title_fragment_setting)
            }
        }
    }

    private fun onSignOutResult(result: ResultState<String>) {
        when (result) {
            is ResultState.Loading -> binding.loadingLayout.root.show()
            is ResultState.Success -> binding.loadingLayout.root.gone()
            is ResultState.Error -> {
                binding.apply {
                    loadingLayout.root.gone()
                    root.showSnackBar(result.message)
                }
            }
        }
    }

    private fun setTextLanguage(language: Language) {
        binding.tvLanguage.text = language.name.capitalizeWords()
    }

    private fun setTextTheme(themeMode: ThemeMode) {
        binding.tvTheme.text = themeMode.name.capitalizeWords()
    }

    private fun showLanguageDialog() {
        val titleLanguage = getString(R.string.label_dialog_language)
        val languageList = arrayOf(
            getString(R.string.label_language_system),
            getString(R.string.label_language_english),
            getString(R.string.label_language_indonesia)
        )

        val languageDialog = CustomDialogFragment(
            titleLanguage,
            languageList
        ) { selectedLanguage ->
            selectedLocale = when (selectedLanguage) {
                0 -> Language.SYSTEM
                1 -> Language.ENGLISH
                else -> Language.INDONESIA
            }
            settingViewModel.saveLanguageSetting(selectedLocale)
        }

        languageDialog.show(parentFragmentManager, "LanguageDialogFragment")
    }

    private fun showThemeDialog() {
        val titleTheme = getString(R.string.label_dialog_theme)
        val themeList = arrayOf(
            getString(R.string.label_theme_mode_light),
            getString(R.string.label_theme_mode_dark),
            getString(R.string.label_theme_mode_system)
        )

        val themeDialog = CustomDialogFragment(
            titleTheme,
            themeList
        ) { selectedTheme ->
            selectedThemeMode = when (selectedTheme) {
                0 -> ThemeMode.LIGHT
                1 -> ThemeMode.DARK
                else -> ThemeMode.SYSTEM
            }
            settingViewModel.saveThemeSetting(selectedThemeMode)
        }

        themeDialog.show(parentFragmentManager, "ThemeDialogFragment")
    }
}