package co.edu.usco.uvindexkt.firebaseservices

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v4.app.NotificationCompat
import android.support.v4.content.LocalBroadcastManager
import co.edu.usco.uvindexkt.MainActivity
import co.edu.usco.uvindexkt.R
import co.edu.usco.uvindexkt.config.Config
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage



class MyFirebaseMessagingService:FirebaseMessagingService(){

    var prefs: SharedPreferences? = null
    val PREFS_FILENAME = "co.edu.usco.uvindexkt"
    val UVI_REF = "uvi_ref"
    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        prefs = this.getSharedPreferences(PREFS_FILENAME,0)
        if(prefs!=null) {
            val uviReferencia: Int = prefs!!.getInt(UVI_REF, 0)
            val uviEntrante:Float = remoteMessage!!.data!!.getValue("body").toFloat()
            if(uviReferencia!=0&&uviEntrante>=uviReferencia) {
                showNotificacion("Alerta UVI", remoteMessage!!.data!!.getValue("body"))

            }
        }
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
                .setContentIntent(contentIntet)

        when(message?.toInt()) {
            1 -> {
                builder.setSmallIcon(R.drawable.uv1)
                        .setContentTitle(title)
                        .setContentText("Se presenta un UVI de 1")
            }
            2 -> {
                builder.setSmallIcon(R.drawable.uv2)
                        .setContentTitle(title)
                        .setContentText("Se presenta un UVI de 2")
            }
            3 -> {
                builder.setSmallIcon(R.drawable.uv3)
                        .setContentTitle(title)
                        .setContentText("Se presenta un UVI de 3")
            }
            4 -> {
                builder.setSmallIcon(R.drawable.uv4)
                        .setContentTitle(title)
                        .setContentText("Se presenta un UVI de 4")
            }
            5 -> {
                builder.setSmallIcon(R.drawable.uv5)
                        .setContentTitle(title)
                        .setContentText("Se presenta un UVI de 5")

            }
            6 -> {
                builder.setSmallIcon(R.drawable.uv6)
                        .setContentTitle(title)
                        .setContentText("Se presenta un UVI de 6")
            }
            7 -> {
                builder.setSmallIcon(R.drawable.uv7)
                        .setContentTitle(title)
                        .setContentText("Se presenta un UVI de 7")
            }
            8 -> {
                builder.setSmallIcon(R.drawable.uv8)
                        .setContentTitle(title)
                        .setContentText("Se presenta un UVI de 8")

            }
            9 -> {
                builder.setSmallIcon(R.drawable.uv9)
                        .setContentTitle(title)
                        .setContentText("Se presenta un UVI de 9")

            }
            10 -> {
                builder.setSmallIcon(R.drawable.uv10)
                        .setContentTitle(title)
                        .setContentText("Se presenta un UVI de 10")
            }
            11 -> {
                builder.setSmallIcon(R.drawable.uv11)
                        .setContentTitle(title)
                        .setContentText("Se presenta un UVI de 11")
            }
        }
        val notificationManager = baseContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1,builder.build())
    }
}