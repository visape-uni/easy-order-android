package edu.uoc.easyorderfront.ui.table

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator
import edu.uoc.easyorderfront.R
import edu.uoc.easyorderfront.ui.constants.EasyOrderConstants
import edu.uoc.easyorderfront.ui.constants.UIMessages
import edu.uoc.easyorderfront.ui.utils.Status
import kotlinx.android.synthetic.main.activity_ocupar_mesa.*
import org.koin.android.viewmodel.ext.android.viewModel

class OcupyTableActivity : AppCompatActivity() {
    private val TAG = "OcupyTableActivity"
    private val viewModel: OcupyTableViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ocupar_mesa)

        prepareUI()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_ocupar_mesa, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.qr_btn -> {
                IntentIntegrator(this).initiateScan()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun prepareUI() {
        viewModel.tableStateChanged.observe(this, { dataWrapper ->
            when(dataWrapper.status) {
                Status.LOADING -> {
                    progress_bar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    progress_bar.visibility = View.GONE
                    //TODO: MOSTRAR MENU DEL RESTAURANTE
                    Toast.makeText(applicationContext, "Mesa ${dataWrapper.data?.uid} ocupada correctamente", Toast.LENGTH_LONG).show()
                }
                Status.ERROR -> {
                    progress_bar.visibility = View.GONE
                    Toast.makeText(applicationContext, UIMessages.ERROR_OCUPANDO_MESA, Toast.LENGTH_LONG).show()
                }
            }
        })

        viewModel.table.observe(this, { dataWrapper ->
            when(dataWrapper.status) {
                Status.LOADING -> {
                    progress_bar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    progress_bar.visibility = View.GONE
                    if (dataWrapper.data?.state != EasyOrderConstants.EMPTY_STATE) {
                        Toast.makeText(this, "Esta mesa ya esta ocupada", Toast.LENGTH_LONG).show()
                    } else {
                        val codigoMesa = txt_codigo_mesa.text.toString()
                        viewModel.changeTableState(codigoMesa, EasyOrderConstants.OCCUPIED_STATE)
                    }
                }
                Status.ERROR -> {
                    progress_bar.visibility = View.GONE
                    Toast.makeText(applicationContext, UIMessages.ERROR_OBTENIENDO_MESA, Toast.LENGTH_LONG).show()
                }
            }
        })

        btn_ocupar_mesa.setOnClickListener({
            val codigoMesa = txt_codigo_mesa.text.toString()
            val stringArray = codigoMesa.split("/").toTypedArray()
            if (codigoMesa.isNotBlank() && stringArray.size == 2) {
                val restaurantId = stringArray.get(0)
                val tableId = stringArray.get(1)
                viewModel.getTable(restaurantId, tableId)
            } else {
                Toast.makeText(this, "El codigo de la mesa no es vàlido", Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Cancelado", Toast.LENGTH_LONG).show()
            } else {
                txt_codigo_mesa.setText(result.contents)
                Log.d(TAG, "El valor escaneado es: " + result.contents)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}