package edu.uoc.easyorderfront.ui.order

import android.content.Context.WINDOW_SERVICE
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View
import android.widget.Toast
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.fragment.app.Fragment
import edu.uoc.easyorderfront.R
import edu.uoc.easyorderfront.domain.model.Table
import edu.uoc.easyorderfront.ui.constants.EasyOrderConstants
import edu.uoc.easyorderfront.ui.utils.DataWrapper
import edu.uoc.easyorderfront.ui.utils.Status
import kotlinx.android.synthetic.main.activity_order_worker.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*
import java.util.concurrent.TimeUnit

class OrderWorkerFragment : Fragment() {
    private val viewModel: OrderWorkerViewModel by viewModel()

    private val TAG = "OrderWorkerActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_order_worker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Preparar Vista
        prepareUI()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_order_worker, menu)
        super.onCreateOptionsMenu(menu, inflater)
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
            when (dataWrapper.status) {
                Status.LOADING -> {
                    progress_bar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    val order = dataWrapper.data

                    var timeDif: String

                    if (order != null) {
                        val orderStartedMilis = order.startedTime
                        if (orderStartedMilis != null) {
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
                    Toast.makeText(context, dataWrapper.message, Toast.LENGTH_SHORT).show()
                }
            }

        })

        viewModel.table.observe(this, { dataWrapper ->
            when (dataWrapper.status) {
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
                    Toast.makeText(context, dataWrapper.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
        val table = arguments?.getSerializable(EasyOrderConstants.TABLE_ID_KEY) as Table
        viewModel.table.postValue(DataWrapper.success(table))

    }

    fun generateQR() {

        val manager = activity?.getSystemService(WINDOW_SERVICE) as WindowManager
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
        dimen = dimen * 3 / 4

        val tableID = viewModel.table.value?.data?.tableRef
        val qrgEncoder = QRGEncoder(tableID, null, QRGContents.Type.TEXT, dimen)

        val bitmap = qrgEncoder.encodeAsBitmap()

        val getQrCodeActivity = GetQrCodeActivity(bitmap)
        getQrCodeActivity.show(fragmentManager!!, "TAG")
    }

    companion object {
        @JvmStatic
        fun newInstance(table: Table) =
                OrderWorkerFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(EasyOrderConstants.TABLE_ID_KEY, table)
                    }
                }


        private fun getHours(millis: Long): String {
            return String.format("%02d", TimeUnit.MILLISECONDS.toHours(millis))
        }

        private fun getMinutes(millis: Long): String {
            return String.format("%02d", TimeUnit.MILLISECONDS.toMinutes(millis) -
                    TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)))
        }
    }
}