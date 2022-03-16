package com.example.favdishnew.model.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.favdishnew.R
import com.example.favdishnew.utils.Constants
import com.example.favdishnew.view.activities.MainActivity

class NotifyWorker(context: Context,workerParams:WorkerParameters):Worker(context,workerParams) {
    override fun doWork(): Result {
        sendNotification()
        return Result.success()
    }

    private fun sendNotification(){
        val notification_id=0;

        val intent=Intent(applicationContext,MainActivity::class.java)
        intent.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra(Constants.NOTIFICATION_ID,notification_id)
        val notificationManager=applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val titleNotification:String="Check Out New Dish Recipes"
        val subTitleNotification:String="Tap for more details"

        val bitmap:Bitmap=BitmapFactory.decodeResource(applicationContext.resources, R.drawable.pizza)

        val bigPicStyle=NotificationCompat.BigPictureStyle()
            .bigPicture(bitmap)
            .bigLargeIcon(null)

        val mainPendingIntent= PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            0
        )

        val notificationCompatBuilder=NotificationCompat.Builder(
            applicationContext,
            Constants.NOTIFICATION_CHANNEL
        )
        notificationCompatBuilder.setStyle(bigPicStyle)
            .setContentTitle(titleNotification)
            .setContentText(subTitleNotification)
            .setSmallIcon(R.drawable.pizza)
            .setLargeIcon(bitmap)
            .setContentIntent(mainPendingIntent)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setContentIntent(mainPendingIntent)
            .setColor(ContextCompat.getColor(applicationContext,R.color.orange))
            .setAutoCancel(true)
            .setCategory(NotificationCompat.CATEGORY_SOCIAL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationCompatBuilder.setChannelId(Constants.NOTIFICATION_CHANNEL)

            // Setup the Ringtone for Notification.
            val ringtoneManager =
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val audioAttributes =
                AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()

            val channel =
                NotificationChannel(
                    Constants.NOTIFICATION_CHANNEL,
                    Constants.NOTIFICATION_NAME,
                    NotificationManager.IMPORTANCE_HIGH
                )

            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            channel.setSound(ringtoneManager, audioAttributes)
            notificationManager.createNotificationChannel(channel)
        }
        // END

        // TODO Step 17: Notify the user with Notification id and Notification builder using the NotificationManager instance that we have created.
        // START
        notificationManager.notify(notification_id, notificationCompatBuilder.build())
        // END
    }





    }
