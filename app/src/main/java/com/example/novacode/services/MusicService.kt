package com.example.novacode.services

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import com.example.novacode.R

class MusicService : Service() {
    private var mediaPlayer: MediaPlayer? = null
    
    override fun onBind(intent: Intent?): IBinder? = null
    
    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer.create(this, R.raw.background_music)
        mediaPlayer?.isLooping = true
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            "PLAY" -> mediaPlayer?.start()
            "PAUSE" -> mediaPlayer?.pause()
            "STOP" -> {
                mediaPlayer?.stop()
                mediaPlayer?.prepare()
            }
        }
        return START_STICKY
    }
    
    override fun onDestroy() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        super.onDestroy()
    }
} 