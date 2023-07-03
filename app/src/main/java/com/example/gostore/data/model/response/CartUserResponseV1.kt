package com.example.gostore.data.model.response

data class CartUserResponseV1(
    val id: Int,
    val user_id: Int,
    val product_id: Int,
    val quantity: Int,
    val product_name: String,
    val product_price: Double,
    val brand_product: String,
    val image_url: String,
    val totalPrice: Double,
)
