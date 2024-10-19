package com.storeonline

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.storeonline.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Botón para finalizar el registro (sin funcionalidad en este ejemplo)
        binding.btnSubmit.setOnClickListener {
            // Puedes agregar lógica aquí para manejar el registro
        }
    }
}