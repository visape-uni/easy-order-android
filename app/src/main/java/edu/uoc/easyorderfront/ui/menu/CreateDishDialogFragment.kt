package edu.uoc.easyorderfront.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import edu.uoc.easyorderfront.R
import edu.uoc.easyorderfront.domain.model.Category
import edu.uoc.easyorderfront.domain.model.Dish
import edu.uoc.easyorderfront.domain.model.Menu
import edu.uoc.easyorderfront.ui.constants.UIMessages
import edu.uoc.easyorderfront.ui.utils.DataWrapper
import edu.uoc.easyorderfront.ui.utils.Status
import kotlinx.android.synthetic.main.bottom_fragment_create_dish.*
import kotlinx.android.synthetic.main.bottom_fragment_create_dish.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class CreateDishDialogFragment(
        private val restaurantId: String,
        private val category: Category,
        private val menuLive: MutableLiveData<DataWrapper<Menu>>
) : BottomSheetDialogFragment() {
    private val viewModel: CreateDishViewModel by viewModel()
    private val TAG = "CreateDishActivity"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.bottom_fragment_create_dish, container, false)

        prepareUI(view)

        return view
    }

    fun prepareUI(view: View) {
        view.lbl_crear_plato.text =
                getString(R.string.nuevo_plato_para_la_categoria, category.name)

        viewModel.createdDish.observe(this, {dataWrapper ->
            when(dataWrapper.status) {
                Status.LOADING -> {
                    view.progress_bar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {

                    view.progress_bar.visibility = View.GONE
                    if (dataWrapper.data != null) {
                        Toast.makeText(context, "Plato creada correctamente", Toast.LENGTH_LONG)
                                .show()

                        addDishToCategory(dataWrapper.data)
                    } else {
                        Toast.makeText(context, UIMessages.ERROR_CREANDO_TABLE, Toast.LENGTH_LONG)
                                .show()
                    }
                    dismiss()
                }
                Status.ERROR -> {

                    view.progress_bar.visibility = View.GONE
                    Toast.makeText(context, UIMessages.ERROR_CREANDO_TABLE, Toast.LENGTH_LONG).show()
                }
            }
        })

        view.btn_crear.setOnClickListener({
            val nombre = txt_nombre.text.toString()
            val description = txt_descripcion.text.toString()
            val price = txt_precio.text.toString()
            if (nombre.isNotBlank() && description.isNotBlank() && price.isNotBlank()) {
                createDish(nombre, description, price.toDouble())
            } else {
                Toast.makeText(context, "Debes rellenar el nombre, la descripción y el precio", Toast.LENGTH_LONG).show()
            }
        })

        view.btn_cancelar.setOnClickListener({
            dismiss()
        })
    }

    fun createDish(name: String, description: String, price: Double) {
        val categoryId = category.uid!!
        val dishId = menuLive.value?.data?.categories?.get(categoryId.toInt() - 1)?.dishes?.size?.plus(1).toString()
        val dish = Dish(dishId, name, description, price)
        viewModel.createDish(dish, restaurantId, categoryId)
    }

    fun addDishToCategory(dish: Dish) {
        val categoryId = category.uid!!
        val newCategory = menuLive.value?.data?.categories?.get(categoryId.toInt() -1)
        newCategory?.dishes?.add(dish)
        menuLive.postValue(DataWrapper.success(menuLive.value?.data))
    }
}