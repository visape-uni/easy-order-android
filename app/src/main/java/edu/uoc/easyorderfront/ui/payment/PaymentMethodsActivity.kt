package edu.uoc.easyorderfront.ui.payment

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import edu.uoc.easyorderfront.R
import edu.uoc.easyorderfront.data.SessionManager
import edu.uoc.easyorderfront.domain.model.Order
import edu.uoc.easyorderfront.ui.constants.EasyOrderConstants.STRIPE_KEY
import edu.uoc.easyorderfront.ui.utils.Status
import kotlinx.android.synthetic.main.activity_payment_methods.*
import org.koin.android.viewmodel.ext.android.viewModel

class PaymentMethodsActivity : AppCompatActivity() {

    private val viewModel: PaymentMethodsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_methods)

        PaymentConfiguration.init(this, STRIPE_KEY)

        viewModel.paymentSheet = PaymentSheet(this) { result ->
            onPaymentSheetResult(result)
        }

        //val order = intent.getSerializableExtra(EasyOrderConstants.ORDER_KEY) as Order



        prepareUI()
    }

    fun prepareUI() {
        btn_pagar_paypal.isEnabled = false

        viewModel.paymentResponse.observe(this, { dataWrapper ->
            when(dataWrapper.status) {
                Status.LOADING -> {

                }
                Status.SUCCESS -> {
                    btn_pagar_paypal.isEnabled = true
                }
                Status.ERROR -> {

                }
            }
        })


        btn_pagar_paypal.setOnClickListener({
            presentPaymentSheet()
        })

        val order = SessionManager(applicationContext).getOrder() as Order
        viewModel.fetchInitData(order)
    }

    fun presentPaymentSheet() {
        val paymentResponse = viewModel.paymentResponse.value?.data!!
        viewModel.paymentSheet.presentWithPaymentIntent(
            paymentResponse.paymentIntent,
            PaymentSheet.Configuration(
                "RESTAURAAANT NAME",
                PaymentSheet.CustomerConfiguration(
                    paymentResponse.customer,
                    paymentResponse.ephemeralKey
                )
            )
        )
    }

    fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
        if (paymentSheetResult is PaymentSheetResult.Canceled) {
            Toast.makeText(
                this,
                "Payment Canceled",
                Toast.LENGTH_LONG
            ).show()
        } else if (paymentSheetResult is PaymentSheetResult.Failed) {
            Toast.makeText(
                this,
                "Payment Failed",
                Toast.LENGTH_LONG
            ).show()
        } else if (paymentSheetResult is PaymentSheetResult.Completed) {
            Toast.makeText(
                this,
                "Payment Complete",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}