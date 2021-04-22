package edu.uoc.easyorderfront.ui.restaurant

import android.content.ClipData
import android.content.ClipboardManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialog
import edu.uoc.easyorderfront.R
import edu.uoc.easyorderfront.data.SessionManager
import edu.uoc.easyorderfront.domain.model.Worker
import edu.uoc.easyorderfront.ui.constants.EasyOrderConstants
import edu.uoc.easyorderfront.ui.constants.UIMessages
import edu.uoc.easyorderfront.ui.table.CreateTableDialogFragment
import edu.uoc.easyorderfront.ui.utils.DataWrapper
import edu.uoc.easyorderfront.ui.utils.Status
import kotlinx.android.synthetic.main.activity_perfil_restaurante.*
import kotlinx.android.synthetic.main.activity_perfil_restaurante.progress_bar
import kotlinx.android.synthetic.main.activity_perfil_restaurante.txt_id
import kotlinx.android.synthetic.main.activity_perfil_restaurante.txt_nombre
import org.koin.android.viewmodel.ext.android.viewModel

class RestaurantProfileActivity : AppCompatActivity() {
    private val viewModel: RestaurantProfileViewModel by viewModel()
    private val TAG = "RestaurantProfileActivity"

    private val RESTAURANT_ID_KEY = "restaurantId"

    private lateinit var bottomSheetDialog: BottomSheetDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_restaurante)
        prepareUI()
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        // TODO: SOLO MOSTRAR MENU SI ES EL DUEÑO
        menuInflater.inflate(R.menu.menu_worker_profile, menu)
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

    fun prepareUI() {
        viewModel.restaurantProfile.observe(this, { dataWrapper ->
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
                            val clipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText(EasyOrderConstants.WORKERID_LABEL, restaurant.id)
                            clipboardManager.setPrimaryClip(clip)
                            Toast.makeText(applicationContext, "ID del restaurante copiado correctamente", Toast.LENGTH_LONG).show()
                        })

                    } else {
                        Toast.makeText(applicationContext, UIMessages.ERROR_CARGANDO_RESTAURANTE, Toast.LENGTH_LONG).show()
                    }
                    progress_bar.visibility = View.GONE
                }
                Status.ERROR -> {
                    progress_bar.visibility = View.GONE
                    Toast.makeText(applicationContext, UIMessages.ERROR_CARGANDO_RESTAURANTE, Toast.LENGTH_LONG).show()
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


        val restaurant = (SessionManager(applicationContext).getUser() as Worker).restaurant
        if (restaurant != null) {
            viewModel.restaurantProfile.postValue(DataWrapper.success(restaurant))
        } else {
            val restrurantId = intent.getStringExtra(RESTAURANT_ID_KEY)
            viewModel.getRestaurant(restrurantId!!)
        }
    }
}