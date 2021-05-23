package edu.uoc.easyorderfront.ui.restaurant

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import edu.uoc.easyorderfront.R
import edu.uoc.easyorderfront.data.SessionManager
import edu.uoc.easyorderfront.domain.model.Worker
import edu.uoc.easyorderfront.ui.constants.EasyOrderConstants
import edu.uoc.easyorderfront.ui.constants.UIMessages
import edu.uoc.easyorderfront.ui.main.MainWorkerMenuActivity
import edu.uoc.easyorderfront.ui.table.TableListFragment
import edu.uoc.easyorderfront.ui.utils.DataWrapper
import edu.uoc.easyorderfront.ui.utils.OnTitleChangedListener
import edu.uoc.easyorderfront.ui.utils.Status
import kotlinx.android.synthetic.main.activity_perfil_restaurante.*
import org.koin.android.viewmodel.ext.android.viewModel

class RestaurantProfileFragment : Fragment() {
    private val viewModel: RestaurantProfileViewModel by viewModel()
    private val TAG = "RestaurantProfileActivity"

    private lateinit var bottomSheetDialog: BottomSheetDialog

    internal lateinit var callback: OnTitleChangedListener

    fun setOnTitleChangedListener(callback: OnTitleChangedListener) {
        this.callback = callback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        callback.onTitleChanged(UIMessages.TITLE_RESTAURANT_PROFILE)
        (activity as MainWorkerMenuActivity).setItemMenu(1)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_perfil_restaurante, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Preparar Vista
        prepareUI()
    }

    override fun onResume() {
        getRestaurant()
        super.onResume()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // TODO: SOLO MOSTRAR MENU SI ES EL DUEÑO
        inflater.inflate(R.menu.menu_restaurant_profile, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.btn_table -> {
                if (viewModel.restaurantProfile.value?.data != null) {
                    val fragment = TableListFragment.newInstance(viewModel.restaurantProfile.value?.data!!.id!!)
                    (activity as MainWorkerMenuActivity).replaceFragment(fragment)
                } else {
                    Toast.makeText(context, "Error: El perfil del restaurante no se ha encontrado", Toast.LENGTH_LONG).show()
                }
            }
            R.id.btn_add_worker -> {
                val intent = Intent(context, WorkersListActivity::class.java)
                intent.putExtra(EasyOrderConstants.RESTAURANT_ID_KEY, viewModel.restaurantProfile.value?.data)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun prepareUI() {
        viewModel.restaurantProfile.observe(viewLifecycleOwner, { dataWrapper ->
            when (dataWrapper.status) {
                Status.LOADING -> {
                    progress_bar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    val restaurant = dataWrapper.data
                    if (restaurant != null) {
                        txt_id.text = restaurant.id
                        txt_nombre.text = restaurant.name
                        txt_calle.text = restaurant.street
                        txt_ciudad.text = restaurant.city
                        txt_codigo_postal.text = restaurant.zipCode
                        txt_pais.text = restaurant.country

                        btn_copy.setOnClickListener({
                            val clipboardManager = activity?.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText(EasyOrderConstants.WORKER_ID_LABEL, restaurant.id)
                            clipboardManager.setPrimaryClip(clip)
                            Toast.makeText(context, "ID del restaurante copiado correctamente", Toast.LENGTH_LONG).show()
                        })

                    } else {
                        Toast.makeText(context, UIMessages.ERROR_CARGANDO_RESTAURANTE, Toast.LENGTH_LONG).show()
                    }
                    progress_bar.visibility = View.GONE
                }
                Status.ERROR -> {
                    progress_bar.visibility = View.GONE
                    Toast.makeText(context, UIMessages.ERROR_CARGANDO_RESTAURANTE, Toast.LENGTH_LONG).show()
                }
            }
            val restaurant = dataWrapper.data
            if (restaurant != null) {
                txt_id.text = restaurant.id
                txt_nombre.text = restaurant.name
                txt_calle.text = restaurant.street
                txt_ciudad.text = restaurant.city
                txt_codigo_postal.text = restaurant.zipCode
                txt_pais.text = restaurant.country
            }
        })
    }

    fun getRestaurant() {

        val restaurant = (SessionManager(requireContext()).getUser() as Worker).restaurant
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
            RestaurantProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(EasyOrderConstants.RESTAURANT_ID_KEY, restaurantId)
                }
            }
    }
}