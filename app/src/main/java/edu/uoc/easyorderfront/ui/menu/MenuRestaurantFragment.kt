package edu.uoc.easyorderfront.ui.menu

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import edu.uoc.easyorderfront.R
import edu.uoc.easyorderfront.ui.adapter.MenuRestaurantAdapter
import edu.uoc.easyorderfront.ui.constants.EasyOrderConstants
import edu.uoc.easyorderfront.ui.utils.Status
import kotlinx.android.synthetic.main.activity_editar_menu.error_message
import kotlinx.android.synthetic.main.activity_editar_menu.progress_bar
import kotlinx.android.synthetic.main.activity_editar_menu.recycler_view_editar_menu
import kotlinx.android.synthetic.main.activity_menu_restaurante.*
import org.koin.android.viewmodel.ext.android.viewModel

class MenuRestaurantFragment : Fragment() {

    private val TAG = "MenuRestauranteActivity"

    private lateinit var adapter : MenuRestaurantAdapter
    private val layoutManager = LinearLayoutManager(context)

    private val viewModel: MenuRestaurantViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_menu_restaurante, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareUI()
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
                    viewModel.getMenu(dataWrapper.data?.id)
                }
                Status.ERROR -> {
                    progress_bar.visibility = View.GONE
                    Log.e(TAG, "Error obteniendo restaurant")
                    Toast.makeText(context, dataWrapper.message, Toast.LENGTH_SHORT).show()
                }
            }
        })

        viewModel.orderPrice.observe(this, {orderPrice ->
            btn_pedido.text = getString(R.string.haz_tu_pedido_por_x, orderPrice.toString())
        })

        viewModel.orderPrice.postValue(0.0)

        getMenu()
    }

    fun initRecyclerView() {

        // Set Layout Manager
        recycler_view_editar_menu.layoutManager = layoutManager

        adapter = MenuRestaurantAdapter(viewModel)
        // Set Adapter
        recycler_view_editar_menu.adapter = adapter
    }

    fun getMenu() {
        val restrurantId = arguments?.getString(EasyOrderConstants.RESTAURANT_ID_KEY)
        viewModel.getRestaurant(restrurantId!!)
    }

    companion object {

        @JvmStatic
        fun newInstance(restaurantId: String) =
                MenuRestaurantFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(EasyOrderConstants.RESTAURANT_ID_KEY, restaurantId)
                    }
                }
    }
}