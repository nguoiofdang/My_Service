package com.example.myservice

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.example.myservice.Constant.CHANEL_ID

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        createNotificationChanel()
    }

    private fun createNotificationChanel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importantChanel = NotificationManager.IMPORTANCE_DEFAULT
            val nameChanel = "Notification chanel example"
            val descriptionChanel = "Description chanel"
            val notificationChannel =
                NotificationChannel(CHANEL_ID, nameChanel, importantChanel).apply {
                    description = descriptionChanel
                    setSound(null, null)
                }
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}