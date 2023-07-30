package com.example.myservice

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.myservice.Constant.DATA_ACTION

class MyBroadCastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val keyActionMusic = intent?.getIntExtra(DATA_ACTION, 0)

        val intentToService = Intent(context, MyStartedService::class.java).apply {
            putExtra(DATA_ACTION, keyActionMusic)
        }
        context?.startService(intentToService)
    }
}