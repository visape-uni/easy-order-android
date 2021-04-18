package edu.uoc.easyorderfront.ui.table

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.uoc.easyorderfront.R
import kotlinx.android.synthetic.main.activity_ocupar_mesa.*

class OcupyTableActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ocupar_mesa)

        prepareUI()
    }

    fun prepareUI() {
        btn_ocupar_mesa.setOnClickListener({
            val codigoMesa = txt_codigo_mesa.text
        })
    }
}