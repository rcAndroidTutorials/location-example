package es.algoritmo.backgroundlocation.mylocationmanager

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

class PermissionsArray {
    private val coarse = Manifest.permission.ACCESS_COARSE_LOCATION
    private val fine = Manifest.permission.ACCESS_FINE_LOCATION
    @RequiresApi(Build.VERSION_CODES.Q)
    private val background = Manifest.permission.ACCESS_BACKGROUND_LOCATION
    fun getArrayPermissions(context: Context, permissionType: PermissionType): Array<String> {
        return when (permissionType) {
            PermissionType.FOREGROUND_MAX_ACCURACY -> {
                arrayOf(fine)
            }
            PermissionType.FOREGROUND_BATTERY_FRIENDLY -> {
                arrayOf(coarse)
            }
            PermissionType.BACKGROUND_MAX_ACCURACY -> {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    arrayOf(fine)
                } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q){ // Android 10
                    arrayOf(fine, background)
                } else { // Android 11 or above
                    if (ContextCompat.checkSelfPermission(context, fine)!=PackageManager.PERMISSION_GRANTED) {
                        arrayOf(fine)
                    } else if (ContextCompat.checkSelfPermission(context, background)!=PackageManager.PERMISSION_GRANTED) {
                        arrayOf(background)
                    } else {
                        arrayOf()
                    }
                }
            }
            PermissionType.BACKGROUND_BATTERY_FRIENDLY -> {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    arrayOf(coarse)
                } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q){ // Android 10
                    arrayOf(coarse, background)
                } else { // Android 11 or above
                    if (ContextCompat.checkSelfPermission(context, coarse)!=PackageManager.PERMISSION_GRANTED) {
                        arrayOf(coarse)
                    } else if (ContextCompat.checkSelfPermission(context, background)!=PackageManager.PERMISSION_GRANTED) {
                        arrayOf(background)
                    } else {
                        arrayOf()
                    }
                }

            }
        }
    }
}
