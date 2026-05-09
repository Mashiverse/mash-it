package com.mashiverse.mashit.data.repos.mashit

import com.mashiverse.mashit.data.models.mashi.ProductInfo
import com.mashiverse.mashit.data.models.mashi.mappers.toProductInfoList
import com.mashiverse.mashit.data.remote.apis.MashitApi
import javax.inject.Inject

//class SoldQtyRepo @Inject constructor(
//    private val mashitApi: MashitApi
//) {
//
//    suspend fun getItemsSoldQty(ids: List<String>): List<ProductInfo> {
//        val productInfo: MutableList<ProductInfo> = mutableListOf()
//
//        try {
//            if (ids.size <= 10) {
//                val batchString = ids.joinToString(",")
//                val productInfos = mashitApi.getItemsSoldQty(commaSeparatedIds = batchString).toProductInfoList()
//                return productInfos
//            }
//
//            val firstBatch = ids.take(10).joinToString(",")
//            val firstRes = mashitApi.getItemsSoldQty(commaSeparatedIds = firstBatch).toProductInfoList()
//            productInfo.addAll(firstRes)
//
//            val secondBatch = ids.drop(10).joinToString(",")
//            val secondRes = mashitApi.getItemsSoldQty(commaSeparatedIds = secondBatch).toProductInfoList()
//            productInfo.addAll(secondRes)
//
//            return productInfo
//        } catch (e: Exception) {
//            return emptyList()
//        }
//    }
//}