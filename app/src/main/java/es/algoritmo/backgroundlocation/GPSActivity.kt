package es.algoritmo.backgroundlocation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices

class GPSActivity: AppCompatActivity() {
    lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private fun updateLocations() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val locationRequest = LocationRequest()
        // *** Aquí fijamos cada cuanto tiempo en mseg. se va actualizar la ubicación
        locationRequest.interval = 300
        // *** La velocidad más alta que se pueda obtener. Máx. 5000
        locationRequest.fastestInterval = 100
        // *** Qué prioridad le damos al cálculo de la ubicación. Le ponemos la precision. Ver opciones desde el . (punto)
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }
}