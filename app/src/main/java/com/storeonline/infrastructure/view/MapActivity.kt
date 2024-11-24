package com.storeonline.infrastructure.view

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
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
    private lateinit var mapController: IMapController

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
        mapController = mapView.controller
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

        findViewById<Button>(R.id.btnZoomIn).setOnClickListener {
            mapController.zoomIn()
        }

        findViewById<Button>(R.id.btnZoomOut).setOnClickListener {
            mapController.zoomOut()
        }

        findViewById<Button>(R.id.btnSelectLocation).setOnClickListener {
            val selectedLocation = marker.position
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

        setupSearchBar(marker)
    }

    private fun setupSearchBar(marker: Marker) {
        val searchBar = findViewById<EditText>(R.id.searchBar)

        searchBar.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = searchBar.text.toString()
                if (query.isNotEmpty()) {
                    searchLocation(query, marker)
                }
                true
            } else {
                false
            }
        }
    }

    private fun searchLocation(query: String, marker: Marker) {
        Executors.newSingleThreadExecutor().execute {
            try {
                val addresses = geocoder.getFromLocationName(query, 1)
                if (addresses != null && addresses.isNotEmpty()) {
                    val address = addresses.first()
                    val geoPoint = GeoPoint(address.latitude, address.longitude)
                    runOnUiThread {
                        marker.position = geoPoint
                        marker.title = "Ubicación encontrada: ${address.getAddressLine(0)}"
                        marker.showInfoWindow()
                        mapController.setCenter(geoPoint)
                        Toast.makeText(this, "Ubicación encontrada", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this, "No se encontraron resultados para: $query", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this, "Error al buscar la ubicación", Toast.LENGTH_SHORT).show()
                }
            }
        }
        }

    private fun getAddressFromLocation(
        geoPoint: GeoPoint,
        callback: (String?) -> Unit
    ) {
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

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }
}
