package com.mashiverse.mashit.sys.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class UploadWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {

    }

    companion object {
        const val WALLET = "wallet"
        const val IMG_TYPE = "img_type"
    }
}