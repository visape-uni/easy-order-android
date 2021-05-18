package edu.uoc.easyorderfront.ui.order

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import edu.uoc.easyorderfront.R
import kotlinx.android.synthetic.main.activity_pantalla_espera.*

class WaitScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_espera)

        prepareUI()
    }

    private fun prepareUI() {
        btn_editar_pedido.setOnClickListener({
            finish()
        })
    }
}