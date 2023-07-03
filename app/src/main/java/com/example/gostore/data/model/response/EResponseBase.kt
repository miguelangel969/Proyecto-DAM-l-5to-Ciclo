package com.example.gostore.data.model.response

data class EResponseBase<E>(

    val text: String,
    val code: Int,
    val list: List<E>,
    val item: E,

)