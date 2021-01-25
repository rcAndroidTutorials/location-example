package es.algoritmo.backgroundlocation

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.*

class GPSActivity: AppCompatActivity() {
    lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var tvLocation: TextView
    val callback = object : LocationCallback() {

        override fun onLocationResult(locationResult: LocationResult?) {
            super.onLocationResult(locationResult)
            locationResult?.locations?.forEach { location ->
                tvLocation.text = "${location.latitude} ${location.longitude}"
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gps)
        tvLocation = findViewById(R.id.tvLocation)
        updateLocations()
    }

    @SuppressLint("MissingPermission")
    private fun updateLocations() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val locationRequest = LocationRequest()
        // *** Aquí fijamos cada cuanto tiempo en mseg. se va actualizar la ubicación
        locationRequest.interval = 300
        // *** La velocidad más alta que se pueda obtener. Máx. 5000
        locationRequest.fastestInterval = 100
        // *** Qué prioridad le damos al cálculo de la ubicación. Le ponemos la precision. Ver opciones desde el . (punto)
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        fusedLocationClient.requestLocationUpdates(locationRequest, callback, null)
    }
}