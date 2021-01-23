package es.algoritmo.backgroundlocation

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ExplanationActivity: AppCompatActivity() {

    private val locationManager = MyLocationManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_explanation)
        locationManager.registry(this, PermissionType.BACKGROUND_MAX_ACCURACY)
        findViewById<Button>(R.id.btCheckPermissions).setOnClickListener {
            if (locationManager.checkPermisions(applicationContext, PermissionType.BACKGROUND_MAX_ACCURACY)==PermissionState.DENIED) {
                locationManager.requestPermissions { permissionState ->
                    when(permissionState) {
                        PermissionState.GRANTED -> {

                        }
                        PermissionState.GRANTED_FOREGROUND_ONLY -> {

                        }
                        PermissionState.DENIED -> {

                        }
                    }
                }
            }

        }
    }
}