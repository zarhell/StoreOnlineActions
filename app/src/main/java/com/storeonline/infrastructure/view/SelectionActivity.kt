package com.storeonline.infrastructure.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.storeonline.databinding.ActivitySelectionBindingBinding


class SelectionActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySelectionBindingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectionBindingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBuy.setOnClickListener {
            navigateToProductList()
        }

        binding.btnSell.setOnClickListener {
            navigateToAddProduct()
        }
    }

    private fun navigateToProductList() {
        val intent = Intent(this, ProductListActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToAddProduct() {
        val intent = Intent(this, AddProductActivity::class.java)
        startActivity(intent)
    }
}
