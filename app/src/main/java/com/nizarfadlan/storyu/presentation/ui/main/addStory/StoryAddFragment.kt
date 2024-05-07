package com.nizarfadlan.storyu.presentation.ui.main.addStory

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.nizarfadlan.storyu.R
import com.nizarfadlan.storyu.databinding.FragmentStoryAddBinding
import com.nizarfadlan.storyu.domain.common.ResultState
import com.nizarfadlan.storyu.presentation.ui.base.BaseLocationFragment
import com.nizarfadlan.storyu.presentation.ui.base.component.bottomSheet.MapBottomSheet
import com.nizarfadlan.storyu.presentation.ui.base.component.dialog.PhotoDialogFragment
import com.nizarfadlan.storyu.presentation.ui.base.component.snackbar.CustomSnackbar
import com.nizarfadlan.storyu.presentation.ui.camera.CameraActivity
import com.nizarfadlan.storyu.presentation.ui.camera.CameraActivity.Companion.CAMERAX_RESULT
import com.nizarfadlan.storyu.utils.constant.AppConstant.REQUIRED_CAMERA_PERMISSION
import com.nizarfadlan.storyu.utils.helpers.gone
import com.nizarfadlan.storyu.utils.helpers.observe
import com.nizarfadlan.storyu.utils.helpers.reduceFileImage
import com.nizarfadlan.storyu.utils.helpers.show
import com.nizarfadlan.storyu.utils.helpers.showSnackBar
import com.nizarfadlan.storyu.utils.helpers.uriToFile
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.io.File
import java.util.Locale

class StoryAddFragment : BaseLocationFragment<FragmentStoryAddBinding>() {
    private var imageFile: File? = null
    private var currentImageUri: Uri? = null
    private var location: Location? = null

    private val storyViewModel: StoryAddViewModel by viewModel()

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentStoryAddBinding =
        FragmentStoryAddBinding::inflate

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (!isGranted) binding.root.showSnackBar("Camera permission denied")
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            requireActivity(),
            REQUIRED_CAMERA_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCheckPermission()
        initToolbar()
        initActions()
        initView()
    }

    private fun initCheckPermission() {
        if (!allPermissionsGranted()) requestPermissionLauncher.launch(REQUIRED_CAMERA_PERMISSION)
    }

    private fun initView() {
        showImage()
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.apply {
                ivHolderPhoto.gone()
                ivPhoto.show()
                ivPhoto.setImageURI(it)
                btnChangeImage.show()
            }
        }
    }

    private fun initToolbar() {
        binding.toolbar.apply {
            tvPage.text = getString(R.string.title_fragment_add_story)
        }
    }

    private fun initActions() {
        binding.apply {
            ivHolderPhoto.setOnClickListener {
                showImageDialog()
            }

            btnChangeImage.setOnClickListener {
                showImageDialog()
            }

            buttonLocation.setOnClickListener {
                initPermissionBottomSheet()
                startBottomSheetLocation()
            }

            buttonAdd.setOnClickListener {
                uploadStory()
            }
        }
    }

    private fun uploadStory() {
        val storyDescription = binding.edAddDescription.text.toString()
        when {
            storyDescription.isEmpty() -> binding.edAddDescription.error =
                getString(R.string.validation_must_not_empty)

            currentImageUri == null -> binding.root.showSnackBar(getString(R.string.validation_photo_empty))
            else -> {
                uploadStoryProcess(storyDescription)
            }
        }
    }

    private fun uploadStoryProcess(storyDescription: String) {
        currentImageUri?.let { uri ->
            imageFile = uri.uriToFile(requireContext()).reduceFileImage()

            observe(
                storyViewModel.addStory(
                    imageFile!!,
                    storyDescription,
                    location?.latitude,
                    location?.longitude
                ),
                ::showResultUploadStory
            )
        }
    }

    private fun showResultUploadStory(result: ResultState<String>) {
        when (result) {
            is ResultState.Loading -> {
                binding.buttonAdd.isEnabled = true
                showLoading(true)
            }

            is ResultState.Success -> {
                showLoading(false)
                setFragmentResult(
                    IS_UPLOAD_SUCCESS_KEY,
                    bundleOf(IS_UPLOAD_SUCCESS_BUNDLE to true)
                )
                binding.root.showSnackBar(
                    getString(R.string.message_success_upload_story),
                    Snackbar.LENGTH_SHORT
                ) {
                    addCallback(object : BaseTransientBottomBar.BaseCallback<CustomSnackbar>() {
                        override fun onDismissed(transientBottomBar: CustomSnackbar?, event: Int) {
                            backToStoryList()
                        }
                    })
                }
            }

            is ResultState.Error -> {
                showLoading(false)
                binding.root.showSnackBar(result.message)
            }
        }
    }

    private fun showImageDialog() {
        PhotoDialogFragment(::startCamera, ::startGallery)
            .show(childFragmentManager, PhotoDialogFragment::class.java.simpleName)
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERAX_RESULT) {
            currentImageUri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            showImage()
        }
    }

    private fun initPermissionBottomSheet() {
        initPermissionAndGetLocation()
        Timber.d("Permission location granted")
    }

    private fun startBottomSheetLocation() {
        if (allPermissionLocation()) showLoading(true)

        if (location == null) {
            getLocation {
                MapBottomSheet(it, ::onLocationReceived)
                    .show(childFragmentManager, MapBottomSheet.TAG)
                showLoading(false)
            }
        } else {
            MapBottomSheet(location!!, ::onLocationReceived)
                .show(childFragmentManager, MapBottomSheet.TAG)
            showLoading(false)
        }
    }

    private fun onLocationReceived(resultLocation: Location) {
        location = resultLocation
        val address = getAddressName(resultLocation.latitude, resultLocation.longitude)
            ?: getString(R.string.unknown_address)
        val addressWithLatLong = "$address (${resultLocation.latitude}, ${resultLocation.longitude})"
        setAddress(addressWithLatLong)
    }

    private fun startCamera() {
        val intent = Intent(requireActivity(), CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private fun setAddress(address: String) {
        binding.apply {
            tvFieldLocation.apply {
                text = address
                show()
            }
            tvTitleLocation.show()
        }
    }

    private fun getAddressName(lat: Double, lon: Double): String? {
        var addressName: String? = null
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocation(lat, lon, 1) { list ->
                if (list.size != 0) {
                    addressName = list[0].getAddressLine(0)
                }
            }
        } else {
            val list = geocoder.getFromLocation(lat, lon, 1)
            if (list != null && list.size != 0) {
                addressName = list[0].getAddressLine(0)
            }
        }
        return addressName
    }

    private fun backToStoryList() {
        findNavController().popBackStack()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loadingLayout.root.apply {
            if (isLoading) show() else gone()
        }
    }

    companion object {
        const val IS_UPLOAD_SUCCESS_BUNDLE = "is_upload_success_bundle"
        const val IS_UPLOAD_SUCCESS_KEY = "is_upload_success_key"
    }
}