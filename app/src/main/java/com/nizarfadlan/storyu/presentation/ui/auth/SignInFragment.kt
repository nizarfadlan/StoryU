package com.nizarfadlan.storyu.presentation.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.nizarfadlan.storyu.R
import com.nizarfadlan.storyu.databinding.FragmentSignInBinding
import com.nizarfadlan.storyu.domain.common.ResultState
import com.nizarfadlan.storyu.presentation.ui.base.BaseFragment
import com.nizarfadlan.storyu.presentation.ui.base.component.snackbar.CustomSnackbar
import com.nizarfadlan.storyu.presentation.ui.main.MainActivity
import com.nizarfadlan.storyu.utils.constant.AppConstant.PASSWORD_LENGTH
import com.nizarfadlan.storyu.utils.helpers.gone
import com.nizarfadlan.storyu.utils.helpers.isLengthPasswordCorrect
import com.nizarfadlan.storyu.utils.helpers.observe
import com.nizarfadlan.storyu.utils.helpers.show
import com.nizarfadlan.storyu.utils.helpers.showSnackBar
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignInFragment : BaseFragment<FragmentSignInBinding>() {
    private val authViewModel: AuthViewModel by viewModel()

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSignInBinding =
        FragmentSignInBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            tvSignUp.setOnClickListener {
                moveToSignUp()
            }

            btnSignIn.setOnClickListener {
                signInRequest()
            }
        }
    }

    private fun signInRequest() {
        val email = binding.edLoginEmail.text.toString()
        val password = binding.edLoginPassword.text.toString()
        when {
            email.isEmpty() -> binding.edLoginEmail.error =
                getString(R.string.validation_must_not_empty)

            password.isEmpty() -> binding.edLoginPassword.error =
                getString(R.string.validation_must_not_empty)

            !password.isLengthPasswordCorrect -> binding.edLoginPassword.error =
                String.format(getString(R.string.validation_password), PASSWORD_LENGTH)

            else -> {
                signInProcess(email, password)
            }
        }
    }

    private fun signInProcess(email: String, password: String) {
        observe(authViewModel.signIn(email, password), ::showResultSignIn)
    }

    private fun showResultSignIn(result: ResultState<String>) {
        when (result) {
            is ResultState.Loading -> showLoading(true)

            is ResultState.Success -> {
                showLoading(false)
                binding.root.showSnackBar(
                    getString(R.string.message_signin_success),
                    Snackbar.LENGTH_SHORT
                ) {
                    addCallback(object : BaseTransientBottomBar.BaseCallback<CustomSnackbar>() {
                        override fun onDismissed(transientBottomBar: CustomSnackbar?, event: Int) {
                            val intent = Intent(requireContext(), MainActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }
                    })
                }
            }

            is ResultState.Error -> {
                showLoading(false)
                binding.root.showSnackBar(getString(R.string.message_signin_failed))
            }
        }
    }

    private fun moveToSignUp() {
        findNavController().navigate(
            SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
        )
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            if (isLoading) {
                loadingLayout.root.show()
            } else {
                loadingLayout.root.gone()
            }
        }
    }
}