package edu.uoc.easyorderfront.ui.restaurant

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
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
    private val REQUEST_CODE_PICK_IMAGE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_restaurant)

        prepareUI()
    }

    fun prepareUI() {

        //TODO: Observe createRestaurantState from viewModel

        //TODO: OnClickListener image restaurant
        btn_add_image.setOnClickListener({
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.setType("image/*")
            val mimeTypes = arrayOf("image/jped", "image/png")
            photoPickerIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(photoPickerIntent, REQUEST_CODE_PICK_IMAGE)
        })

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
            startActivity(Intent(this, CreateRestaurantActivity::class.java))
            finish()
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                val selectedImage = data?.data

                img_view.setImageURI(selectedImage)
            }
        }
    }
}