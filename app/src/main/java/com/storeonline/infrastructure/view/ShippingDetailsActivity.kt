package com.storeonline.infrastructure.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.storeonline.databinding.ActivityShippingDetailsBinding
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.storeonline.R
import com.storeonline.domain.model.Location
import com.storeonline.infrastructure.repository.LocationRepository


class ShippingDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShippingDetailsBinding
    private lateinit var selectLocationLauncher: ActivityResultLauncher<Intent>
    private lateinit var locationRepository: LocationRepository
    private var selectedLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShippingDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        locationRepository = LocationRepository(this)

        selectLocationLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                val address = data?.getStringExtra(EXTRA_SELECTED_LOCATION)
                val latitude = data?.getDoubleExtra(EXTRA_LATITUDE, 0.0) ?: 0.0
                val longitude = data?.getDoubleExtra(EXTRA_LONGITUDE, 0.0) ?: 0.0
                val estimatedDelivery = calculateEstimatedDelivery(address)

                val location = Location(
                    address = address ?: "Desconocido",
                    latitude = latitude,
                    longitude = longitude,
                    estimatedDelivery = estimatedDelivery
                )

                val locationId = locationRepository.saveLocation(location)
                selectedLocation = location.copy(id = locationId.toInt())

                binding.tvSelectedLocation.text = getString(R.string.selected_location, address)
                binding.tvEstimatedDelivery.text = getString(R.string.delivery_estimate, estimatedDelivery)
            }
        }

        binding.btnSelectLocation.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            selectLocationLauncher.launch(intent)
        }

        binding.btnProceedToPayment.setOnClickListener {
            if (selectedLocation != null) {
                val intent = Intent(this, PaymentActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Por favor, seleccione una ubicación primero.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun calculateEstimatedDelivery(address: String?): String {
        return if (address != null) {
            if (address.contains("Centro")) "1-2 días" else "3-5 días"
        } else {
            "Sin estimar"
        }
    }

    companion object {
        const val EXTRA_SELECTED_LOCATION = "extra_selected_location"
        const val EXTRA_LATITUDE = "extra_latitude"
        const val EXTRA_LONGITUDE = "extra_longitude"
    }
}
