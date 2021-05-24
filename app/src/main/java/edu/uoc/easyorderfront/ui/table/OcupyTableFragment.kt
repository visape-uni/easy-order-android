package edu.uoc.easyorderfront.ui.table

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.zxing.integration.android.IntentIntegrator
import edu.uoc.easyorderfront.R
import edu.uoc.easyorderfront.data.SessionManager
import edu.uoc.easyorderfront.ui.constants.EasyOrderConstants
import edu.uoc.easyorderfront.ui.constants.UIMessages
import edu.uoc.easyorderfront.ui.main.MainClientMenuActivity
import edu.uoc.easyorderfront.ui.menu.MenuRestaurantActivity
import edu.uoc.easyorderfront.ui.utils.OnTitleChangedListener
import edu.uoc.easyorderfront.ui.utils.Status
import kotlinx.android.synthetic.main.activity_ocupar_mesa.*
import org.koin.android.viewmodel.ext.android.viewModel

class OcupyTableFragment : Fragment() {
    private val TAG = "OcupyTableActivity"
    private val viewModel: OcupyTableViewModel by viewModel()

    lateinit var restaurantId: String
    lateinit var tableId: String

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
        callback.onTitleChanged(UIMessages.TITLE_OCUPAR_MESA)
        (activity as MainClientMenuActivity).setItemMenu(1)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_ocupar_mesa, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Preparar Vista
        prepareUI()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_ocupar_mesa, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.qr_btn -> {
                IntentIntegrator(activity).initiateScan()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun prepareUI() {
        viewModel.tableStateChanged.observe(viewLifecycleOwner, { dataWrapper ->
            when(dataWrapper.status) {
                Status.LOADING -> {
                    progress_bar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    progress_bar.visibility = View.GONE
                    Toast.makeText(context, "Mesa ${dataWrapper.data?.uid} ocupada correctamente", Toast.LENGTH_LONG).show()

                    /*val fragmentTag = MenuRestaurantFragment::class.qualifiedName.toString()
                    val fragment = MenuRestaurantFragment.newInstance(restaurantId)
                    (activity as MainClientMenuActivity).replaceFragment(fragment, fragmentTag)*/

                    val intent = Intent(context, MenuRestaurantActivity::class.java)
                    intent.putExtra(EasyOrderConstants.RESTAURANT_ID_KEY, restaurantId)
                    intent.putExtra(EasyOrderConstants.TABLE_ID_KEY, tableId)
                    startActivity(intent)

                }
                Status.ERROR -> {
                    progress_bar.visibility = View.GONE
                    Toast.makeText(context, UIMessages.ERROR_OCUPANDO_MESA, Toast.LENGTH_LONG).show()
                }
            }
        })

        viewModel.table.observe(viewLifecycleOwner, { dataWrapper ->
            when(dataWrapper.status) {
                Status.LOADING -> {
                    progress_bar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    progress_bar.visibility = View.GONE
                    if (dataWrapper.data?.state != EasyOrderConstants.EMPTY_TABLE_STATE) {
                        Toast.makeText(context, "Esta mesa ya esta ocupada", Toast.LENGTH_LONG).show()
                    } else {
                        val codigoMesa = txt_codigo_mesa.text.toString()
                        tableId = codigoMesa

                        val codigoMesaSplit = codigoMesa.split("/")

                        restaurantId = codigoMesaSplit.get(0)

                        val user = context?.let { SessionManager(it).getUser() }
                        if (user != null && user.uid != null) {
                            viewModel.changeTableState(codigoMesa, user.uid!!, EasyOrderConstants.OCCUPIED_TABLE_STATE)
                        } else {
                            Toast.makeText(context, "El usuario es incorrecto", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                Status.ERROR -> {
                    progress_bar.visibility = View.GONE
                    Toast.makeText(context, UIMessages.ERROR_OBTENIENDO_MESA, Toast.LENGTH_LONG).show()
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
                Toast.makeText(context, "El codigo de la mesa no es vàlido", Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(context, "Cancelado", Toast.LENGTH_LONG).show()
            } else {
                txt_codigo_mesa.setText(result.contents)
                Log.d(TAG, "El valor escaneado es: " + result.contents)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            OcupyTableFragment().apply {
            }
    }
}