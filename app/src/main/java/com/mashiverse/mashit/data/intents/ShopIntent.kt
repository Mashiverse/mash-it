package com.mashiverse.mashit.data.intents

import com.mashiverse.mashit.data.models.ShopDataType

sealed class ShopIntent {

    data class OnDataTypeSelect(
        val dataType: ShopDataType,
        val query: String
    ) : ShopIntent()

    data object OnCategorySelect : ShopIntent()

    data object OnCategoryClose : ShopIntent()

    data class OnNftSelect(
        val id: String
    ) : ShopIntent()

    data object OnNftDeselect : ShopIntent()
}