package co.edu.usco.uvindexkt.firebaseservices

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

class MyFIrebaseIdService: FirebaseInstanceIdService(){
    override  fun onTokenRefresh() {
        super.onTokenRefresh()
        val refreshedToken = FirebaseInstanceId.getInstance().token
        sendRegistrationToServer(refreshedToken!!)

    }

    private fun sendRegistrationToServer(refreshedToken:String){
        Log.d("Debug",refreshedToken)
    }

}