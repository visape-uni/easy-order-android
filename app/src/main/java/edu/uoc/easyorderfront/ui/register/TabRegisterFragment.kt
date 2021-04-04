package edu.uoc.easyorderfront.ui.register

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import edu.uoc.easyorderfront.R
import edu.uoc.easyorderfront.ui.utils.Status
import kotlinx.android.synthetic.main.fragment_tab_register.*
import org.koin.android.viewmodel.ext.android.viewModel


class TabRegisterFragment : Fragment() {
    private val viewModel: TabRegisterViewModel by viewModel()
    private val TAG = "TabRegisterFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab_register, container, false)
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
                viewModel.register(username, email, clave)
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
                    progress_bar.visibility = View.GONE
                    logo.visibility = View.VISIBLE

                    Toast.makeText(context, "Sessión iniciada correctamente", Toast.LENGTH_LONG).show()

                    Log.d(TAG, dataWrapper.data.toString())
                    //TODO: Abrir activity segun el tipo de usuario
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

    companion object {

        @JvmStatic
        fun newInstance() =
            TabRegisterFragment().apply {

            }
    }
}