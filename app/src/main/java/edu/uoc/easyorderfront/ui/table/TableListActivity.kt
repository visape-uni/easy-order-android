package edu.uoc.easyorderfront.ui.table

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import edu.uoc.easyorderfront.R
import edu.uoc.easyorderfront.data.SessionManager
import edu.uoc.easyorderfront.domain.model.Restaurant
import edu.uoc.easyorderfront.domain.model.Worker
import edu.uoc.easyorderfront.ui.constants.EasyOrderConstants.RESTAURANT_ID_KEY
import edu.uoc.easyorderfront.ui.constants.UIMessages
import edu.uoc.easyorderfront.ui.utils.DataWrapper
import edu.uoc.easyorderfront.ui.utils.Status
import kotlinx.android.synthetic.main.activity_create_table.*
import kotlinx.android.synthetic.main.activity_nou_plat.*
import kotlinx.android.synthetic.main.activity_table_list.*
import kotlinx.android.synthetic.main.activity_table_list.progress_bar
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * Pass RestaurantID in intent
 */

class TableListActivity : AppCompatActivity() {

    private val TAG = "TableListActivity"

    private val adapter = TablesAdapter()
    private val layoutManager = LinearLayoutManager(this)

    private val viewModel: TableListViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_table_list)

        prepareUI()
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        // TODO: SOLO MOSTRAR MENU SI ES EL DUEÑO
        menuInflater.inflate(R.menu.menu_table_list, menu)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.btn_add_table -> {
                if (viewModel.restaurantProfile.value?.data != null) {
                    val createTableBottomActivity = CreateTableDialogFragment(viewModel.restaurantProfile.value?.data!!)
                    createTableBottomActivity.show(supportFragmentManager, "TAG")

                } else {
                    Toast.makeText(applicationContext, "Error: El perfil del restaurante no se ha encontrado", Toast.LENGTH_LONG).show()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun initRecyclerView() {

        // Set Layout Manager
        recycler_view_estado_mesas.layoutManager = layoutManager

        // Set Adapter
        recycler_view_estado_mesas.adapter = adapter
    }

    fun prepareUI() {

        viewModel.tables.observe(this, { dataWrapper ->
            when(dataWrapper.status) {
                Status.LOADING -> {
                    progress_bar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    progress_bar.visibility = View.GONE
                    val tableList = dataWrapper.data
                    if (tableList != null && tableList.isNotEmpty()) {
                        adapter.submitList(tableList)
                    } else {
                        if (adapter.currentList.isNotEmpty()) {
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
                    viewModel.getTables(dataWrapper.data?.id)
                }
                Status.ERROR -> {
                    progress_bar.visibility = View.GONE
                    Log.e(TAG, "Error obteniendo restaurant")
                    Toast.makeText(applicationContext, dataWrapper.message, Toast.LENGTH_SHORT).show()
                }
            }
        })

        initRecyclerView()

        getRestaurantTables()
    }

    fun getRestaurantTables() {

        val restaurant = (SessionManager(applicationContext).getUser() as Worker).restaurant
        if (restaurant != null) {
            viewModel.restaurantProfile.postValue(DataWrapper.success(restaurant))
        } else {
            val restrurantId = intent.getStringExtra(RESTAURANT_ID_KEY)
            viewModel.getRestaurant(restrurantId!!)
        }
    }

}