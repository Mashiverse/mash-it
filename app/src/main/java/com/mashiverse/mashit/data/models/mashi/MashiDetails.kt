package com.mashiverse.mashit.data.models.mashi

data class MashiDetails(
    val name: String,
    val author: String,
    val description: String,
    val compositeUrl: String,
    val mashiTraits: List<MashiTrait>,
    val productInfo: ProductInfo? = null
)