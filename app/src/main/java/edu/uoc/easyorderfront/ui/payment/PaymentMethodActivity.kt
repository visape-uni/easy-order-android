package edu.uoc.easyorderfront.ui.payment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import edu.uoc.easyorderfront.R
import edu.uoc.easyorderfront.ui.constants.EasyOrderConstants
import edu.uoc.easyorderfront.ui.utils.Status
import kotlinx.android.synthetic.main.activity_metodo_pago.*
import org.koin.android.viewmodel.ext.android.viewModel

class PaymentMethodActivity : AppCompatActivity() {
    private val viewModel: PaymentMethodViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_metodo_pago)

        prepareUI()
    }

    fun prepareUI() {
        viewModel.pay.observe(this, { dataWrapper ->
            when (dataWrapper.status) {
                Status.LOADING ->{
                    progress_bar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    progress_bar.visibility = View.GONE
                    val tableId = intent.getStringExtra(EasyOrderConstants.TABLE_ID_KEY)
                    val int = Intent(this, WaitingBillActivity::class.java)
                    int.putExtra(EasyOrderConstants.TABLE_ID_KEY, tableId)
                    startActivity(int)
                }
                Status.ERROR -> {
                    progress_bar.visibility = View.GONE
                    Toast.makeText(applicationContext, dataWrapper.message, Toast.LENGTH_LONG).show()
                }
            }
        })

        //val order = intent.getSerializableExtra(EasyOrderConstants.ORDER_KEY)

        btn_pagar_bizum.isEnabled = false
        btn_pagar_tarjeta.isEnabled = false
        btn_pagar_paypal.isEnabled = false

        btn_pagar_efectivo.setOnClickListener({
            val tableId = intent.getStringExtra(EasyOrderConstants.TABLE_ID_KEY)
            viewModel.askForTheBill(tableId)
        })
    }
}