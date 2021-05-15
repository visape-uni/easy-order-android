package edu.uoc.easyorderfront.ui.order

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import edu.uoc.easyorderfront.R
import edu.uoc.easyorderfront.domain.model.Order
import edu.uoc.easyorderfront.ui.adapter.OrderClientAdapter
import edu.uoc.easyorderfront.ui.constants.EasyOrderConstants
import edu.uoc.easyorderfront.ui.constants.UIMessages.ERROR_HACIENDO_PEDIDO
import edu.uoc.easyorderfront.ui.utils.Status
import kotlinx.android.synthetic.main.activity_menu_restaurante.toolbar
import kotlinx.android.synthetic.main.activity_order_client.*
import org.koin.android.viewmodel.ext.android.viewModel

class OrderClientActivity : AppCompatActivity() {
    private val TAG = "OrderClientActivity"
    private lateinit var adapter : OrderClientAdapter

    private val viewModel: OrderClientViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_client)

        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        prepareUI()
    }

    fun prepareUI() {
        initRecyclerView()

        viewModel.orderSaved.observe(this, { dataWrapper ->
            when(dataWrapper.status) {
                Status.LOADING -> {
                    progress_bar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    //TODO: ABRIR PANTALLA DE ESPERA
                    progress_bar.visibility = View.GONE

                    startActivity(Intent(this, WaitScreenActivity::class.java))
                }
                Status.ERROR -> {
                    progress_bar.visibility = View.GONE
                    Toast.makeText(this, ERROR_HACIENDO_PEDIDO, Toast.LENGTH_LONG).show()
                }
            }
        })

        val order = intent.getSerializableExtra(EasyOrderConstants.ORDER_KEY) as Order
        val price = String.format("%.2f", order.price)
        btn_confirmar_pedido.text = getString(R.string.haz_tu_pedido_por_x, price)
        btn_confirmar_pedido.setOnClickListener({
            order.price = price.toDouble()
            val tableId = intent.getStringExtra(EasyOrderConstants.TABLE_ID_KEY)
            viewModel.saveOrder(tableId, order)
        })

        if (!order.orderedDishes.isNullOrEmpty()) {
            adapter.submitList(order.orderedDishes)
        } else {
            if (adapter.currentList.isEmpty()) {
                //TODO: MOSTRAR MENSAJE DE ERROR
            }
        }
    }

    fun initRecyclerView() {

        // Set Layout Manager
        recycler_vw_comanda.layoutManager = LinearLayoutManager(applicationContext)

        adapter = OrderClientAdapter()
        // Set Adapter
        recycler_vw_comanda.adapter = adapter
    }

}