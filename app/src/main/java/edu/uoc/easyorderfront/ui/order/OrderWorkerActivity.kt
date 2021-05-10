package edu.uoc.easyorderfront.ui.order

import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
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
import java.util.*
import java.util.concurrent.TimeUnit

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
                generateQR()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun prepareUI() {
        viewModel.lastOrder.observe(this, { dataWrapper ->
            when(dataWrapper.status) {
                Status.LOADING -> {
                    progress_bar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    val order = dataWrapper.data

                    var timeDif: String

                    if (order != null) {
                        val orderStartedMilis = order.startedTime
                        if(orderStartedMilis != null) {
                            val actualTimeMilis = Calendar.getInstance().timeInMillis

                            val difMillis = (actualTimeMilis - orderStartedMilis)

                            timeDif = getHours(difMillis) + ":" + getMinutes(difMillis)
                        } else {
                            timeDif = "Desconocido"
                        }

                        if (order.orderedDishes != null && !order.orderedDishes.isEmpty()) {
                            txt_pedido.visibility = View.GONE
                            //TODO: LISTA CON LOS PLATOS
                        } else {
                            txt_pedido.visibility = View.VISIBLE
                        }

                        txt_tiempo.text = timeDif
                        txt_total.text = order.price.toString() + "€"
                    }
                    progress_bar.visibility = View.GONE
                }
                Status.ERROR -> {
                    progress_bar.visibility = View.GONE
                    Log.e(TAG, "Error obteniendo comanda")
                    Toast.makeText(applicationContext, dataWrapper.message, Toast.LENGTH_SHORT).show()
                }
            }

        })

        viewModel.table.observe(this, { dataWrapper ->
            when(dataWrapper.status) {
                Status.LOADING -> {
                    progress_bar.visibility = View.VISIBLE
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
                            txt_estado.text = getString(R.string.la_mesa_esta_ocupada)
                            viewModel.getLastOrderFromTable(table.tableRef!!)
                        }
                    }
                }
                Status.ERROR -> {
                    progress_bar.visibility = View.GONE
                    Log.e(TAG, "Error obteniendo mesa")
                    Toast.makeText(applicationContext, dataWrapper.message, Toast.LENGTH_SHORT).show()
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

    private fun getHours(millis: Long): String {
        return String.format("%02d", TimeUnit.MILLISECONDS.toHours(millis))
    }

    private fun getMinutes(millis: Long): String {
        return String.format("%02d", TimeUnit.MILLISECONDS.toMinutes(millis) -
                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)))
    }
}