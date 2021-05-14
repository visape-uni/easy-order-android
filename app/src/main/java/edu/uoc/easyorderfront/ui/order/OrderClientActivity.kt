package edu.uoc.easyorderfront.ui.order

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import edu.uoc.easyorderfront.R
import edu.uoc.easyorderfront.domain.model.Order
import edu.uoc.easyorderfront.ui.adapter.OrderClientAdapter
import edu.uoc.easyorderfront.ui.constants.EasyOrderConstants
import kotlinx.android.synthetic.main.activity_menu_restaurante.toolbar
import kotlinx.android.synthetic.main.activity_order_client.*

class OrderClientActivity : AppCompatActivity() {
    private val TAG = "OrderClientActivity"
    private lateinit var adapter : OrderClientAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_client)

        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        prepareUI()
    }

    fun prepareUI() {
        val order = intent.getSerializableExtra(EasyOrderConstants.ORDER_KEY) as Order
        initRecyclerView()

        btn_confirmar_pedido.text = getString(R.string.haz_tu_pedido_por_x, order.price.toString())
        btn_confirmar_pedido.setOnClickListener({

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