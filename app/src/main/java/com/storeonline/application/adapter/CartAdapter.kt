package com.storeonline.application.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.storeonline.databinding.ItemCartBinding
import com.storeonline.domain.model.CartItem

class CartAdapter(
    private val cartItems: List<CartItem>,
    private val onQuantityChange: (CartItem, Int) -> Unit,
    private val onRemove: (CartItem) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(private val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cartItem: CartItem) {
            binding.productName.text = cartItem.product.name
            binding.productPrice.text = "$${cartItem.product.price}"
            binding.productQuantity.text = cartItem.quantity.toString()

            Glide.with(binding.root.context)
                .load(cartItem.product.images.firstOrNull())
                .override(100, 100)
                .into(binding.productImage)

            binding.btnIncreaseQuantity.setOnClickListener {
                onQuantityChange(cartItem, cartItem.quantity + 1)
            }

            binding.btnDecreaseQuantity.setOnClickListener {
                if (cartItem.quantity > 1) {
                    onQuantityChange(cartItem, cartItem.quantity - 1)
                }
            }

            binding.btnRemove.setOnClickListener {
                onRemove(cartItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(cartItems[position])
    }

    override fun getItemCount(): Int = cartItems.size
}
