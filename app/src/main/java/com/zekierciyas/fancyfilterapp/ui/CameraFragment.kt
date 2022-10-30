package com.zekierciyas.fancyfilterapp.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.zekierciyas.fancyfilterapp.R
import com.zekierciyas.fancyfilterapp.databinding.FragmentCameraBinding
import com.zekierciyas.fancyfilterapp.model.FilterSelectionNavModel
import com.zekierciyas.fancyfilterapp.util.CameraPermissionHelper
import com.zekierciyas.library.model.SimpleCameraStateModel
import com.zekierciyas.library.observe.Observers
import com.zekierciyas.library.observe.SimpleCameraState

class CameraFragment: Fragment(R.layout.fragment_camera) {

    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!
    private val cameraPermissionHelper: CameraPermissionHelper by lazy { CameraPermissionHelper() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        cameraPermissionHelper.requestCameraPermission(
            requireActivity(),
            permissionDenied = {

            },
            permissionGranted = {
                binding.cameraView
                    .observeCameraState(observerCameraState)
                    .imageCapture(this) {
                        {
                            // Could be used to check if camera ready to take photo
                        }
                    }
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.captureButton.setOnClickListener {
            binding.cameraView.takePhoto(observerImageCapture)
        }

        binding.flipCameraButton.setOnClickListener {
            binding.cameraView.flipCamera()
        }
    }

    private val observerImageCapture: Observers.ImageCapture = object : Observers.ImageCapture {
        override fun result(savedUri: Uri?, exception: Exception?) {
            if (savedUri != null) {
                println("Photo taken successfully")
                requireActivity().runOnUiThread {
                    val action =
                        CameraFragmentDirections
                            .actionCameraFragmentToFilterSelectorFragment(FilterSelectionNavModel(savedUri))
                    findNavController().navigate(action)
                }
            }
        }
    }

    private val observerCameraState: Observers.CameraState = object  : Observers.CameraState {
        override fun cameraState(simpleCameraState: SimpleCameraStateModel) {
            when (simpleCameraState.action) {
                is SimpleCameraState.Action.Closed -> {

                }

                is SimpleCameraState.Action.Closing -> {

                }

                is SimpleCameraState.Action.Open -> {

                }

                is SimpleCameraState.Action.Opening -> {

                }

                is SimpleCameraState.Action.Pending -> {

                }
                else -> {

                }
            }

            when (simpleCameraState.error) {
                is SimpleCameraState.Error.ErrorCameraDisabled -> {

                }

                is SimpleCameraState.Error.ErrorCameraFatalError -> {

                }

                is SimpleCameraState.Error.ErrorCameraInUse -> {

                }

                is SimpleCameraState.Error.ErrorDoNotDisturbModelEnabled -> {

                }

                is SimpleCameraState.Error.ErrorMaxCameraInUse -> {

                }

                is SimpleCameraState.Error.ErrorOtherRecoverableError -> {

                }

                is SimpleCameraState.Error.ErrorStreamConfig -> {

                }
                else -> {

                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}