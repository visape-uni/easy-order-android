package edu.uoc.easyorderfront.ui.order

import android.graphics.Point
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.appcompat.app.AppCompatActivity
import edu.uoc.easyorderfront.R
import edu.uoc.easyorderfront.domain.model.Table
import edu.uoc.easyorderfront.ui.constants.EasyOrderConstants
import edu.uoc.easyorderfront.ui.utils.DataWrapper
import edu.uoc.easyorderfront.ui.utils.Status
import kotlinx.android.synthetic.main.activity_order_worker.*
import org.koin.android.viewmodel.ext.android.viewModel

class OrderWorkerActivity : AppCompatActivity() {
    private val viewModel: OrderWorkerViewModel by viewModel()

    private val TAG = "OrderWorkerActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_worker)

        prepareUI()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_order_worker, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.qr_btn -> {
                // TODO: obtener el codigo QR
                generateQR()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun prepareUI() {
        viewModel.table.observe(this, { dataWrapper ->

            when(dataWrapper.status) {
                Status.LOADING -> {

                }
                Status.SUCCESS -> {
                    val table = dataWrapper.data
                    if (table != null) {
                        txt_id.text = table.uid
                        txt_capacidad.text =
                            getString(R.string.num_personas, table.capacity.toString())

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
                Status.ERROR -> {

                }
            }
        })
        val table = intent.getSerializableExtra(EasyOrderConstants.TABLE_ID_KEY) as Table
        viewModel.table.postValue(DataWrapper.success(table))

    }

    fun generateQR() {

        val manager = getSystemService(WINDOW_SERVICE) as WindowManager
        val display = manager.defaultDisplay

        val point = Point()

        display.getSize(point)

        val width = point.x
        val height = point.y

        var dimen: Int
        if (width < height) {
            dimen = width
        } else {
            dimen = height
        }
        dimen = dimen * 3/4

        val tableID = viewModel.table.value?.data?.tableRef
        val qrgEncoder = QRGEncoder(tableID, null, QRGContents.Type.TEXT, dimen)

        val bitmap = qrgEncoder.encodeAsBitmap()

        val getQrCodeActivity = GetQrCodeActivity(bitmap)
        getQrCodeActivity.show(supportFragmentManager, "TAG")


    }
}