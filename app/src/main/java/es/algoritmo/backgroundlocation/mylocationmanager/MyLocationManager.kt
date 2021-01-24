package es.algoritmo.backgroundlocation.mylocationmanager

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

typealias CallbackLocationManager = (state: PermissionState) -> Unit
enum class PermissionState {
    GRANTED,
    GRANTED_FOREGROUND_ONLY,
    DENIED
}
enum class PermissionType {
    FOREGROUND_MAX_ACCURACY,
    FOREGROUND_BATTERY_FRIENDLY,
    BACKGROUND_MAX_ACCURACY,
    BACKGROUND_BATTERY_FRIENDLY
}
class MyLocationManager {
    private var activityResult: ActivityResultLauncher<Array<String>>? = null
    private var callback: CallbackLocationManager? = null
    private var permissionType: PermissionType? = null
    private val permissionsArray = PermissionsArray()

    fun checkPermisions(context: Context, permissionType: PermissionType): PermissionState {
        when (permissionType) {
            PermissionType.FOREGROUND_MAX_ACCURACY -> {
                return if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    PermissionState.GRANTED
                } else {
                    PermissionState.DENIED
                }
            }
            PermissionType.FOREGROUND_BATTERY_FRIENDLY -> {
                return if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    PermissionState.GRANTED
                } else {
                    PermissionState.DENIED
                }
            }
            PermissionType.BACKGROUND_MAX_ACCURACY -> {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    return if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        PermissionState.GRANTED
                    } else {
                        PermissionState.DENIED
                    }
                } else {
                    return if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                PermissionState.GRANTED
                            } else {
                                PermissionState.GRANTED_FOREGROUND_ONLY
                            }
                    } else {
                        PermissionState.DENIED
                    }
                }
            }
            PermissionType.BACKGROUND_BATTERY_FRIENDLY -> {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    return if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        PermissionState.GRANTED
                    } else {
                        PermissionState.DENIED
                    }
                } else {
                    return if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            PermissionState.GRANTED
                        } else {
                            PermissionState.GRANTED_FOREGROUND_ONLY
                        }
                    } else {
                        PermissionState.DENIED
                    }
                }
            }
        }

    }
    fun registry(activity: AppCompatActivity, permissionType: PermissionType){
        this.permissionType = permissionType
        activityResult = activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            var fineLocation = false
            var coarseLocation = false
            var backgroundLocation = false
            permissions.entries.forEach {
                Log.e("DEBUG", "${it.key} = ${it.value}")
                if (it.key==Manifest.permission.ACCESS_FINE_LOCATION) {
                    fineLocation = it.value
                } else if (it.key==Manifest.permission.ACCESS_COARSE_LOCATION) {
                    coarseLocation = it.value
                } else if (it.key==Manifest.permission.ACCESS_BACKGROUND_LOCATION) {
                    backgroundLocation = it.value
                }
            }
            when(permissionType) {
                PermissionType.FOREGROUND_MAX_ACCURACY -> {
                    callback?.let {
                        it(if (fineLocation) PermissionState.GRANTED else PermissionState.GRANTED)
                    }
                }
                PermissionType.FOREGROUND_BATTERY_FRIENDLY -> {
                    callback?.let {
                        it(if (coarseLocation) PermissionState.GRANTED else PermissionState.GRANTED)
                    }
                }
                PermissionType.BACKGROUND_MAX_ACCURACY -> {
                    callback?.let {
                        if (fineLocation || ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED) {
                            if (backgroundLocation) {
                                it(PermissionState.GRANTED)
                            } else {
                                it(PermissionState.GRANTED_FOREGROUND_ONLY)
                            }
                        } else {
                            it(PermissionState.DENIED)
                        }
                    }
                }
                PermissionType.BACKGROUND_BATTERY_FRIENDLY -> {
                    callback?.let {
                        if (coarseLocation || ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED) {
                            if (backgroundLocation) {
                                it(PermissionState.GRANTED_FOREGROUND_ONLY)
                            }
                        } else {
                            it(PermissionState.DENIED)
                        }
                    }
                }
            }
        }
    }
    fun requestPermissions(context: Context, callback: CallbackLocationManager) {
        permissionType?.also { permissions ->
            this.callback = callback
            activityResult?.also {
                it.launch(permissionsArray.getArrayPermissions(context, permissions))
            }
        }
    }
}
