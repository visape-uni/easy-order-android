package edu.uoc.easyorderfront.ui.table

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import edu.uoc.easyorderfront.R
import edu.uoc.easyorderfront.data.SessionManager
import edu.uoc.easyorderfront.domain.model.Worker
import edu.uoc.easyorderfront.ui.adapter.TablesAdapter
import edu.uoc.easyorderfront.ui.constants.EasyOrderConstants
import edu.uoc.easyorderfront.ui.constants.EasyOrderConstants.RESTAURANT_ID_KEY
import edu.uoc.easyorderfront.ui.utils.DataWrapper
import edu.uoc.easyorderfront.ui.utils.Status
import kotlinx.android.synthetic.main.activity_table_list.*
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * Pass RestaurantID in intent
 */

class TableListFragment : Fragment() {

    private val TAG = "TableListActivity"

    private val adapter = TablesAdapter()

    private val viewModel: TableListViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_table_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Preparar Vista
        prepareUI()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_table_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.btn_add_table -> {
                if (viewModel.restaurantProfile.value?.data != null) {
                    val createTableBottomActivity = CreateTableDialogFragment(viewModel.restaurantProfile.value?.data!!)
                    createTableBottomActivity.show(fragmentManager!!, "TAG")

                } else {
                    Toast.makeText(context, "Error: El perfil del restaurante no se ha encontrado", Toast.LENGTH_LONG).show()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun initRecyclerView() {

        // Set Layout Manager
        recycler_view_estado_mesas.layoutManager = LinearLayoutManager(context)

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
                    tableList?.forEach { table ->
                        table.tableRef = viewModel.restaurantProfile.value?.data?.id + '/' + table.uid
                    }
                    if (tableList != null && tableList.isNotEmpty()) {
                        adapter.submitList(tableList)
                    } else {
                        if (adapter.currentList.isEmpty()) {
                            error_message.visibility = View.VISIBLE
                        }
                    }
                }
                Status.ERROR -> {
                    progress_bar.visibility = View.GONE
                    Log.e(TAG, "Error obteniendo mesas")
                    Toast.makeText(context, dataWrapper.message, Toast.LENGTH_SHORT).show()
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
                    Toast.makeText(context, dataWrapper.message, Toast.LENGTH_SHORT).show()
                }
            }
        })

        initRecyclerView()

        getRestaurantTables()
    }

    fun getRestaurantTables() {

        val restaurant = (SessionManager(context!!).getUser() as Worker).restaurant
        if (restaurant != null) {
            viewModel.restaurantProfile.postValue(DataWrapper.success(restaurant))
        } else {
            val restrurantId = arguments?.getString(RESTAURANT_ID_KEY)
            viewModel.getRestaurant(restrurantId!!)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(restaurantId: String) =
            TableListFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(EasyOrderConstants.RESTAURANT_ID_KEY, restaurantId)
                }
            }
    }
}