package com.example.myservice

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class MyWorkManager(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {

    override fun doWork(): Result {
        TODO("Not yet implemented")
    }

}