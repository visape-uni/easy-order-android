package edu.uoc.easyorderfront.ui.register

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import edu.uoc.easyorderfront.R
import edu.uoc.easyorderfront.data.SessionManager
import edu.uoc.easyorderfront.domain.model.User
import edu.uoc.easyorderfront.ui.constants.EasyOrderConstants
import edu.uoc.easyorderfront.ui.utils.Status
import kotlinx.android.synthetic.main.fragment_tab_register_client.*
import kotlinx.android.synthetic.main.fragment_tab_register_client.email_txt
import kotlinx.android.synthetic.main.fragment_tab_register_client.logo
import kotlinx.android.synthetic.main.fragment_tab_register_client.progress_bar
import org.koin.android.viewmodel.ext.android.viewModel


class TabRegisterClientFragment : Fragment() {
    private val viewModel: TabRegisterViewModel by viewModel()
    private val TAG = "TabRegisterClientFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab_register_client, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareUI()
    }

    fun prepareUI() {

        viewModel.registered.observe(this, { dataWrapper ->

            when(dataWrapper.status) {
                Status.LOADING -> {
                    progress_bar.visibility = View.VISIBLE
                    logo.visibility = View.GONE
                }
                Status.SUCCESS -> {

                    Toast.makeText(context, "Registrado correctamente", Toast.LENGTH_SHORT).show()

                    context?.let {context ->
                        dataWrapper.data?.let { SessionManager(context).saveUser(it) }
                    }

                    login()
                }
                Status.ERROR -> {
                    progress_bar.visibility = View.GONE
                    logo.visibility = View.VISIBLE

                    Log.e(TAG, "Error registrando")
                    Toast.makeText(context, dataWrapper.message, Toast.LENGTH_SHORT).show()
                }
            }
        })

        registrar_btn.setOnClickListener(View.OnClickListener {

            val username = nombre_txt.text.toString()
            val email = email_txt.text.toString()
            val clave = contraseña_txt.text.toString()
            val claveRep = repetir_contraseña_txt.text.toString()

            if (username.isBlank()
                    || email.isBlank()
                    || clave.isBlank()
                    || claveRep.isBlank()) {
                // Si algun campo esta vacio
                Toast.makeText(context, getString(R.string.rellenar_todos_los_campos), Toast.LENGTH_LONG).show()
            } else if (clave != claveRep) {
                // Si las contraseñas no coinciden
                Toast.makeText(context, getString(R.string.contrase_as_deben_coincidir), Toast.LENGTH_LONG).show()
            } else if (clave.length < 8) {
                // Si contraseña menos de 8 caracteres
                Toast.makeText(context, getString(R.string.contrase_na_almenos_8_caracteres), Toast.LENGTH_LONG).show()
            } else {
                // No errores, hacer register
                viewModel.register(User(null, username, email, clave, true))
            }
        })

    }

    fun login () {
        viewModel.login(email_txt.text.toString(), contraseña_txt.text.toString())

        viewModel.login.observe(this, { dataWrapper ->

            when(dataWrapper.status) {
                Status.LOADING -> {
                    progress_bar.visibility = View.VISIBLE
                    logo.visibility = View.GONE
                }
                Status.SUCCESS -> {

                    Toast.makeText(context, getString(R.string.session_iniciada_correctamente), Toast.LENGTH_LONG).show()

                    Log.d(TAG, dataWrapper.data.toString())
                    dataWrapper.data?.uid?.let { saveUserId(it) }
                    getToken()
                }
                Status.ERROR -> {
                    progress_bar.visibility = View.GONE
                    logo.visibility = View.VISIBLE

                    Log.e(TAG, "Error iniciando sessión")
                    Toast.makeText(context, dataWrapper.message, Toast.LENGTH_LONG).show()
                }
            }


        })
    }

    fun saveUserId(uid: String) {
        context?.let { context ->
            SessionManager(context).saveUserId(uid)
        }
    }

    fun getToken() {
        viewModel.getTokenId()
        viewModel.getTokenResult.observe(this, { dataWrapper ->
            when (dataWrapper.status) {
                Status.LOADING -> {
                    progress_bar.visibility = View.VISIBLE
                    logo.visibility = View.GONE
                }
                Status.SUCCESS -> {
                    progress_bar.visibility = View.GONE
                    logo.visibility = View.VISIBLE
                    Log.d(TAG, "Token obtenido: " + dataWrapper.data)

                    if (dataWrapper.data != null) {
                        context?.let {context ->
                            dataWrapper.data.token?.let { token ->
                                // Save Token in sessionManager
                                SessionManager(context).saveAccessToken(token)

                                // Open client screen
                                val user = SessionManager(context).getUser()
                                if (user != null) {
                                    //TODO: Show Client Screen

                                } else {
                                    Toast.makeText(context, "Error obteniendo el perfil", Toast.LENGTH_LONG).show()
                                    Log.e(TAG, "Error obteniendo Perfil")
                                }
                            }
                        }
                    }
                }
                Status.ERROR -> {
                    progress_bar.visibility = View.GONE
                    logo.visibility = View.VISIBLE
                    Log.e(TAG, "Error obteniendo Token")
                }
            }
        })
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            TabRegisterClientFragment()
    }
}