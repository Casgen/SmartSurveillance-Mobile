package com.example.smartsurveillance.services

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Binder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.smartsurveillance.R
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SSMessagingService : FirebaseMessagingService() {

    init {
        Log.i("SsMessagingService", "Constructed")
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.i(SSMessagingService::class.toString(), "Refreshed token :: $token")

        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String) {

    }


    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.i(SSMessagingService::class.toString(), "Message :: ${message.notification}")

        val builder = NotificationCompat.Builder(applicationContext, "General")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Person detected!")
            .setContentText("Someone has entered your property!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val name = "Detection channel"
        val description = "Channel for sending notification about detection"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("General", name, importance)
        channel.description = description
        val manager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        NotificationManagerCompat.from(applicationContext).notify(1, builder.build())

    }

}