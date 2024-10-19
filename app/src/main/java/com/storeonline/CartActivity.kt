package com.storeonline

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.storeonline.databinding.ActivityCartBinding

class CartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Usamos la función de extensión para obtener los productos seleccionados
        val selectedProducts: ArrayList<Product>? = intent.parcelableArrayList("selectedProducts")

        if (selectedProducts != null) {
            // Mostrar los productos seleccionados (puedes mejorarlo usando un RecyclerView)
            selectedProducts.forEach { product ->
                Toast.makeText(this, "${product.name} - $${product.price}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
