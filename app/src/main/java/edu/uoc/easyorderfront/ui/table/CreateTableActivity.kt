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
import edu.uoc.easyorderfront.domain.model.Table
import kotlinx.android.synthetic.main.activity_create_table.*
import kotlinx.android.synthetic.main.activity_create_table.btn_crear
import kotlinx.android.synthetic.main.activity_create_table.txt_id
import kotlinx.android.synthetic.main.activity_create_table.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class CreateTableActivity : BottomSheetDialogFragment() {
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

    //TODO: BORRAR
    // Mostrar la activity sin ocupar toda la pantalla
    /*fun setActivityHalfScreen() {
        val display = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(display)
        val width = display.widthPixels
        val height = display.heightPixels
        window.setLayout((width * 1.0).toInt(), (height * 0.5).toInt())
    }*/

    fun prepareUI(view: View) {

        view.btn_crear.setOnClickListener({
            val tableUid = txt_id.text.toString()
            val tableCapacity = txt_capacidad.text.toString().toIntOrNull()

            if (tableUid.isBlank()
                || tableCapacity != null) {
                Toast.makeText(context, getString(R.string.rellenar_todos_los_campos), Toast.LENGTH_LONG).show()
            } else {
                viewModel.createTable(Table(tableUid, tableCapacity, "EMPTY"))
            }
        })

        view.btn_cancelar.setOnClickListener({
            dismiss()
        })
    }
}