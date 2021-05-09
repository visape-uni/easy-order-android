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
import edu.uoc.easyorderfront.data.SessionManager
import edu.uoc.easyorderfront.domain.model.Worker
import edu.uoc.easyorderfront.ui.adapter.EditMenuAdapter
import edu.uoc.easyorderfront.ui.constants.EasyOrderConstants
import edu.uoc.easyorderfront.ui.utils.DataWrapper
import edu.uoc.easyorderfront.ui.utils.Status
import kotlinx.android.synthetic.main.activity_editar_menu.*
import org.koin.android.viewmodel.ext.android.viewModel

class EditarMenuFragment : Fragment() {

    private val TAG = "EditarMenuActivity"

    private val adapter = EditMenuAdapter()
    private val layoutManager = LinearLayoutManager(context)

    private val viewModel: EditarMenuViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_editar_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareUI()
    }

    fun initRecyclerView() {

        // Set Layout Manager
        recycler_view_editar_menu.layoutManager = layoutManager

        // Set Adapter
        recycler_view_editar_menu.adapter = adapter
    }

    fun prepareUI() {

        viewModel.menu.observe(this, { dataWrapper ->
            when(dataWrapper.status) {
                Status.LOADING -> {
                    progress_bar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    progress_bar.visibility = View.GONE
                    val menu = dataWrapper.data
                    if (menu?.categories != null && menu.categories.isNotEmpty()) {
                        adapter.submitList(menu.categories)
                    } else {
                        if (adapter.currentList.isNotEmpty()) {
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

        initRecyclerView()

        getMenu()
    }

    fun getMenu() {
        val restaurant = (SessionManager(context!!).getUser() as Worker).restaurant
        if (restaurant != null) {
            viewModel.restaurantProfile.postValue(DataWrapper.success(restaurant))
        } else {
            val restrurantId = arguments?.getString(EasyOrderConstants.RESTAURANT_ID_KEY)
            viewModel.getRestaurant(restrurantId!!)
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(restaurantId: String) =
                EditarMenuFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(EasyOrderConstants.RESTAURANT_ID_KEY, restaurantId)
                    }
                }
    }
}