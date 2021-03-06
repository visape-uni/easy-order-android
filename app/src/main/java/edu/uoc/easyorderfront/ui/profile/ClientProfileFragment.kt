package edu.uoc.easyorderfront.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import edu.uoc.easyorderfront.R
import edu.uoc.easyorderfront.data.SessionManager
import edu.uoc.easyorderfront.ui.constants.UIMessages
import edu.uoc.easyorderfront.ui.main.MainClientMenuActivity
import edu.uoc.easyorderfront.ui.table.OcupyTableFragment
import edu.uoc.easyorderfront.ui.utils.DataWrapper
import edu.uoc.easyorderfront.ui.utils.OnTitleChangedListener
import edu.uoc.easyorderfront.ui.utils.Status
import kotlinx.android.synthetic.main.activity_perfil_client.*
import org.koin.android.viewmodel.ext.android.viewModel

class ClientProfileFragment : Fragment() {

    private val viewModel: ClientProfileViewModel by viewModel()
    private val TAG = "WorkerProfileActivity"

    internal lateinit var callback: OnTitleChangedListener

    fun setOnTitleChangedListener(callback: OnTitleChangedListener) {
        this.callback = callback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        callback.onTitleChanged(UIMessages.TITLE_CLIENT_PROFILE)
        (activity as MainClientMenuActivity).setItemMenu(0)
        return inflater.inflate(R.layout.activity_perfil_client, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Preparar Vista
        prepareUI()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_client_profile, menu)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.btn_table -> {
                val fragmentTag = OcupyTableFragment::class.qualifiedName.toString()
                val fragment = OcupyTableFragment.newInstance()
                (activity as MainClientMenuActivity).replaceFragment(fragment, fragmentTag)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun prepareUI() {
        viewModel.clientProfile.observe(viewLifecycleOwner, {dataWrapperUser ->
            when (dataWrapperUser.status) {
                Status.LOADING -> {
                    progress_bar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    Log.d(TAG, dataWrapperUser.data.toString())
                    val client = dataWrapperUser.data

                    if(client != null) {
                        txt_id.text = client.uid
                        txt_nombre.text = client.username
                        txt_email.text = client.email
                        txt_tipo.text = getString(R.string.client)

                        txt_alergia.visibility = View.VISIBLE
                        txt_alergia.text = getString(R.string.no_has_a_adido_ninguna_alergia)
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
            viewModel.clientProfile.postValue(DataWrapper.success(profile))
        } else {
            // Si falla obteniendo perfil de SessionManager
            val uid = SessionManager(requireContext()).getUserId()
            if (uid != null) {
                viewModel.getClientProfile(uid)
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
            ClientProfileFragment().apply {
            }
    }
}