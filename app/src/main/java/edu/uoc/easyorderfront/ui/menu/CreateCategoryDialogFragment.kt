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
import edu.uoc.easyorderfront.domain.model.Menu
import edu.uoc.easyorderfront.ui.constants.UIMessages
import edu.uoc.easyorderfront.ui.utils.DataWrapper
import edu.uoc.easyorderfront.ui.utils.Status
import kotlinx.android.synthetic.main.bottom_fragment_create_category.*
import kotlinx.android.synthetic.main.bottom_fragment_create_category.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class CreateCategoryDialogFragment(
        private val restaurantId: String,
        private val menuLive: MutableLiveData<DataWrapper<Menu>>
) : BottomSheetDialogFragment() {
    private val viewModel: CreateCategoryViewModel by viewModel()
    private val TAG = "CreateCategoryActivity"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.bottom_fragment_create_category, container, false)

        prepareUI(view)

        return view
    }

    fun prepareUI(view: View) {

        viewModel.createdCategory.observe(this, {dataWrapper ->
            when(dataWrapper.status) {
                Status.LOADING -> {
                    view.progress_bar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {

                    view.progress_bar.visibility = View.GONE
                    if (dataWrapper.data != null) {
                        Toast.makeText(context, "Categoria creada correctamente", Toast.LENGTH_LONG)
                                .show()

                        addCategoryToMenu(dataWrapper.data)
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
            if (nombre.isNotBlank() && description.isNotBlank()) {
                createCategory(nombre, description)
            } else {
                Toast.makeText(context, "Debes rellenar el nombre y la descripción", Toast.LENGTH_LONG).show()
            }
        })

        view.btn_cancelar.setOnClickListener({
            dismiss()
        })
    }

    fun createCategory(name: String, description: String) {
        val categoryId = menuLive.value?.data?.categories?.size?.plus(1).toString()
        val category = Category(categoryId, name, description)
        viewModel.createCategory(category, restaurantId)
    }

    fun addCategoryToMenu(category: Category) {
        val newMenu = menuLive.value?.data
        newMenu?.categories?.add(category)
        menuLive.postValue(DataWrapper.success(newMenu))
    }
}