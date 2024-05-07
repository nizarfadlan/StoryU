package com.nizarfadlan.storyu.presentation.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.nizarfadlan.storyu.R
import com.nizarfadlan.storyu.databinding.ActivityStoryMapBinding
import com.nizarfadlan.storyu.domain.common.ResultState
import com.nizarfadlan.storyu.domain.model.Story
import com.nizarfadlan.storyu.presentation.ui.base.BaseActivity
import com.nizarfadlan.storyu.utils.helpers.addMultipleMarker
import com.nizarfadlan.storyu.utils.helpers.boundsCameraToMarkers
import com.nizarfadlan.storyu.utils.helpers.convertToLatLng
import com.nizarfadlan.storyu.utils.helpers.gone
import com.nizarfadlan.storyu.utils.helpers.observe
import com.nizarfadlan.storyu.utils.helpers.setCustomStyle
import com.nizarfadlan.storyu.utils.helpers.show
import com.nizarfadlan.storyu.utils.helpers.showSnackBar
import org.koin.androidx.viewmodel.ext.android.viewModel

class StoryMapActivity : BaseActivity<ActivityStoryMapBinding>() {
    private val storyViewModel: StoryMapViewModel by viewModel()

    override val bindingInflater: (LayoutInflater) -> ActivityStoryMapBinding =
        ActivityStoryMapBinding::inflate

    override fun onViewBindingCreated(savedInstanceState: Bundle?) {
        super.onViewBindingCreated(savedInstanceState)

        callback?.onReady()
        initToolbar()
        initMap()
    }

    private fun initMap() {
        val mapFragment = supportFragmentManager
            .findFragmentById(binding.containerMap.id) as SupportMapFragment

        mapFragment.getMapAsync { googleMap ->
            googleMap.uiSettings.isZoomControlsEnabled = true
            googleMap.uiSettings.isZoomGesturesEnabled = true
            googleMap.setCustomStyle(this)
            observe(storyViewModel.recentStories) {
                onStoriesResult(googleMap, it)
            }
        }
    }

    private fun initToolbar() {
        binding.apply {
            toolbar.apply {
                tvPage.text = getString(R.string.title_stories_location)
                backButton.setOnClickListener { finish() }
            }
        }
    }

    private fun onStoriesResult(googleMap: GoogleMap, result: ResultState<List<Story>>) {
        when (result) {
            is ResultState.Loading -> showLoading(true)
            is ResultState.Success -> {
                showLoading(false)
                googleMap.addMultipleMarker(result.data, this)
                val listLocations = result.data.convertToLatLng()
                googleMap.boundsCameraToMarkers(listLocations, this)
                setButtonViews(googleMap, listLocations)
            }

            is ResultState.Error -> {
                showLoading(false)
                binding.root.showSnackBar(result.message)
            }
        }
    }

    private fun setButtonViews(googleMap: GoogleMap, listLocation: List<LatLng>) {
        binding.btnMapBounds.setOnClickListener {
            googleMap.boundsCameraToMarkers(listLocation, this)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loadingLayout.root.apply {
            if (isLoading) show() else gone()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        clearCallback()
    }

    companion object {
        private var callback: OnStoryMapActivityReadyCallback? = null

        fun setOnReadyCallback(callback: OnStoryMapActivityReadyCallback) {
            this.callback = callback
        }

        fun clearCallback() {
            this.callback = null
        }
    }
}