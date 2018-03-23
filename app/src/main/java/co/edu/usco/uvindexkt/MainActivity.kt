package co.edu.usco.uvindexkt

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintSet
import android.support.transition.TransitionManager
import android.view.View
import kotlinx.android.synthetic.main.activity_main_full.*
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    val fullSet: ConstraintSet = ConstraintSet()
    val mainSet: ConstraintSet = ConstraintSet()
    val notifiSet: ConstraintSet = ConstraintSet()
    var initial: Boolean = true
    val client = OkHttpClient()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_full)

        //obteniendo las constrains
        //fullSet.clone(this, R.layout.activity_main_full)
        mainSet.clone(this, R.layout.activity_main)
        notifiSet.clone(this, R.layout.activity_main_notifi)
        fullSet.clone(root)
        run("https://api.github.com/users/Evin1-/repos")

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
                //uviNoti.visibility=View.GONE

            }
            else {
                fullSet.applyTo(root)
            }
            println(initial)

            initial = !initial

        }

        floaInf.setOnClickListener {
            TransitionManager.beginDelayedTransition(root)
            if (initial){
                mainSet.applyTo(root)
                uviNoti.visibility=View.GONE
                titleRecomen.text="Información"
            }
            else {fullSet.applyTo(root)
                uviNoti.visibility=View.VISIBLE
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
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) = println(response.body()?.string())
        })
    }
}
