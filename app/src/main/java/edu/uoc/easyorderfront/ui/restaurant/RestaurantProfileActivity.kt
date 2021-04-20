package edu.uoc.easyorderfront.ui.restaurant

import android.content.ClipData
import android.content.ClipboardManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import edu.uoc.easyorderfront.R
import edu.uoc.easyorderfront.data.SessionManager
import edu.uoc.easyorderfront.ui.constants.EasyOrderConstants
import edu.uoc.easyorderfront.ui.constants.UIMessages
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_restaurante)
        prepareUI()
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

        val restrurantId = intent.getStringExtra(RESTAURANT_ID_KEY)
        viewModel.getRestaurant(restrurantId)
    }
}