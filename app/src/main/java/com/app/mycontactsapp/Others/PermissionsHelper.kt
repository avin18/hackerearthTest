package com.app.mycontactsapp.Others

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat

object PermissionsHelper {

    fun checkPermission(context: Context,
                        activity: AppCompatActivity,
                        permission: String,
                        onPermissionsGranted: () -> Unit = {},
                        onPermissionsDenied: (ActivityResultLauncher<String>?, Boolean) -> Unit){

        var requestPermissionLauncher: ActivityResultLauncher<String>? = null

        requestPermissionLauncher = activity.registerForActivityResult(ActivityResultContracts.RequestPermission())
        { isGranted: Boolean ->
            if (isGranted) {
                // Permission granted
                onPermissionsGranted()
            } else {
                // Permission denied
                onPermissionsDenied(requestPermissionLauncher, checkIfUserClickedNeverShowAgain(activity, permission))
            }
        }

        when {
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED -> {
                // Permission granted
                onPermissionsGranted()
            }
            shouldShowRequestPermissionRationale(activity,permission) -> {
                // Permission Denied
                onPermissionsDenied(requestPermissionLauncher, checkIfUserClickedNeverShowAgain(activity, permission))
            }
            else -> {
                // Requesting for Permission
                requestPermissionLauncher.launch(
                    permission)
            }
        }

    }

    private fun checkIfUserClickedNeverShowAgain(activity: AppCompatActivity, permission: String)
            = !shouldShowRequestPermissionRationale(activity,permission)

}