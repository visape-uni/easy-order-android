package edu.uoc.easyorderfront.ui.payment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import edu.uoc.easyorderfront.R
import edu.uoc.easyorderfront.ui.constants.EasyOrderConstants
import edu.uoc.easyorderfront.ui.main.MainClientMenuActivity
import edu.uoc.easyorderfront.ui.utils.Status
import kotlinx.android.synthetic.main.activity_waiting_bill.*
import org.koin.android.viewmodel.ext.android.viewModel

class WaitingBillActivity : AppCompatActivity() {
    private val viewModel: WaitingBillViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waiting_bill)
        prepareUI()
    }

    fun prepareUI() {
        viewModel.paid.observe(this, { dataWrapper ->
            when (dataWrapper.status) {
                Status.LOADING -> {
                    progress_bar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    progress_bar.visibility = View.GONE
                    if (dataWrapper.data?.state != null
                            && dataWrapper.data.state.equals(EasyOrderConstants.PAID_TABLE_STATE)) {
                        val tableId = intent.getStringExtra(EasyOrderConstants.TABLE_ID_KEY)
                        viewModel.endOrder(tableId)
                    } else {
                        Toast.makeText(applicationContext, "La cuenta aun no esta pagada", Toast.LENGTH_LONG).show()
                    }
                }
                Status.ERROR -> {
                    progress_bar.visibility = View.GONE
                    Toast.makeText(applicationContext, dataWrapper.message, Toast.LENGTH_LONG).show()
                }
            }
        })

        viewModel.tableClosed.observe(this, { dataWrapper ->
            when (dataWrapper.status) {
                Status.LOADING -> {
                    progress_bar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    progress_bar.visibility = View.GONE

                    val intent = Intent(applicationContext, MainClientMenuActivity::class.java)
                    intent.putExtra(EasyOrderConstants.FRAGMENT_KEY, EasyOrderConstants.CLIENT_PROFILE_FRAGMENT)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                }
                Status.ERROR -> {
                    progress_bar.visibility = View.GONE
                    Toast.makeText(applicationContext, dataWrapper.message, Toast.LENGTH_LONG).show()
                }
            }
        })


        btn_pagado.setOnClickListener({
            val tableId = intent.getStringExtra(EasyOrderConstants.TABLE_ID_KEY)
            viewModel.getOrder(tableId)
        })
    }
}