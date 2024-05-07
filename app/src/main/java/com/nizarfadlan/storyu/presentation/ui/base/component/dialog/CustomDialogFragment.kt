package com.nizarfadlan.storyu.presentation.ui.base.component.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class CustomDialogFragment(
    private val title: String,
    private val items: Array<String>,
    private val onSelected: (Int) -> Unit
) : DialogFragment() {
    override fun onCreateDialog(
        savedInstanceState: Bundle?
    ): Dialog {
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setCancelable(true)
            .setNegativeButton(getString(android.R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .setItems(items) { _, which ->
                onSelected(which)
            }
            .create()
    }
}