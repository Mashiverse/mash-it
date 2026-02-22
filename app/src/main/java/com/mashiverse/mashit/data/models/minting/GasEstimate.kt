package com.mashiverse.mashit.data.models.minting

data class GasEstimate(
    val gasLimit: String,
    val maxFeePerGas: String,
    val maxPriorityFeePerGas: String
)