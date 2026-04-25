package com.mashiverse.mashit.utils.helpers.sys

import androidx.paging.PagingData
import androidx.paging.filter
import androidx.paging.map
import com.mashiverse.mashit.data.models.mashi.Nft
import com.mashiverse.mashit.data.states.web3.Web3Intent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeout
import kotlin.coroutines.resume

fun Flow<PagingData<Nft>>.fetchSoldQty(
    processWeb3Intent: (Web3Intent) -> Unit
): Flow<PagingData<Nft>> = this.map { pagingData ->
    pagingData.map { item ->
        val listingId = item.productInfo?.listingId?.toInt() ?: -1
        val soldQty = try {
            withTimeout(3000L) {
                suspendCancellableCoroutine<Int> { cont ->
                    processWeb3Intent.invoke(
                        Web3Intent.OnTotalSoldGet(listingId) { v ->
                            if (cont.isActive) cont.resume(v)
                        }
                    )
                }
            }
        } catch (e: Exception) {
            null // Fallback
        }
        item.copy(soldQty = soldQty)
    }
}


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
                val soldQty = item.soldQty ?: -1
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