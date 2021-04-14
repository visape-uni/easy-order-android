package edu.uoc.easyorderfront.ui.restaurant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import edu.uoc.easyorderfront.R
import edu.uoc.easyorderfront.data.SessionManager
import edu.uoc.easyorderfront.domain.model.Restaurant
import edu.uoc.easyorderfront.domain.model.User
import kotlinx.android.synthetic.main.activity_create_restaurant.*
import org.koin.android.viewmodel.ext.android.viewModel

class CreateRestaurantActivity : AppCompatActivity() {
    private val viewModel: CreateRestaurantViewModel by viewModel()
    private val TAG = "CreateRestaurantActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_restaurant)

        prepareUI()
    }

    fun prepareUI() {

        //TODO: Observe createRestaurantState from viewModel

        //TODO: OnClickListener image restaurant

        btn_crear.setOnClickListener({
            val restaurantName = nombre_restaurante_txt.text.toString()
            val restaurantStreet = calle_restaurante_txt.text.toString()
            val restaurantCity = ciudad_restaurante_txt.text.toString()
            val restaurantZipCode = codigo_postal_txt.text.toString()
            val restaurantCountry = pais_restaurante_txt.text.toString()

            //TODO: Guargar imagen del restaurante

            if (restaurantName.isBlank()
                || restaurantStreet.isBlank()
                || restaurantCity.isBlank()
                || restaurantZipCode.isBlank()
                || restaurantCountry.isBlank()) {

                Toast.makeText(applicationContext, getString(R.string.rellenar_todos_los_campos), Toast.LENGTH_LONG).show()
            } else {
                // Guardar al usuario como dueño
                val owner = User(SessionManager(applicationContext).getUserId())
                viewModel.createRestaurant(Restaurant(null, restaurantName, restaurantStreet, restaurantCity, restaurantZipCode, restaurantCountry, owner = owner))
            }
        })

        btn_cancelar.setOnClickListener({
            //TODO: Mostrar pantalla del perfil de trabajador
            finish()
        })
    }
}