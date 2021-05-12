package edu.uoc.easyorderfront.ui.menu

import android.app.AlertDialog
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
import edu.uoc.easyorderfront.ui.utils.Status
import kotlinx.android.synthetic.main.activity_editar_menu.error_message
import kotlinx.android.synthetic.main.activity_editar_menu.progress_bar
import kotlinx.android.synthetic.main.activity_editar_menu.recycler_view_editar_menu
import kotlinx.android.synthetic.main.activity_menu_restaurante.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

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

        prepareUI()
    }

    override fun onBackPressed() {
        dialogDesocuparMesa()
        super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                dialogDesocuparMesa()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    public fun dialogDesocuparMesa() {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Desocupar mesa")
            .setMessage("Estas seguro que quieres dejar la mesa?")
            .setNegativeButton("No") {dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("Si") {dialog, _ ->
                //TODO: IMPORTANT COMPROBAR QUE NO HE HA HECHO PEDIDO Y PONER MESA COMO EMPTY

                finish()
                dialog.dismiss()
            }
        dialog.show()
    }

    fun prepareUI() {

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
            val price = String.format("%.2f", orderPrice)
            btn_pedido.text = getString(R.string.haz_tu_pedido_por_x, price)
            viewModel.order.value?.price = orderPrice
        })

        viewModel.order.observe(this, { order ->
            viewModel.orderPrice.postValue(order.price)
        })

        val startedTime = Calendar.getInstance().time.time
        viewModel.order.postValue(Order(null, 0.0, EasyOrderConstants.ORDERING_STATE, startedTime))

        btn_pedido.setOnClickListener({
            //TODO: MOSTRAR PEDIDO
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
        val restrurantId = intent.getStringExtra(EasyOrderConstants.RESTAURANT_ID_KEY)
        viewModel.getRestaurant(restrurantId!!)
    }

}