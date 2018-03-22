package co.edu.usco.uvindexkt

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintSet
import android.view.View
import kotlinx.android.synthetic.main.activity_main_full.*

class MainActivity : AppCompatActivity() {

    val fullSet:ConstraintSet = ConstraintSet()
    val mainSet:ConstraintSet = ConstraintSet()
    val notifiSet:ConstraintSet = ConstraintSet()
    var initial:Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_full)

        //obteniendo las constrains
        //fullSet.clone(this, R.layout.activity_main_full)
        mainSet.clone(this, R.layout.activity_main)
        notifiSet.clone(this, R.layout.activity_main_notifi)
        fullSet.clone(root)

        floaArrow.setOnClickListener{
            if(floaNoti.visibility==View.VISIBLE) {
                floaNoti.visibility = View.GONE
                floaInf.visibility = View.GONE
            }else{
                floaNoti.visibility = View.VISIBLE
                floaInf.visibility = View.VISIBLE
            }

        }
    }
}
