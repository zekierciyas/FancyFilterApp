package com.zekierciyas.fancyfilterapp.ui.camera

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.zekierciyas.fancyfilterapp.R
import com.zekierciyas.fancyfilterapp.databinding.FragmentCameraBinding
import com.zekierciyas.fancyfilterapp.model.FilterSelectionNavModel
import com.zekierciyas.library.model.SimpleCameraStateModel
import com.zekierciyas.library.observe.Observers
import com.zekierciyas.library.observe.SimpleCameraState
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnNeverAskAgain
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.OnShowRationale
import permissions.dispatcher.PermissionRequest
import permissions.dispatcher.RuntimePermissions

@RuntimePermissions
class CameraFragment: Fragment(R.layout.fragment_camera) {

    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    fun initCameraView() {
        binding.cameraView
            .observeCameraState(observerCameraState)
            .imageCapture(this) {
                {
                    // Could be used to check if camera ready to take photo
                }
            }
    }

    @OnShowRationale(Manifest.permission.CAMERA)
    fun showRationaleForCamera(request: PermissionRequest) {

    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    fun onCameraDenied() {
        Toast.makeText(requireActivity(), R.string.permission_camera_denied, Toast.LENGTH_SHORT).show()
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    fun onCameraNeverAskAgain() {
        Toast.makeText(requireActivity(), R.string.permission_camera_never_askagain, Toast.LENGTH_SHORT).show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCameraView()

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
                    val action = CameraFragmentDirections.actionCameraFragmentToFilterSelectorFragment(
                            FilterSelectionNavModel(savedUri)
                        )
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