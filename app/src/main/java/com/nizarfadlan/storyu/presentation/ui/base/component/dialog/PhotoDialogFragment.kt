package com.nizarfadlan.storyu.presentation.ui.base.component.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.nizarfadlan.storyu.R
import com.nizarfadlan.storyu.databinding.LayoutPhotoDialogBinding

class PhotoDialogFragment(
    private val onCameraClick: () -> Unit,
    private val onGalleryClick: () -> Unit
) : DialogFragment() {
    private var _binding: LayoutPhotoDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LayoutPhotoDialogBinding.inflate(inflater, container, false)

        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.apply {
            window?.setBackgroundDrawableResource(R.drawable.bg_dialog_transparent)
        }
        initActions()
    }

    private fun initActions() {
        binding.apply {
            btnClose.setOnClickListener {
                dismiss()
            }

            containerCamera.setOnClickListener {
                onCameraClick.invoke()
                dismiss()
            }

            containerGallery.setOnClickListener {
                onGalleryClick.invoke()
                dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}