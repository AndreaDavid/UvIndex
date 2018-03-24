package co.edu.usco.uvindexkt.firebaseservices

import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import co.edu.usco.uvindexkt.config.Config
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * Created by JULIAN on 24/03/201e8.
 */
class MyFirebaseMessagingService:FirebaseMessagingService(){
    override fun onMessageReceived(remoteMessage: RemoteMessage?) {

        handleNotification(remoteMessage!!.notification!!.body)
    }

    fun handleNotification(body:String?){
        val pushNotification = Intent(Config.STR_PUSH)
        pushNotification.putExtra("message",body)
        LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification)
    }
}