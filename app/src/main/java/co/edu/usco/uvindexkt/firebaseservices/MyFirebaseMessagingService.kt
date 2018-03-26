package co.edu.usco.uvindexkt.firebaseservices

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.support.v4.content.LocalBroadcastManager
import co.edu.usco.uvindexkt.MainActivity
import co.edu.usco.uvindexkt.R
import co.edu.usco.uvindexkt.config.Config
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService:FirebaseMessagingService(){
    override fun onMessageReceived(remoteMessage: RemoteMessage?) {

        showNotificacion(remoteMessage!!.data!!.getValue("body"),remoteMessage!!.data!!.getValue("body"))
        handleRenderLayout(remoteMessage!!.data!!.getValue("body"))
    }


    fun handleRenderLayout(body:String?){
        val pushNotification = Intent(Config.STR_PUSH)
        pushNotification.putExtra("message",body)
        LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification)
    }

    private fun showNotificacion(title:String,message:String?){
        val intent = Intent(applicationContext, MainActivity::class.java)
        val contentIntet = PendingIntent.getActivity(applicationContext,0,intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val builder = NotificationCompat.Builder(applicationContext,"hola")
        builder.setAutoCancel(true).setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.uv8)

                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(contentIntet)
        val notificationManager = baseContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(1,builder.build())
    }
}