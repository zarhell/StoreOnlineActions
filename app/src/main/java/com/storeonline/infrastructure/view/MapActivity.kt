package com.storeonline.infrastructure.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.storeonline.R
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.util.GeoPoint

class MapActivity : AppCompatActivity() {

    private lateinit var mapView: MapView

    @SuppressLint("MissingInflatedId", "ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        Configuration.getInstance().load(applicationContext, getPreferences(MODE_PRIVATE))

        mapView = findViewById(R.id.mapView)
        mapView.setMultiTouchControls(true)

        val mapController: IMapController = mapView.controller
        mapController.setZoom(15.0)
        val startPoint = GeoPoint(40.7128, -74.0060)
        mapController.setCenter(startPoint)

        val marker = Marker(mapView)
        marker.position = startPoint
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

        findViewById<Button>(R.id.btnSelectLocation).setOnClickListener {
            val selectedLocation = marker.position
            Toast.makeText(this, "Ubicación guardada: ${selectedLocation.latitude}, ${selectedLocation.longitude}", Toast.LENGTH_LONG).show()
            finish()
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
