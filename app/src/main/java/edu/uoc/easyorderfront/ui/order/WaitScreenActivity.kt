package edu.uoc.easyorderfront.ui.order

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import edu.uoc.easyorderfront.R
import edu.uoc.easyorderfront.ui.constants.UIMessages
import edu.uoc.easyorderfront.ui.payment.PaymentMethodsActivity
import kotlinx.android.synthetic.main.activity_pantalla_espera.*

class WaitScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_espera)

        supportActionBar?.title = UIMessages.TITLE_WAITING_SCREEN

        prepareUI()
    }

    private fun prepareUI() {
        btn_editar_pedido.setOnClickListener({
            finish()
        })
        btn_pagar.setOnClickListener({
            val intent = Intent(this, PaymentMethodsActivity::class.java)
            startActivity(intent)
        })
    }
}