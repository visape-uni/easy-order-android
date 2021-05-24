package edu.uoc.easyorderfront.ui.order

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import edu.uoc.easyorderfront.R
import edu.uoc.easyorderfront.ui.constants.EasyOrderConstants
import edu.uoc.easyorderfront.ui.payment.PaymentMethodActivity
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

        btn_pagar.setOnClickListener({

            val tableId = intent.getStringExtra(EasyOrderConstants.TABLE_ID_KEY)

            val intent = Intent(this, PaymentMethodActivity::class.java)
            intent.putExtra(EasyOrderConstants.TABLE_ID_KEY, tableId)
            startActivity(intent)
        })
    }
}