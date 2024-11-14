package com.storeonline.infrastructure.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.storeonline.databinding.ActivityPaymentBinding
import com.storeonline.domain.model.PaymentDetails

class PaymentActivity : AppCompatActivity() {
    private lateinit var activityPaymentbinding: ActivityPaymentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityPaymentbinding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(activityPaymentbinding.root)

        activityPaymentbinding.btnConfirmPayment.setOnClickListener {
            val paymentDetails = gatherPaymentDetails()
            if (paymentDetails != null) {
                processPayment(paymentDetails)
            } else {
                Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show()
            }
        }

        activityPaymentbinding.btnSelectLocation.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }
    }

    private fun gatherPaymentDetails(): PaymentDetails? {
        val cardNumber = activityPaymentbinding.etCardNumber.text.toString()
        val cardholderName = activityPaymentbinding.etCardholderName.text.toString()
        val expiryDate = activityPaymentbinding.etExpiryDate.text.toString()
        val cvv = activityPaymentbinding.etCvv.text.toString()
        val shippingAddress = activityPaymentbinding.etShippingAddress.text.toString()
        val city = activityPaymentbinding.etCity.text.toString()
        val postalCode = activityPaymentbinding.etPostalCode.text.toString()
        val country = activityPaymentbinding.etCountry.text.toString()

        return if (cardNumber.isNotEmpty() && cardholderName.isNotEmpty() && expiryDate.isNotEmpty() &&
            cvv.isNotEmpty() && shippingAddress.isNotEmpty() && city.isNotEmpty() && postalCode.isNotEmpty() && country.isNotEmpty()
        ) {
            PaymentDetails(cardNumber, cardholderName, expiryDate, cvv, shippingAddress, city, postalCode, country)
        } else {
            null
        }
    }

    private fun processPayment(paymentDetails: PaymentDetails) {
        Toast.makeText(this, "Pago confirmado. Detalles de envío registrados.", Toast.LENGTH_LONG).show()
        finish()
    }
}
