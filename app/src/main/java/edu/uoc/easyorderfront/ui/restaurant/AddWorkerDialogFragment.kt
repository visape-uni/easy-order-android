package edu.uoc.easyorderfront.ui.restaurant

import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import edu.uoc.easyorderfront.R
import edu.uoc.easyorderfront.data.SessionManager
import edu.uoc.easyorderfront.domain.model.Restaurant
import edu.uoc.easyorderfront.domain.model.Worker
import edu.uoc.easyorderfront.ui.constants.UIMessages
import edu.uoc.easyorderfront.ui.utils.DataWrapper
import edu.uoc.easyorderfront.ui.utils.Status
import kotlinx.android.synthetic.main.bottom_fragment_add_worker.*
import kotlinx.android.synthetic.main.bottom_fragment_add_worker.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class AddWorkerDialogFragment(
        private val restaurantLive: MutableLiveData<DataWrapper<Restaurant>>
) : BottomSheetDialogFragment() {

    private val viewModel: AddWorkerDialogViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.bottom_fragment_add_worker, container, false)

        prepareUI(view)

        return view
    }

    fun prepareUI(view: View) {

        viewModel.created.observe(this, { dataWrapper ->
            when(dataWrapper.status) {
                Status.LOADING -> {
                    view.progress_bar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    view.progress_bar.visibility = View.GONE
                    if (dataWrapper.data != null) {
                        Toast.makeText(context, "Trabajador añadido correctamente", Toast.LENGTH_LONG)
                                .show()
                        addWorkerToRestaurant(dataWrapper.data)
                        dismiss()
                    }
                }
                Status.ERROR -> {
                    view.progress_bar.visibility = View.GONE
                    if (dataWrapper.message?.equals(UIMessages.ERROR_GENERICO)!!) {
                        Toast.makeText(context, UIMessages.ERROR_AÑADIENDO_TRABAJADOR, Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context, dataWrapper.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        })


        view.btn_paste.setOnClickListener({
            val clipboardManager = activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val item = clipboardManager.primaryClip?.getItemAt(0)
            txt_id_worker.setText(item?.text)
        })

        view.btn_cancelar.setOnClickListener({
            dismiss()
        })

        view.btn_añadir.setOnClickListener({
            val id_worker = txt_id_worker.text.toString().trim()
            if (id_worker.isBlank()) {
                Toast.makeText(context, getString(R.string.introducir_id_trabajador), Toast.LENGTH_LONG).show()
            } else {
                val workers = restaurantLive.value?.data?.workers
                var found = false

                for (worker in workers!!) {
                    if (worker.uid.equals(id_worker)) {
                        found = true
                    }
                }
                if (found) {
                    Toast.makeText(context, "El trabajador ya existe", Toast.LENGTH_LONG).show()
                } else {
                    viewModel.addWorkerRestaurant(restaurantLive.value?.data?.id!!, id_worker)
                }
            }
        })
    }

    fun addWorkerToRestaurant(worker: Worker) {
        restaurantLive.value?.data?.workers?.add(worker)
        restaurantLive.postValue(DataWrapper.success(restaurantLive.value?.data))
        SessionManager(requireContext()).addWorker(worker)
    }
}