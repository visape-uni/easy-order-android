package edu.uoc.easyorderfront.ui.order

import android.app.AlertDialog
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
import edu.uoc.easyorderfront.ui.constants.EasyOrderConstants.ORDER_KEY
import edu.uoc.easyorderfront.ui.constants.UIMessages
import edu.uoc.easyorderfront.ui.constants.UIMessages.ERROR_HACIENDO_PEDIDO
import edu.uoc.easyorderfront.ui.menu.MenuRestaurantActivity
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
        supportActionBar?.title = UIMessages.TITLE_ORDER_CLIENT

        prepareUI()
    }

    override fun onResume() {
        updateOrder()
        super.onResume()
    }



    fun prepareUI() {
        initRecyclerView()

        viewModel.orderSaved.observe(this, { dataWrapper ->
            when(dataWrapper.status) {
                Status.LOADING -> {
                    progress_bar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    progress_bar.visibility = View.GONE
                    val order = viewModel.order.value
                    order?.orderedDishes?.filter { orderedDish -> orderedDish.newOrder!! }?.forEach { orderedDish ->
                        orderedDish.newOrder = false
                    }

                    startActivity(Intent(this, WaitScreenActivity::class.java))
                }
                Status.ERROR -> {
                    progress_bar.visibility = View.GONE
                    Toast.makeText(this, ERROR_HACIENDO_PEDIDO, Toast.LENGTH_LONG).show()
                }
            }
        })

        viewModel.order.observe(this, { order ->
            val price = String.format("%.2f", order?.price)
            btn_confirmar_pedido.text = getString(R.string.haz_tu_pedido_por_x, price)
        })

        val order = intent.getSerializableExtra(ORDER_KEY) as Order?
        intent.removeExtra(ORDER_KEY)
        if (order != null) {
            viewModel.order.value = order
        }

        btn_confirmar_pedido.setOnClickListener {
            confirmarPedido()
        }

    }

    fun updateOrder() {
        if (!viewModel.order.value?.orderedDishes.isNullOrEmpty()) {
            adapter.submitList(viewModel.order.value?.orderedDishes)
        } else {
            if (adapter.currentList.isEmpty()) {
                Toast.makeText(applicationContext, "No se ha podido recibir el pedido", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun confirmarPedido() {

        val dialog = AlertDialog.Builder(this)
                .setTitle("Confirmar pedido")
                .setMessage("Estas seguro que quieres confirmar el pedido? Una vez confirmado no podrás eliminar el pedido.")
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton("Si") { dialog, _ ->
                    val price = String.format("%.2f", viewModel.order.value?.price)
                    val priceLong = price.replace(',','.').trim()
                    viewModel.order.value?.price = priceLong.toFloat()
                    val tableId = intent.getStringExtra(EasyOrderConstants.TABLE_ID_KEY)
                    viewModel.saveOrder(tableId, viewModel.order.value!!)
                    dialog.dismiss()
                }
        dialog.show()
    }

    fun initRecyclerView() {

        // Set Layout Manager
        recycler_vw_comanda.layoutManager = LinearLayoutManager(applicationContext)

        adapter = OrderClientAdapter(viewModel.order)
        // Set Adapter
        recycler_vw_comanda.adapter = adapter

    }

    override fun onBackPressed() {
        viewModel.order.value?.orderedDishes = adapter.currentList

        val tableId = intent.getStringExtra(EasyOrderConstants.TABLE_ID_KEY)
        val restaurantId = intent.getStringExtra(EasyOrderConstants.RESTAURANT_ID_KEY)

        val intent = Intent(applicationContext, MenuRestaurantActivity::class.java)
        intent.putExtra(EasyOrderConstants.ORDER_KEY, viewModel.order.value)
        intent.putExtra(EasyOrderConstants.TABLE_ID_KEY, tableId)
        intent.putExtra(EasyOrderConstants.RESTAURANT_ID_KEY, restaurantId)
        startActivity(intent)
        finish()
    }
}