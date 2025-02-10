package com.example.utils.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.utils.Notification

class TestWorker(private val appContext: Context, private val params: WorkerParameters) :
    CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        Log.i("TAG", "doWork: ................")
        Notification(appContext).sendNotification("fsdfg","fsfsr")
        return Result.success()
    }



}