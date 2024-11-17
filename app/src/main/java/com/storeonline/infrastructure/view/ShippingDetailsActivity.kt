package com.storeonline.infrastructure.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.storeonline.databinding.ActivityShippingDetailsBinding

class ShippingDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShippingDetailsBinding
    private var selectedLocation: String? = null
    private var estimatedDelivery: String = "Sin estimar"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShippingDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSelectLocation.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            startActivityForResult(intent, REQUEST_SELECT_LOCATION)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_SELECT_LOCATION && resultCode == RESULT_OK) {
            selectedLocation = data?.getStringExtra(EXTRA_SELECTED_LOCATION)
            binding.tvSelectedLocation.text = "Ubicación Seleccionada: $selectedLocation"

            estimatedDelivery = calculateEstimatedDelivery(selectedLocation)
            binding.tvEstimatedDelivery.text = "Tiempo estimado de entrega: $estimatedDelivery"
        }
    }

    private fun calculateEstimatedDelivery(location: String?): String {
        return if (location != null) {
            if (location.contains("Centro")) "1-2 días" else "3-5 días"
        } else {
            "Sin estimar"
        }
    }

    companion object {
        const val REQUEST_SELECT_LOCATION = 1
        const val EXTRA_SELECTED_LOCATION = "extra_selected_location"
    }
}
