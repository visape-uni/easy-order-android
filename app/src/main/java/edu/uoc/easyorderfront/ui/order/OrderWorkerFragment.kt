package edu.uoc.easyorderfront.ui.order

import android.app.AlertDialog
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
import androidx.recyclerview.widget.LinearLayoutManager
import edu.uoc.easyorderfront.R
import edu.uoc.easyorderfront.domain.model.Table
import edu.uoc.easyorderfront.ui.adapter.OrderClientAdapter
import edu.uoc.easyorderfront.ui.constants.EasyOrderConstants
import edu.uoc.easyorderfront.ui.constants.UIMessages
import edu.uoc.easyorderfront.ui.utils.DataWrapper
import edu.uoc.easyorderfront.ui.utils.OnTitleChangedListener
import edu.uoc.easyorderfront.ui.utils.Status
import kotlinx.android.synthetic.main.activity_order_worker.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*
import java.util.concurrent.TimeUnit

class OrderWorkerFragment : Fragment() {
    private val viewModel: OrderWorkerViewModel by viewModel()
    private lateinit var adapter : OrderClientAdapter

    private val TAG = "OrderWorkerActivity"

    internal lateinit var callback: OnTitleChangedListener

    fun setOnTitleChangedListener(callback: OnTitleChangedListener) {
        this.callback = callback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        callback.onTitleChanged(UIMessages.TITLE_ORDER_WORKER)
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
            R.id.change_state_btn -> {
                changeState()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun prepareUI() {
        initRecyclerView()

        viewModel.lastOrder.observe(viewLifecycleOwner, { dataWrapper ->
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

                        if (!order.orderedDishes.isNullOrEmpty()) {
                            txt_pedido.visibility = View.INVISIBLE
                            adapter.submitList(order.orderedDishes)
                        } else {
                            if (adapter.currentList.isEmpty()) {
                                txt_pedido.visibility = View.VISIBLE
                            }
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

        viewModel.table.observe(viewLifecycleOwner, { dataWrapper ->
            when (dataWrapper.status) {
                Status.LOADING -> {
                    progress_bar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    progress_bar.visibility = View.GONE
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
                            txt_pedido.visibility = View.GONE
                            recycler_view_pedido.visibility = View.GONE
                            lbl_total.visibility = View.GONE
                            txt_total.visibility = View.GONE
                        } else {
                            txt_estado.text = getString(R.string.la_mesa_esta_ocupada)
                            viewModel.getLastOrderFromTable(viewModel.tableRef!!)
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
        viewModel.tableRef = table.tableRef
        viewModel.table.postValue(DataWrapper.success(table))
    }

    fun changeState() {
        if (!viewModel.table.value?.data?.state.equals(EasyOrderConstants.EMPTY_STATE)) {
            val dialog = AlertDialog.Builder(context)
                .setTitle("Cambiar estado de la mesa")
                .setMessage(
                    "Estas seguro que quieres vaciar la mesa? " +
                            "Si la mesa tiene un pedido abierto se cancelará"
                )
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton("Si") { dialog, _ ->
                    val tableId = viewModel.tableRef
                    if (tableId != null) {
                        viewModel.changeTableState(tableId, "", EasyOrderConstants.EMPTY_STATE)
                    } else {
                        Toast.makeText(
                            context,
                            "La referencia de la mesa es incorrecta",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    dialog.dismiss()
                }

            dialog.show()
        } else {
            Toast.makeText(
                context,
                "La mesa ya esta vacia",
                Toast.LENGTH_LONG
            ).show()
        }
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

        val tableID = viewModel.tableRef
        val qrgEncoder = QRGEncoder(tableID, null, QRGContents.Type.TEXT, dimen)

        val bitmap = qrgEncoder.encodeAsBitmap()

        if (bitmap != null) {
            val getQrCodeActivity = GetQrCodeActivity(bitmap)
            getQrCodeActivity.show(fragmentManager!!, "TAG")
        } else {
            Toast.makeText(context, "Error generando código QR", Toast.LENGTH_LONG).show()
        }
    }

    fun initRecyclerView() {

        // Set Layout Manager
        recycler_view_pedido.layoutManager = LinearLayoutManager(context)

        adapter = OrderClientAdapter(null)
        // Set Adapter
        recycler_view_pedido.adapter = adapter
    }

    private fun getHours(millis: Long): String {
        return String.format("%02d", TimeUnit.MILLISECONDS.toHours(millis))
    }

    private fun getMinutes(millis: Long): String {
        return String.format("%02d", TimeUnit.MILLISECONDS.toMinutes(millis) -
                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)))
    }

    companion object {
        @JvmStatic
        fun newInstance(table: Table) =
                OrderWorkerFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(EasyOrderConstants.TABLE_ID_KEY, table)
                    }
                }
    }
}