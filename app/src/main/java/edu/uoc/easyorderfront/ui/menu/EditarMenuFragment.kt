package edu.uoc.easyorderfront.ui.menu

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import edu.uoc.easyorderfront.R
import edu.uoc.easyorderfront.data.SessionManager
import edu.uoc.easyorderfront.domain.model.Worker
import edu.uoc.easyorderfront.ui.adapter.EditMenuAdapter
import edu.uoc.easyorderfront.ui.constants.EasyOrderConstants
import edu.uoc.easyorderfront.ui.constants.UIMessages
import edu.uoc.easyorderfront.ui.utils.DataWrapper
import edu.uoc.easyorderfront.ui.utils.OnTitleChangedListener
import edu.uoc.easyorderfront.ui.utils.Status
import kotlinx.android.synthetic.main.activity_editar_menu.*
import org.koin.android.viewmodel.ext.android.viewModel

class EditarMenuFragment : Fragment() {

    private val TAG = "EditarMenuActivity"

    private lateinit var adapter : EditMenuAdapter

    private val viewModel: EditarMenuViewModel by viewModel()

    internal lateinit var callback: OnTitleChangedListener

    fun setOnTitleChangedListener(callback: OnTitleChangedListener) {
        this.callback = callback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        callback.onTitleChanged(UIMessages.TITLE_EDIT_MENU)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_editar_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareUI()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_editar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.create_category_btn -> {
                if (viewModel.menu.value?.status?.equals(Status.SUCCESS)!!) {
                    if (viewModel.restaurantProfile.value?.data?.id != null) {
                        val createCategoryActivity = CreateCategoryDialogFragment(
                                viewModel.restaurantProfile.value?.data!!.id!!,
                                viewModel.menu
                        )
                        createCategoryActivity.show(fragmentManager!!, "TAG")
                    }
                } else {
                    Toast.makeText(context, "Espera a que cargue el menú", Toast.LENGTH_LONG).show()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }



    fun initRecyclerView() {

        // Set Layout Manager
        recycler_view_editar_menu.layoutManager = LinearLayoutManager(context)

        adapter = EditMenuAdapter(viewModel.restaurantProfile.value?.data?.id!!, viewModel)
        // Set Adapter
        recycler_view_editar_menu.adapter = adapter
    }

    fun prepareUI() {

        viewModel.menuDeleted.observe(this, { dataWrapper ->
            when(dataWrapper.status) {
                Status.LOADING -> {
                    progress_bar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    updateMenu(dataWrapper)
                    Toast.makeText(context, "Plato eliminado correctamente", Toast.LENGTH_LONG).show()
                }
                Status.ERROR -> {
                    progress_bar.visibility = View.GONE
                    Log.e(TAG, "Error obteniendo mesas")
                    Toast.makeText(context, dataWrapper.message, Toast.LENGTH_SHORT).show()
                }
            }
        })

        viewModel.menu.observe(this, { dataWrapper ->
            when(dataWrapper.status) {
                Status.LOADING -> {
                    progress_bar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    updateMenu(dataWrapper)
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

    fun updateMenu(dataWrapper: DataWrapper<edu.uoc.easyorderfront.domain.model.Menu>) {
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