package com.storeonline.infrastructure.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.storeonline.databinding.ActivityUserManagementBinding
import com.storeonline.domain.service.AccountService

class UserManagementActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserManagementBinding
    private lateinit var accountService: AccountService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)
        accountService = AccountService(this)

        binding.btnDeleteUser.setOnClickListener {
            val username = binding.etUsernameToDelete.text.toString()
            if (accountService.deleteUser(username)) {
                Toast.makeText(this, "Usuario eliminado", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
