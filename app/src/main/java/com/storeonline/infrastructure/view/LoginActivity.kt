package com.storeonline.infrastructure.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.storeonline.databinding.ActivityLoginBinding
import com.storeonline.infrastructure.repository.AccountRepository

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var accountService: AccountRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        accountService = AccountRepository(this)

        if (accountService.isSessionActive()) {
            navigateToProductList()
        }

        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor ingrese usuario y contraseña", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            when {
                !accountService.doesUserExist(username) -> {
                    Toast.makeText(
                        this,
                        "El Usuario no se encuentra registrado",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                !accountService.authenticateUser(username, password) -> {
                    Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                }

                else -> {
                    navigateToProductList()
                }
            }
        }

        binding.btnCreateAccount.setOnClickListener {
            try {
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(
                    this,
                    "Error al iniciar RegisterActivity: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun navigateToProductList() {
        val intent = Intent(this, ProductListActivity::class.java)
        startActivity(intent)
        finish()
    }
}