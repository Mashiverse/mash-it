package com.mashiverse.mashit.data.repos.drops

import com.mashiverse.mashit.data.models.drops.toDrops
import com.mashiverse.mashit.data.remote.apis.MashitApi
import javax.inject.Inject

class DropsRepo @Inject constructor(
    private val mashitApi: MashitApi
) {

    suspend fun getSpecialDropsList() = mashitApi.getSpecialDrops().toDrops()
}