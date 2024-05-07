package com.nizarfadlan.storyu.presentation.ui.base.component.bottomSheet

import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nizarfadlan.storyu.R
import com.nizarfadlan.storyu.utils.helpers.setCustomStyle

class MapBottomSheet(
    private val currentLocation: Location,
    private val onActionsDone: (location: Location) -> Unit
) : BottomSheetDialogFragment() {
    private var location: Location? = null
    private var changeLocation = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.layout_map_bottom_sheet, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        location = location ?: currentLocation
        behavior(view)
        initViews()
        initActions(view)
    }

    private fun behavior(view: View) {
        val bottomSheet = view.findViewById<View>(R.id.map_bottom_sheet)
        val behavior = BottomSheetBehavior.from(bottomSheet)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.isDraggable = false
    }

    private fun initActions(view: View) {
        val buttonDone = view.findViewById<Button>(R.id.button_done_location)
        val buttonChange = view.findViewById<Button>(R.id.button_change_location)

        buttonDone.setOnClickListener {
            location?.let { onActionsDone(it) }
            dismiss()
        }

        buttonChange.setOnClickListener {
            changeLocation = !changeLocation
        }
    }

    private fun initViews() {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.containerMap) as SupportMapFragment?

        mapFragment?.getMapAsync { googleMap ->
            googleMap.setCustomStyle(requireContext())
            googleMap.uiSettings.isZoomControlsEnabled = true
            googleMap.uiSettings.isZoomGesturesEnabled = true

            markerLocation(googleMap)

            googleMap.setOnMapClickListener { latLng ->
                if (changeLocation) {
                    googleMap.clear()
                    val getLocation = Location("newLocation").apply {
                        latitude = latLng.latitude
                        longitude = latLng.longitude
                    }
                    location = getLocation
                    markerLocation(googleMap)
                    changeLocation = false
                }
            }
        }
    }

    private fun markerLocation(googleMap: GoogleMap) {
        val lat = location?.latitude
        val long = location?.longitude

        if (lat != null && long != null) {
            val currentLocation = LatLng(lat, long)
            googleMap.addMarker(createMarker(currentLocation))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f))
        }
    }

    private fun createMarker(latLng: LatLng): MarkerOptions {
        val iconDrawable = R.drawable.map_pin
        val icon = BitmapDescriptorFactory.fromResource(iconDrawable)
        return MarkerOptions()
            .position(latLng)
            .icon(icon)
    }

    companion object {
        const val TAG = "MapBottomSheet"
    }
}