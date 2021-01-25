package es.algoritmo.backgroundlocation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import es.algoritmo.backgroundlocation.mylocationmanager.MyLocationManager
import es.algoritmo.backgroundlocation.mylocationmanager.PermissionState
import es.algoritmo.backgroundlocation.mylocationmanager.PermissionType

class ExplanationActivity: AppCompatActivity() {

    private val locationManager = MyLocationManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_explanation)
        locationManager.registry(this, PermissionType.BACKGROUND_MAX_ACCURACY)
        findViewById<Button>(R.id.btFetchLocations).setOnClickListener {
            locationManager.requestPermissions(applicationContext) { permissionState ->
                when(permissionState) {
                    PermissionState.GRANTED -> {
                        Log.e("DEBUG", "GRANTED")
                        Intent(this, GPSActivity::class.java).also {
                            startActivity(it)
                        }
                    }
                    PermissionState.GRANTED_FOREGROUND_ONLY -> {
                        Log.e("DEBUG", "GRANTED_FOREGROUND_ONLY")
                    }
                    PermissionState.DENIED -> {
                        Log.e("DEBUG", "DENIED")
                    }
                }
            }
        }
    }
}