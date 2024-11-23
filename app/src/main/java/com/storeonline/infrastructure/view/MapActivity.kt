package com.storeonline.infrastructure.view

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.storeonline.R
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.util.Locale
import java.util.concurrent.Executors

class MapActivity : AppCompatActivity() {

    private lateinit var mapView: MapView
    private lateinit var geocoder: Geocoder

    companion object {
        const val EXTRA_SELECTED_LOCATION = "extra_selected_location"
        const val EXTRA_LATITUDE = "extra_latitude"
        const val EXTRA_LONGITUDE = "extra_longitude"
    }

    @SuppressLint("MissingInflatedId", "ClickableViewAccessibility", "NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        Configuration.getInstance().load(applicationContext, getPreferences(MODE_PRIVATE))

        mapView = findViewById(R.id.mapView)
        mapView.setMultiTouchControls(true)

        geocoder = Geocoder(this, Locale.getDefault())

        val defaultLocation = GeoPoint(4.711, -74.0721)
        val mapController: IMapController = mapView.controller
        mapController.setZoom(15.0)
        mapController.setCenter(defaultLocation)

        val marker = Marker(mapView)
        marker.position = defaultLocation
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.title = "Ubicación de envío"
        mapView.overlays.add(marker)


        mapView.setOnTouchListener { _, event ->
            val geoPoint = mapView.projection.fromPixels(event.x.toInt(), event.y.toInt()) as GeoPoint
            marker.position = geoPoint
            marker.title = "Ubicación seleccionada: ${geoPoint.latitude}, ${geoPoint.longitude}"
            marker.showInfoWindow()
            true
        }

        findViewById<Button>(R.id.btnSelectLocation).setOnClickListener  {
            val selectedLocation = marker.position
            Toast.makeText(this, "Obteniendo dirección...", Toast.LENGTH_SHORT).show()

            getAddressFromLocation(selectedLocation) { address ->
                if (address != null) {
                    val resultIntent = Intent().apply {
                        putExtra(EXTRA_SELECTED_LOCATION, address)
                        putExtra(EXTRA_LATITUDE, selectedLocation.latitude)
                        putExtra(EXTRA_LONGITUDE, selectedLocation.longitude)
                    }
                    setResult(RESULT_OK, resultIntent)
                        Toast.makeText(this, "Ubicación guardada: $address", Toast.LENGTH_LONG).show()
                    finish()
                } else {
                        Toast.makeText(this, "No se pudo obtener la dirección.", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

    private fun getAddressFromLocation(
        geoPoint: GeoPoint,
        callback: (String?) -> Unit
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocation(
                geoPoint.latitude,
                geoPoint.longitude,
                1,
                object : Geocoder.GeocodeListener {
                    override fun onGeocode(addresses: List<Address>) {
                        val address = addresses.firstOrNull()?.getAddressLine(0)
                        runOnUiThread {
                        callback(address)
                    }
                    }

                    override fun onError(errorMessage: String?) {
                        runOnUiThread {
                        callback(null)
                    }
                }
                }
            )
        } else {
            Executors.newSingleThreadExecutor().execute {
                try {
                    val addresses = geocoder.getFromLocation(geoPoint.latitude, geoPoint.longitude, 1)
                    val address = addresses?.firstOrNull()?.getAddressLine(0)
                    runOnUiThread {
                        callback(address)
                    }
        } catch (e: Exception) {
            e.printStackTrace()
                    runOnUiThread {
            callback(null)
        }
    }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }
}
