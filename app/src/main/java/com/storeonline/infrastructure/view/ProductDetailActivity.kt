package com.storeonline.infrastructure.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.storeonline.R
import com.storeonline.databinding.ActivityProductDetailBinding
import com.storeonline.domain.model.Product

class ProductDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductDetailBinding;

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailBinding.inflate(layoutInflater);
        setContentView(binding.root);

        val product: Product? = intent.getParcelableExtra("selectedProduct", Product::class.java);
        product?.let {
            binding.productName.text = it.name;
            binding.productPrice.text = getString(R.string.product_price_format, it.price);

        }

        binding.btnAddToCart.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java);
            product?.let { product ->
                intent.putParcelableArrayListExtra("selectedProducts", arrayListOf(product));
            }
            startActivity(intent);
        }
    }
}