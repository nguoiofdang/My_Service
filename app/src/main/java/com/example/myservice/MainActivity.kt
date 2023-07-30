package com.example.myservice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myservice.Constant.KEY_DATE_INTENT
import com.example.myservice.Constant.START_SERVICE
import com.example.myservice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startServiceID.setOnClickListener {
            startService()
        }
        binding.stopServiceID.setOnClickListener {
            stopService()
        }
    }

    private fun startService() {
        val song = Song("New Day", "GiaHuy", R.drawable.ic_notification, R.raw.sound_notification)
        val bundle = Bundle().apply {
            putSerializable("object_song", song)
        }
        val intentService = Intent(this, MyStartedService::class.java).apply {
            putExtra(START_SERVICE, bundle)
        }
        startService(intentService)
    }

    private fun stopService() {
        val intentService = Intent(this, MyStartedService::class.java)
        stopService(intentService)
    }
}