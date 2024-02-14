package com.example.flo

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.flo.data.entities.Song
import com.example.flo.ui.song.SongActivity
import com.google.gson.Gson

class Foreground : Service() {


    val CHANNEL_ID = "FLO123"
    val NOTI_ID = 153

    fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            val serviceChannel = NotificationChannel(CHANNEL_ID, "FOREGROUND", NotificationManager.IMPORTANCE_DEFAULT)
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        var song = Gson(). fromJson(intent?.getStringExtra("song"), Song::class.java)
        val afterIntent: Intent = Intent(this, SongActivity::class.java)
        afterIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val pendingIntent = PendingIntent.getActivity(this,0,afterIntent, PendingIntent.FLAG_ONE_SHOT)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_flo_app_logo)
            .setContentTitle("FLO")
            .setContentTitle(song.title)
            .setContentText(song.singer)
            .setOngoing(true)
            .setContentIntent(pendingIntent)
            .setProgress(100, 0, false)

        Thread(object: Runnable{
            override fun run() {
                var count = 1
                try {
                    while (count <=100){
                        Log.d("count", count.toString())
                        count += 1
                        Thread.sleep(100)
                        notification.setProgress(100, count, false)
                        NotificationManagerCompat.from(this@Foreground).notify(NOTI_ID, notification.build())
                    }
                    Log.d("count", "finish")
                    notification.setContentText("Finish").setProgress(0,0,false)
                    NotificationManagerCompat.from(this@Foreground).notify(NOTI_ID, notification.build())
                }catch (e: InterruptedException){
                    Log.d("count", "error")
                }
            }
        }).start()
        startForeground(NOTI_ID, notification.build())
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder {
        return Binder()
    }
}
