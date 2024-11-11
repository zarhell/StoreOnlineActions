package com.storeonline.infrastructure.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.storeonline.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            binding = ActivityLoginBinding.inflate(layoutInflater)
            setContentView(binding.root)
        } catch (e: Exception) {
            Toast.makeText(this, "Error al instanciar el layout: ${e.message}", Toast.LENGTH_LONG).show()
            return
        }

        binding.btnLogin.setOnClickListener {
            try {
                val intent = Intent(this, ProductListActivity::class.java)
                startActivity(intent)
                finish()
            } catch (e: Exception) {
                Toast.makeText(this, "Error al iniciar ProductListActivity: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }

        binding.btnCreateAccount.setOnClickListener {
            try {
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(this, "Error al iniciar RegisterActivity: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}