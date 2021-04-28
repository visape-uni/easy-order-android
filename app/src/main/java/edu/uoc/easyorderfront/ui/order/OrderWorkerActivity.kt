package edu.uoc.easyorderfront.ui.order

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import edu.uoc.easyorderfront.R
import edu.uoc.easyorderfront.domain.model.Table
import edu.uoc.easyorderfront.ui.constants.EasyOrderConstants
import kotlinx.android.synthetic.main.activity_order_worker.*

class OrderWorkerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_worker)

        prepareUI()
    }

    fun prepareUI() {
        val table = intent.getSerializableExtra(EasyOrderConstants.TABLE_ID_KEY) as Table
        txt_id.text = table.uid
        txt_capacidad.text = getString(R.string.num_personas, table.capacity.toString())

        if (table.state.equals(EasyOrderConstants.EMPTY_STATE)) {
            txt_estado.text = getString(R.string.la_mesa_esta_libre)

            lbl_tiempo.visibility = View.GONE
            txt_tiempo.visibility = View.GONE
            lbl_pedido.visibility = View.GONE
            recycler_view_pedido.visibility = View.GONE
            lbl_total.visibility = View.GONE
            txt_total.visibility = View.GONE
        } else {
            //TODO: LA MESA ESTA OCUPADA, HAY QUE RECIBIR LA COMANDA
            //TODO: PONER TIEMPO Y LISTADO DE PLATOS
            txt_estado.text = getString(R.string.la_mesa_esta_ocupada)
            //TODO: GET COMANDA
        }
    }
}