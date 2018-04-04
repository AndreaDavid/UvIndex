package co.edu.usco.uvindexkt

import android.app.Activity
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.*
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintSet
import android.support.transition.TransitionManager
import android.support.v4.app.NotificationCompat
import android.support.v4.content.LocalBroadcastManager

import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import co.edu.usco.uvindexkt.`interface`.InterfaceRender
import co.edu.usco.uvindexkt.config.Config
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main_full.*
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity(),InterfaceRender {

    val fullSet: ConstraintSet = ConstraintSet()
    val mainSet: ConstraintSet = ConstraintSet()
    val notifiSet: ConstraintSet = ConstraintSet()
    var initial: Boolean = true
    val client = OkHttpClient()
    val PREFS_FILENAME = "co.edu.usco.uvindexkt"
    val UVI_REF = "uvi_ref"
    var prefs: SharedPreferences? = null
    val URL_SERVIDOR:String = "http://192.168.0.114:8784/uvradiation/raspberrycontroller/findLastTrackDataUviInteger"
    var mRegistrationBroadcastReceiver:BroadcastReceiver?=null


    override fun renderizarImg(uvi:Int?) {
        renderImageFromUVI(uvi)
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, IntentFilter("registrationComplete"))
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, IntentFilter(Config.STR_PUSH))
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_full)
        prefs = this.getSharedPreferences(PREFS_FILENAME,0)

        mRegistrationBroadcastReceiver = object:BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                if(intent!!.action == Config.STR_PUSH){
                    val message = intent!!.getStringExtra("message")
                    println("Mensajee"+message)
                    run(URL_SERVIDOR)
                }
            }
        }
        FirebaseMessaging.getInstance().subscribeToTopic("AndroidPushApp")
        //obteniendo las constrains
        //fullSet.clone(this, R.layout.activity_main_full)
        mainSet.clone(this, R.layout.activity_main)
        notifiSet.clone(this, R.layout.activity_main_notifi)
        fullSet.clone(root)

        run(URL_SERVIDOR)

        floaArrow.setOnClickListener {
            TransitionManager.beginDelayedTransition(root)
            if (floaNoti.visibility == View.VISIBLE) {

                floaNoti.visibility = View.GONE
                floaInf.visibility = View.GONE
            } else {

                floaNoti.visibility = View.VISIBLE
                floaInf.visibility = View.VISIBLE
            }

        }
        floaNoti.setOnClickListener {
            TransitionManager.beginDelayedTransition(root)
            if (initial) {
                notifiSet.applyTo(root)
                titleRecomen.text="Notificaciones"
                recomendaciones.text = getString(R.string.notifi)
                //uviNoti.visibility=View.GONE

            }
            else {
                fullSet.applyTo(root)
            }
            println(initial)

            initial = !initial

        }
        btnGuardar.setOnClickListener {
            val editor = prefs!!.edit()
            var edittext:EditText? = null;
            edittext = uviNoti.editText
            if(edittext !=null) {
                val valorCadena:String= edittext.text.toString()
                if(!valorCadena.equals("")) {
                    if(valorCadena.toInt()<12&&valorCadena.toInt()>0) {
                        editor.putInt(UVI_REF, valorCadena.toInt())
                        editor.apply()
                        //editor.commit()
                        val view: View = currentFocus
                        if (view != null) {
                            val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                        this.toast("Se ha guardado la información")
                    }else{
                        this.toast("El valor debe estar entre 1 y 11")
                    }
                }else{
                    this.toast("Ingrese un dato")
                }
            }else{
                this.toast("Ingrese un dato hagame el fa")
            }
        }

        floaInf.setOnClickListener {
            TransitionManager.beginDelayedTransition(root)
            if (initial){
                mainSet.applyTo(root)
                uviNoti.visibility=View.GONE
                btnGuardar.visibility=View.GONE
                run(URL_SERVIDOR)
                titleRecomen.text="Información"
            }
            else {fullSet.applyTo(root)
                uviNoti.visibility=View.VISIBLE
                btnGuardar.visibility=View.VISIBLE
            }


            initial = !initial
        }

    }

    /*fun obtenerUltimoUVIFromServer():Int{

        RestTemplate rt = RestTemplate()
    return 1
    }*/
    fun run(url: String) {
        val request = Request.Builder()
                .url(url)
                .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("error")
            }
            override fun onResponse(call: Call, response: Response) {
                var lectura:Int? =response.body()?.string()?.toInt();
                this@MainActivity.runOnUiThread(Runnable {

                    println(lectura)
                    this@MainActivity.renderImageFromUVI(lectura)
                })


            }
        })
    }
    fun Activity.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, message, duration).show()
    }

    private fun showNotificacion(title:String,message:String?){
        val intent = Intent(applicationContext,MainActivity::class.java)
        val contentIntet = PendingIntent.getActivity(applicationContext,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)
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

    fun renderImageFromUVI(uvi:Int?){
        println(uvi)
        when(uvi){
            1-> {img.setImageResource(R.drawable.uv1)
                recomendaciones.text = "Se presenta un UVI bajo, puede permanecer en el exterior sin necesidad de protecion y no corre ningun riesgo"
            }
            2-> {img.setImageResource(R.drawable.uv2)
                recomendaciones.text = "Se presenta un UVI bajo, puede permanecer en el exterior sin necesidad de protecion y no corre ningun riesgo"
            }
            3->{img.setImageResource(R.drawable.uv3)
                recomendaciones.text ="Se presenta un UVI moderado, mantengase a la sombra durante las horas centrales del dia, apliquese crema de proteccion solar use camisa manga larga y sombrero."
            }
            4->{img.setImageResource(R.drawable.uv4)
                recomendaciones.text ="Se presenta un UVI moderado, mantengase a la sombra durante las horas centrales del dia, apliquese crema de proteccion solar use camisa manga larga y sombrero."
            }
            5->{img.setImageResource(R.drawable.uv5)
                recomendaciones.text ="Se presenta un UVI moderado, mantengase a la sombra durante las horas centrales del dia, apliquese crema de proteccion solar use camisa manga larga y sombrero."
            }
            6->{img.setImageResource(R.drawable.uv6)
                recomendaciones.text ="Se presenta un UVI alto, mantengase a la sombra durante las horas centrales del dia, apliquese crema de proteccion solar use camisa manga larga y sombrero."
            }
            7->{img.setImageResource(R.drawable.uv7)
                recomendaciones.text ="Se presenta un UVI alto, mantengase a la sombra durante las horas centrales del dia, apliquese crema de proteccion solar use camisa manga larga y sombrero."
            }
            8->{img.setImageResource(R.drawable.uv8)
                recomendaciones.text ="Se presenta un UVI muy alto, mantengase a la sombra durante las horas centrales del dia, apliquese crema de proteccion solar use camisa manga larga y sombrero."
            }
            9->{img.setImageResource(R.drawable.uv9)
                recomendaciones.text ="Se presenta un UVI muy alto, mantengase a la sombra durante las horas centrales del dia, apliquese crema de proteccion solar use camisa manga larga y sombrero."
            }
            10->{img.setImageResource(R.drawable.uv10)
                recomendaciones.text ="Se presenta un UVI muy alto, mantengase a la sombra durante las horas centrales del dia, apliquese crema de proteccion solar use camisa manga larga y sombrero."
            }
            11->{img.setImageResource(R.drawable.uv11)
                recomendaciones.text ="Se presenta un UVI extremadamente alto, evite salir durante las horas centrales del dia de 10am a 2pm, busque la sombra en todo momento, es imprescindible que use camisa sombrero y crema de proteccion solar."
            }
        }
    }

}
