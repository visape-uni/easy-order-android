package edu.uoc.easyorderfront.ui.profile


import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import edu.uoc.easyorderfront.R
import edu.uoc.easyorderfront.data.SessionManager
import edu.uoc.easyorderfront.domain.model.Worker
import edu.uoc.easyorderfront.ui.constants.EasyOrderConstants
import edu.uoc.easyorderfront.ui.constants.UIMessages
import edu.uoc.easyorderfront.ui.main.MainWorkerMenuActivity
import edu.uoc.easyorderfront.ui.restaurant.CreateRestaurantFragment
import edu.uoc.easyorderfront.ui.utils.DataWrapper
import edu.uoc.easyorderfront.ui.utils.OnTitleChangedListener
import edu.uoc.easyorderfront.ui.utils.Status
import kotlinx.android.synthetic.main.activity_perfil_worker.*
import org.koin.android.viewmodel.ext.android.viewModel

class WorkerProfileFragment : Fragment() {
    private val viewModel: WorkerProfileViewModel by viewModel()
    private val TAG = "WorkerProfileActivity"

    private lateinit var bottomSheetDialog: BottomSheetDialog

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
        callback.onTitleChanged(UIMessages.TITLE_WORKER_PROFILE)
        (activity as MainWorkerMenuActivity).setItemMenu(0)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_perfil_worker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Preparar Vista
        prepareUI()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        if (viewModel.restaurantMenu.value != null
                && viewModel.restaurantMenu.value!!) {
            inflater.inflate(R.menu.menu_worker_profile, menu)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.btn_create_restaurant -> {
                val fragment = CreateRestaurantFragment.newInstance()
                (activity as MainWorkerMenuActivity).replaceFragment(fragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }


    fun prepareUI() {

        viewModel.restaurantMenu.observe(viewLifecycleOwner, { showMenu ->
            Log.d(TAG, "restaurantMenu $showMenu")
            activity?.invalidateOptionsMenu()
        })

        viewModel.workerProfile.observe(viewLifecycleOwner, {dataWrapperUser ->
            when(dataWrapperUser.status) {
                Status.LOADING -> {
                    progress_bar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    Log.d(TAG, dataWrapperUser.data.toString())
                    val worker = dataWrapperUser.data

                    if (worker != null) {
                        txt_id.text = worker.uid
                        txt_nombre.text = worker.username
                        txt_email.text = worker.email
                        if (worker.isOwner != null && worker.isOwner!!) {
                            txt_tipo.text = getString(R.string.due_o)

                        } else {
                            txt_tipo.text = getString(R.string.trabajador)
                        }
                        txt_restaurante.text = worker.restaurant?.name
                                ?: getString(R.string.no_trabaja_en_restaurante_actualmente)


                        btn_copy.setOnClickListener({
                            val clipboardManager = activity?.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText(EasyOrderConstants.WORKER_ID_LABEL, worker.uid)
                            clipboardManager.setPrimaryClip(clip)
                            Toast.makeText(context, "ID del trabajador copiado correctamente", Toast.LENGTH_LONG).show()
                        })

                        if ((worker.isOwner == null || !worker.isOwner!!)
                                && (worker.restaurant == null)) {
                            viewModel.restaurantMenu.postValue(true)
                        } else {
                            viewModel.restaurantMenu.postValue(false)
                        }

                        progress_bar.visibility = View.GONE
                    }

                }
                Status.ERROR -> {
                    progress_bar.visibility = View.GONE
                    Toast.makeText(context, UIMessages.ERROR_CARGANDO_PERFIL, Toast.LENGTH_LONG).show()
                }
            }
        })


        val profile = SessionManager(requireContext()).getUser()
        if (profile != null && profile.uid != null) {
            viewModel.workerProfile.postValue(DataWrapper.success(profile as Worker))
        } else {
            // Si falla obteniendo perfil de SessionManager
            val uid = SessionManager(requireContext()).getUserId()
            if (uid != null) {
                viewModel.getWorkerProfile(uid)
            } else {
                progress_bar.visibility = View.GONE
                Toast.makeText(
                    context,
                    UIMessages.ERROR_SESSION_EXPIRADA,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
    companion object {
        @JvmStatic
        fun newInstance() =
            WorkerProfileFragment().apply {
            }
    }
}