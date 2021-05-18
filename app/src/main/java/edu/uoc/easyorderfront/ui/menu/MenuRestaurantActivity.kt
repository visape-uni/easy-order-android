package edu.uoc.easyorderfront.ui.menu

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import edu.uoc.easyorderfront.R
import edu.uoc.easyorderfront.domain.model.Order
import edu.uoc.easyorderfront.ui.adapter.MenuRestaurantAdapter
import edu.uoc.easyorderfront.ui.constants.EasyOrderConstants
import edu.uoc.easyorderfront.ui.constants.EasyOrderConstants.ORDER_KEY
import edu.uoc.easyorderfront.ui.constants.EasyOrderConstants.RESTAURANT_ID_KEY
import edu.uoc.easyorderfront.ui.constants.EasyOrderConstants.TABLE_ID_KEY
import edu.uoc.easyorderfront.ui.constants.UIMessages
import edu.uoc.easyorderfront.ui.order.OrderClientActivity
import edu.uoc.easyorderfront.ui.utils.DataWrapper
import edu.uoc.easyorderfront.ui.utils.Status
import kotlinx.android.synthetic.main.activity_editar_menu.error_message
import kotlinx.android.synthetic.main.activity_editar_menu.progress_bar
import kotlinx.android.synthetic.main.activity_editar_menu.recycler_view_editar_menu
import kotlinx.android.synthetic.main.activity_menu_restaurante.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.math.RoundingMode
import java.text.DecimalFormat

class MenuRestaurantActivity : AppCompatActivity() {

    private val TAG = "MenuRestauranteActivity"

    private lateinit var adapter : MenuRestaurantAdapter

    private val viewModel: MenuRestaurantViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_restaurante)

        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = UIMessages.TITLE_MENU_RESTAURANT

        prepareUI()
    }

    override fun onBackPressed() {
        dialogDesocuparMesa()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                dialogDesocuparMesa()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun dialogDesocuparMesa() {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Desocupar mesa")
            .setMessage("Estas seguro que quieres dejar la mesa?")
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("Si") { dialog, _ ->

                val tableId = intent.getStringExtra(TABLE_ID_KEY)
                val order = viewModel.order.value?.data

                if (order != null && order.orderedDishes != null && order.orderedDishes!!.isNotEmpty()) {
                    Toast.makeText(applicationContext, "No puedes dejar la mesa sin pagar si ya has hecho un pedido", Toast.LENGTH_LONG).show()
                } else {
                    viewModel.changeTableState(tableId, "", EasyOrderConstants.EMPTY_STATE)
                }

                dialog.dismiss()
            }
        dialog.show()
    }

    fun prepareUI() {

        viewModel.tableStateChanged.observe(this, {dataWrapper ->
            when(dataWrapper.status) {
                Status.LOADING -> {
                    progress_bar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    progress_bar.visibility = View.GONE
                    finish()
                }
                Status.ERROR -> {
                    progress_bar.visibility = View.GONE
                    Toast.makeText(applicationContext, "No has podido desocupar la mesa", Toast.LENGTH_SHORT).show()
                }
            }
        })

        viewModel.menu.observe(this, { dataWrapper ->
            when(dataWrapper.status) {
                Status.LOADING -> {
                    progress_bar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    initRecyclerView()
                    progress_bar.visibility = View.GONE
                    error_message.visibility = View.GONE
                    val menu = dataWrapper.data
                    if (menu?.categories != null && menu.categories.isNotEmpty()) {
                        adapter.submitList(menu.categories)

                    } else {
                        if (adapter.currentList.isEmpty()) {
                            error_message.visibility = View.VISIBLE
                        }
                    }
                }
                Status.ERROR -> {
                    progress_bar.visibility = View.GONE
                    Log.e(TAG, "Error obteniendo mesas")
                    Toast.makeText(applicationContext, dataWrapper.message, Toast.LENGTH_SHORT).show()
                }
            }
        })

        viewModel.restaurantProfile.observe(this, { dataWrapper->
            when(dataWrapper.status) {
                Status.LOADING -> {
                    progress_bar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    // Get Table
                    viewModel.getMenu(dataWrapper.data?.id)
                }
                Status.ERROR -> {
                    progress_bar.visibility = View.GONE
                    Log.e(TAG, "Error obteniendo restaurant")
                    Toast.makeText(applicationContext, dataWrapper.message, Toast.LENGTH_SHORT).show()
                }
            }
        })

        viewModel.orderPrice.observe(this, { orderPrice ->

            val df = DecimalFormat()
            df.maximumFractionDigits = 2
            df.minimumFractionDigits = 2
            df.roundingMode = RoundingMode.CEILING
            val price = df.format(orderPrice)

            btn_pedido.text = getString(R.string.haz_tu_pedido_por_x,  price)
            viewModel.order.value?.data?.price = orderPrice
        })

        viewModel.order.observe(this, { dataWrapper ->
            when(dataWrapper.status) {
                Status.LOADING -> {
                    progress_bar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    progress_bar.visibility = View.GONE
                    viewModel.orderPrice.postValue(dataWrapper.data?.price)
                }
                Status.ERROR -> {
                    progress_bar.visibility = View.GONE
                    Log.e(TAG, "Error obteniendo pedido")
                    Toast.makeText(applicationContext, dataWrapper.message, Toast.LENGTH_SHORT).show()
                }
            }
        })

        val tableId = intent.getStringExtra(TABLE_ID_KEY)

        val order = intent.getSerializableExtra(ORDER_KEY) as Order?
        if (order != null) {
            // Si se viene de la pantalla de confirmacion del pedido
            order.orderedDishes = order.orderedDishes?.toMutableList()
            viewModel.order.postValue(DataWrapper.success(order))
        } else {
            viewModel.getLastOrderFromTable(tableId)
        }

        btn_pedido.setOnClickListener({
            val tableId = intent.getStringExtra(TABLE_ID_KEY)
            val intent = Intent(this, OrderClientActivity::class.java)
            intent.putExtra(ORDER_KEY, viewModel.order.value?.data)
            intent.putExtra(TABLE_ID_KEY, tableId)
            intent.putExtra(RESTAURANT_ID_KEY, viewModel.restaurantProfile.value?.data?.id)
            startActivity(intent)
            finish()
        })

        getMenu()
    }

    fun initRecyclerView() {

        // Set Layout Manager
        recycler_view_editar_menu.layoutManager = LinearLayoutManager(applicationContext)

        adapter = MenuRestaurantAdapter(viewModel)
        // Set Adapter
        recycler_view_editar_menu.adapter = adapter
    }

    fun getMenu() {
        val restrurantId = intent.getStringExtra(RESTAURANT_ID_KEY)
        viewModel.getRestaurant(restrurantId!!)
    }
}