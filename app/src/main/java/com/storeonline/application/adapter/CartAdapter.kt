package com.storeonline.application.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.storeonline.databinding.ItemProductBinding
import com.storeonline.domain.model.Product

class CartAdapter(
    private var products: List<Product>
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val product = products[position]
        holder.binding.tvProductName.text = product.name
        holder.binding.tvProductPrice.text = "$${product.price}"
    }

    override fun getItemCount() = products.size

    // Método para actualizar la lista de productos en el carrito
    fun updateList(newProducts: List<Product>) {
        products = newProducts
        notifyDataSetChanged()
    }
}
