package com.nizarfadlan.storyu.presentation.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.nizarfadlan.storyu.R
import com.nizarfadlan.storyu.databinding.FragmentSignUpBinding
import com.nizarfadlan.storyu.domain.common.ResultState
import com.nizarfadlan.storyu.presentation.ui.base.BaseFragment
import com.nizarfadlan.storyu.utils.constant.AppConstant
import com.nizarfadlan.storyu.utils.helpers.gone
import com.nizarfadlan.storyu.utils.helpers.isLengthPasswordCorrect
import com.nizarfadlan.storyu.utils.helpers.observe
import com.nizarfadlan.storyu.utils.helpers.show
import com.nizarfadlan.storyu.utils.helpers.showSnackBar
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUpFragment : BaseFragment<FragmentSignUpBinding>() {
    private val authViewModel: AuthViewModel by viewModel()

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSignUpBinding =
        FragmentSignUpBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            tvSignIn.setOnClickListener {
                backSignIn()
            }
            btnSignUp.setOnClickListener {
                signUpRequest()
            }
        }
    }

    private fun signUpRequest() {
        val name = binding.edRegisterName.text.toString()
        val email = binding.edRegisterEmail.text.toString()
        val password = binding.edRegisterPassword.text.toString()
        when {
            name.isEmpty() -> binding.edRegisterName.error =
                getString(R.string.validation_must_not_empty)

            email.isEmpty() -> binding.edRegisterEmail.error =
                getString(R.string.validation_must_not_empty)

            password.isEmpty() -> binding.edRegisterPassword.error =
                getString(R.string.validation_must_not_empty)

            !password.isLengthPasswordCorrect -> binding.edRegisterPassword.error = String.format(
                getString(
                    R.string.validation_password
                ), AppConstant.PASSWORD_LENGTH
            )

            else -> {
                signUpProcess(name, email, password)
            }
        }
    }

    private fun signUpProcess(name: String, email: String, password: String) {
        observe(authViewModel.signUp(name, email, password), ::showResultSignUp)
    }

    private fun showResultSignUp(result: ResultState<String>) {
        when (result) {
            is ResultState.Loading -> showLoading(true)

            is ResultState.Success -> {
                showLoading(false)
                binding.root.showSnackBar(result.data)
                backSignIn()
            }

            is ResultState.Error -> {
                showLoading(false)
                binding.root.showSnackBar(result.message)
            }
        }
    }

    private fun backSignIn() {
        findNavController().popBackStack()
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