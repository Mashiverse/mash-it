package com.mashiverse.mashit.data.states.shop

import com.mashiverse.mashit.data.models.sys.data.ShopDataType

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