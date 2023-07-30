package com.example.myservice

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.myservice.Constant.ACTION_CLEAR
import com.example.myservice.Constant.ACTION_PAUSE
import com.example.myservice.Constant.ACTION_PLAY
import com.example.myservice.Constant.CHANEL_ID
import com.example.myservice.Constant.DATA_ACTION
import com.example.myservice.Constant.START_SERVICE

class MyStartedService : Service() {

    private var mediaPlayer: MediaPlayer? = null
    private var mSong: Song? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val bundle = intent?.getBundleExtra(START_SERVICE)
        bundle?.let {
            Log.e("check", "From Activity")
            val song = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getSerializable("object_song", Song::class.java)!!
            } else {
                it.getSerializable("object_song") as Song
            }
            mSong = song.copy()
            startSong(song)
            sendNotification(song, ACTION_PLAY)
        }
        /*val dateNotification = intent?.getStringExtra(KEY_DATE_INTENT)*/

        val keyActionMusic = intent?.getIntExtra(DATA_ACTION, 0)
        keyActionMusic?.let {
            Log.e("check", "From Broadcast")
            handleActionMusic(it)
        }

        return START_NOT_STICKY
    }

    private fun startSong(song: Song) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, song.resource)
        }
        mediaPlayer?.start()
    }

    private fun sendNotification(song: Song, action: Int) {

        /*val bitmap = BitmapFactory.decodeResource(resources, song.image)*/  // dung cho anh kich thuoc lon
        val remoteView = RemoteViews(packageName, R.layout.custom_notification)
        remoteView.setTextViewText(R.id.tvTitleSong, song.title)
        remoteView.setTextViewText(R.id.tvSingleSong, song.single)
        remoteView.setImageViewResource(R.id.imvSong, song.image)
        remoteView.setImageViewResource(R.id.imvPlayOrPauseSong, R.drawable.ic_pause)

        when (action) {
            ACTION_PLAY -> {
                remoteView.setOnClickPendingIntent(
                    R.id.imvPlayOrPauseSong, getPendingIntent(
                        ACTION_PAUSE
                    )
                )
                remoteView.setImageViewResource(R.id.imvPlayOrPauseSong, R.drawable.ic_pause)
            }

            ACTION_PAUSE -> {
                remoteView.setOnClickPendingIntent(
                    R.id.imvPlayOrPauseSong, getPendingIntent(
                        ACTION_PLAY
                    )
                )
                remoteView.setImageViewResource(R.id.imvPlayOrPauseSong, R.drawable.ic_play)
            }
        }

        remoteView.setOnClickPendingIntent(R.id.imvClearSong, getPendingIntent(ACTION_CLEAR))

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, CHANEL_ID)
            .setSmallIcon(song.image)
            .setSound(null)
            .setCustomContentView(remoteView)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(1, notification)
    }

    private fun getPendingIntent(action: Int): PendingIntent {
        val intent = Intent(this, MyBroadCastReceiver::class.java).apply {
            putExtra(DATA_ACTION, action)
        }
        return PendingIntent.getBroadcast(
            this,
            action,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun handleActionMusic(action: Int) {
        when (action) {
            ACTION_PAUSE -> {
                mediaPlayer?.pause()
                mSong?.let {
                    sendNotification(it, ACTION_PAUSE)
                }
            }

            ACTION_PLAY -> {
                mediaPlayer?.start()
                mSong?.let {
                    sendNotification(it, ACTION_PLAY)
                }
            }

            ACTION_CLEAR -> {
                stopSelf()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}