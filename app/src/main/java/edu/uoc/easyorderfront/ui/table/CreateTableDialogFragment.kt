package edu.uoc.easyorderfront.ui.table

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import edu.uoc.easyorderfront.R
import edu.uoc.easyorderfront.data.SessionManager
import edu.uoc.easyorderfront.domain.model.Restaurant
import edu.uoc.easyorderfront.domain.model.Table
import edu.uoc.easyorderfront.ui.constants.UIMessages
import edu.uoc.easyorderfront.ui.utils.Status
import kotlinx.android.synthetic.main.activity_create_table.*
import kotlinx.android.synthetic.main.activity_create_table.btn_crear
import kotlinx.android.synthetic.main.activity_create_table.txt_id
import kotlinx.android.synthetic.main.activity_create_table.view.*
import kotlinx.android.synthetic.main.activity_perfil_client.*
import kotlinx.android.synthetic.main.activity_perfil_restaurante.*
import org.koin.android.viewmodel.ext.android.viewModel

class CreateTableDialogFragment(
        private val restaurant: Restaurant
) : BottomSheetDialogFragment() {
    private val viewModel: CreateTableViewModel by viewModel()
    private val TAG = "CreateTableActivity"



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_create_table, container, false)

        prepareUI(view)

        return view;
    }


    fun prepareUI(view: View) {

        viewModel.created.observe(this, {dataWrapper ->
            when(dataWrapper.status) {
                Status.LOADING -> {
                    view.progress_bar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {

                    view.progress_bar.visibility = View.GONE
                    if (dataWrapper.data != null) {
                        Toast.makeText(context, "Mesa creada correctamente", Toast.LENGTH_LONG)
                            .show()

                        addTableToRestaurant(dataWrapper.data)
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

        // Set Table ID (RestaurantID/numTables + 1) (Si tables is null, then return 1)
        val tableNumber = restaurant.tables?.size?.plus(1) ?: 1
        val tableId = restaurant.id + "/" + tableNumber
        view.txt_id.setText(tableId)

        view.btn_crear.setOnClickListener({
            val tableUid = view.txt_id.text.toString()
            val tableCapacity = view.txt_capacidad.text.toString().toIntOrNull()

            if (tableUid.isBlank()
                || tableCapacity == null) {
                Toast.makeText(context, getString(R.string.rellenar_todos_los_campos), Toast.LENGTH_LONG).show()
            } else {
                createTable(tableUid, tableCapacity)
            }
        })

        view.btn_cancelar.setOnClickListener({
            dismiss()
        })
    }

    fun createTable(tableUid: String, tableCapacity: Int) {
        val table = Table(tableUid, tableCapacity, "EMPTY")
        viewModel.createTable(table)
    }
    fun addTableToRestaurant(table: Table) {
        restaurant.tables?.add(table)
        SessionManager(requireContext()).addTable(table)
        (activity as TableListActivity).getRestaurantTables()
    }
}