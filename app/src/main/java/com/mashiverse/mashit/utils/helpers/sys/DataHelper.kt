package com.mashiverse.mashit.utils.helpers.sys

import androidx.paging.PagingData
import androidx.paging.filter
import androidx.paging.map
import com.mashiverse.mashit.data.models.mashi.Nft
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun Flow<PagingData<Nft>>.filter(
    isAvailableOnly: Boolean,
): Flow<PagingData<Nft>> = this.map { pagingData ->
    if (!isAvailableOnly) {
        pagingData
    } else {
        pagingData
            // 1. Map to a Pair (Item, KeepBoolean) to avoid Nft?
            .map { item ->
                val isDelisted = item.productInfo?.delisted ?: false
                val soldQty = item.productInfo?.soldQuantity ?: -1
                val totalQty = item.productInfo?.quantity ?: -1

                if (isDelisted) {
                    item to false
                } else {
                    item to (soldQty < totalQty)
                }
            }
            // 2. Filter based on the boolean in the pair
            .filter { pair -> pair.second }
            // 3. Map back to just the Item
            .map { pair -> pair.first }
    }
}