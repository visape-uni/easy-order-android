package edu.uoc.easyorderfront.ui.restaurant

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import edu.uoc.easyorderfront.R
import edu.uoc.easyorderfront.data.SessionManager
import edu.uoc.easyorderfront.domain.model.Restaurant
import edu.uoc.easyorderfront.domain.model.Worker
import edu.uoc.easyorderfront.ui.constants.UIMessages
import edu.uoc.easyorderfront.ui.main.MainWorkerMenuActivity
import edu.uoc.easyorderfront.ui.utils.OnTitleChangedListener
import edu.uoc.easyorderfront.ui.utils.Status
import kotlinx.android.synthetic.main.activity_create_restaurant.*
import org.koin.android.viewmodel.ext.android.viewModel

class CreateRestaurantFragment : Fragment() {
    private val viewModel: CreateRestaurantViewModel by viewModel()
    private val TAG = "CreateRestaurantActivity"
    private val REQUEST_CODE_PICK_IMAGE = 1

    internal lateinit var callback: OnTitleChangedListener

    fun setOnTitleChangedListener(callback: OnTitleChangedListener) {
        this.callback = callback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        callback.onTitleChanged(UIMessages.TITLE_CREATE_RESTAURANT)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_create_restaurant, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Preparar Vista
        prepareUI()

    }


    fun prepareUI() {

        viewModel.created.observe(viewLifecycleOwner, {dataWrapper ->
            when (dataWrapper.status) {
                Status.LOADING -> {
                    progress_bar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    Toast.makeText(context, getString(R.string.restaurante_creado_correctamente), Toast.LENGTH_LONG).show()
                    dataWrapper.data?.let {
                        SessionManager(requireContext()).addRestaurant(it)
                        (activity as MainWorkerMenuActivity).onBackPressed()
                    }
                }
                Status.ERROR -> {
                    progress_bar.visibility = View.GONE
                    Toast.makeText(context, UIMessages.ERROR_CREANDO_RESTAURANTE, Toast.LENGTH_LONG).show()
                }
            }
        })

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

                Toast.makeText(context, getString(R.string.rellenar_todos_los_campos), Toast.LENGTH_LONG).show()
            } else {
                // Guardar al usuario como due??o
                val owner = Worker(SessionManager(requireContext()).getUserId())
                viewModel.createRestaurant(Restaurant(null, restaurantName, restaurantStreet, restaurantCity, restaurantZipCode, restaurantCountry, owner = owner))
            }
        })

        btn_cancelar.setOnClickListener({
            (activity as MainWorkerMenuActivity).onBackPressed()
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

    companion object {
        @JvmStatic
        fun newInstance() =
            CreateRestaurantFragment().apply {
            }
    }
}