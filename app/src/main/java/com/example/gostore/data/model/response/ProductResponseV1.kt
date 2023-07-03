package com.example.gostore.data.model.response

import com.google.gson.annotations.SerializedName

data class ProductResponseV1(
    val id: Int,
    val product_name: String,
    val product_price: Double,
    @SerializedName("brand_product") val product_brand: String,
    @SerializedName("image_url") val img_url: String,
    val category_name: String
)
