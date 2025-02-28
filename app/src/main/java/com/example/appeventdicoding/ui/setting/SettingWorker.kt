package com.example.appeventdicoding.ui.setting

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.text.HtmlCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.bumptech.glide.Glide
import com.example.appeventdicoding.R
import com.example.appeventdicoding.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class SettingWorker(context:Context, workerParameters: WorkerParameters) : CoroutineWorker(context, workerParameters) {
    // jalankan dengan coroutine
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val response = ApiConfig.getApiService().getReminder(EVENT_ACTIVE, EVENT_LIMIT).execute()
            Log.d(SETTING_TAG, "Cek Data Responsenya : ${response.message()} + ${response.code()}")
            if(response.isSuccessful) {
                val dataNotification = response.body()?.listEvents ?: emptyList()
                if (dataNotification.isNotEmpty()) {
                    val dataTitle = dataNotification.firstOrNull()?.name ?: " "
                    val description = dataNotification.firstOrNull()?.description ?: " "

                    val dataDescripitionParse = HtmlCompat.fromHtml(
                        description.toString() ?: "",
                        HtmlCompat.FROM_HTML_MODE_LEGACY
                    )

                    val dataImage = dataNotification.firstOrNull()?.imageLogo
                    val url = dataNotification.firstOrNull()?.link
                    showNotification(dataTitle.toString(), dataDescripitionParse.toString(), dataImage.toString(), url.toString())
                }

                Result.success()

            } else{
                Log.d(SETTING_TAG, "Terjadi Kesalahan ${response.message()}")
                Result.failure()
            }

        } catch (e:Exception) {
            Result.retry()
        }
    }

    private fun showNotification(title:String, description:String, imageEvent:String, url:String) {
        val bitmap = Glide.with(applicationContext)
            .asBitmap()
            .load(imageEvent)
            .submit()
            .get()


        val pendingIntent= Intent(Intent.ACTION_VIEW, Uri.parse(url))

        val resultPendingIntent:PendingIntent? = TaskStackBuilder.create(applicationContext)
            .addNextIntentWithParentStack(pendingIntent)
            .getPendingIntent(200,PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE )


        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification:NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setContentTitle(title)
            .setContentText(description)
            .setLargeIcon(bitmap)
            .setContentIntent(resultPendingIntent)
//            .setStyle(NotificationCompat.BigPictureStyle()
//                .bigPicture(bitmap)) // gambar yang dibesarkan
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(description))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            notification.setChannelId(CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(NOTIFICATION_ID, notification.build())

    }

    companion object {
        private const val SETTING_TAG = "setting_view_model"
        const val EVENT_ACTIVE = -1
        const val EVENT_LIMIT = 1
        const val EXTRA_EVENT = "event"
        const val CHANNEL_ID = "channel_id"
        const val NOTIFICATION_ID = 1
        const val CHANNEL_NAME = "event dicoding"
    }
}