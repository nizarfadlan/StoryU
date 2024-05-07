package com.nizarfadlan.storyu.presentation.ui.base

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.location.LocationManagerCompat
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.nizarfadlan.storyu.R
import com.nizarfadlan.storyu.utils.constant.AppConstant.REQUIRED_LOCATION_PERMISSION
import com.nizarfadlan.storyu.utils.helpers.showSnackBar
import timber.log.Timber

abstract class BaseLocationFragment<viewBinding : ViewBinding> : Fragment() {
    private var _binding: viewBinding? = null
    protected val binding get() = _binding!!
    private var mFusedLocationClient: FusedLocationProviderClient? = null

    private val priority = Priority.PRIORITY_BALANCED_POWER_ACCURACY
    private val cancellationTokenSource = CancellationTokenSource()

    private fun isLocationEnabled(): Boolean {
        val locationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return LocationManagerCompat.isLocationEnabled(locationManager)
    }

    protected fun initPermissionAndGetLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        requestPermissionLauncher.launch(REQUIRED_LOCATION_PERMISSION)
    }

    protected fun getLocation(onLocationReceived: (location: Location) -> Unit) {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            MaterialAlertDialogBuilder(requireContext())
                .setIcon(R.drawable.ic_location_broken_24)
                .setTitle(getString(R.string.title_location_required))
                .setMessage(getString(R.string.message_location_required))
                .setPositiveButton(
                    "OK"
                ) { p0, _ ->
                    requestPermissionLauncher.launch(REQUIRED_LOCATION_PERMISSION)
                    p0.dismiss()
                }
                .create()
                .show()
            return
        }

        if (!isLocationEnabled()) {
            MaterialAlertDialogBuilder(requireContext())
                .setIcon(R.drawable.ic_location_off_24)
                .setTitle(getString(R.string.title_ask_permission_location))
                .setMessage(getString(R.string.message_location_access))
                .setPositiveButton(
                    "OK"
                ) { p0, _ ->
                    getLocation(onLocationReceived)
                    p0.dismiss()
                }
                .create()
                .show()
            return
        }

        mFusedLocationClient?.getCurrentLocation(priority, cancellationTokenSource.token)
            ?.addOnSuccessListener { location ->
                if (location == null) return@addOnSuccessListener
                onLocationReceived(location)
            }
            ?.addOnFailureListener { exception ->
                exception.message?.let { binding.root.showSnackBar(it) }
            }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.entries.all {
                it.value
            }
            if (granted) {
                getLocation {
                    Timber.d("lat ${it.latitude}, lng ${it.longitude}")
                }
            }
        }

    protected fun allPermissionLocation() = ActivityCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater(inflater, container, false)
        return _binding?.root
    }

    abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> viewBinding

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}