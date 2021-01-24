package es.algoritmo.backgroundlocation

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import es.algoritmo.backgroundlocation.mylocationmanager.MyLocationManager
import es.algoritmo.backgroundlocation.mylocationmanager.PermissionState
import es.algoritmo.backgroundlocation.mylocationmanager.PermissionType

class MainActivity : AppCompatActivity() {
    private val locationManager = MyLocationManager()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.btFetchLocations).setOnClickListener {
            when (locationManager.checkPermisions(this, PermissionType.BACKGROUND_MAX_ACCURACY)) {
                PermissionState.GRANTED -> {
                    Intent(this, GPSActivity::class.java).also {
                        startActivity(it)
                    }
                }
                PermissionState.DENIED -> {
                    Intent(this, ExplanationActivity::class.java).also {
                        startActivity(it)
                    }
                }
                PermissionState.GRANTED_FOREGROUND_ONLY -> {
                    Intent(this, ExplanationActivity::class.java).also {
                        startActivity(it)
                    }
                }
            }
        }
    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        locationManager.handleResponse(requestCode, permissions, grantResults)
//    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

}