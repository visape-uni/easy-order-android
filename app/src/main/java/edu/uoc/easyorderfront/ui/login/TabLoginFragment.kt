package edu.uoc.easyorderfront.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import edu.uoc.easyorderfront.R
import edu.uoc.easyorderfront.ui.recovery.PasswordRecoveryActivity
import edu.uoc.easyorderfront.ui.utils.Status
import kotlinx.android.synthetic.main.fragment_tab_login.*
import kotlinx.android.synthetic.main.fragment_tab_login.email_txt
import org.koin.android.viewmodel.ext.android.viewModel

class TabLoginFragment : Fragment() {
    private val viewModel: TabLoginViewModel by viewModel()
    private val TAG = "TabLoginFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Preparar Vista
        prepareUI()

    }

    fun prepareUI() {

        // Si pulsa el boton de contraseña oblidada se abre la activity de recuperación
        forgot_password_txt.setOnClickListener(View.OnClickListener {
            startActivity(Intent(context, PasswordRecoveryActivity::class.java))
        })

        entrar_btn.setOnClickListener(View.OnClickListener {
            val email = email_txt.text.toString()
            val password = password_txt.text.toString()

            if (email.isBlank()) {
                Toast.makeText(context, getString(R.string.introducir_email), Toast.LENGTH_LONG).show()
            } else if (password.isBlank()) {
                Toast.makeText(context, getString(R.string.introducir_contrase_a), Toast.LENGTH_LONG).show()
            } else {
                login(email, password)
            }
        })
    }

    fun login(email:String, password:String) {
        viewModel.login(email, password)

        viewModel.isLogged.observe(this, { dataWrapper ->

            when(dataWrapper.status) {
                Status.LOADING -> {
                    progress_bar.visibility = View.VISIBLE
                    logo.visibility = View.GONE
                }
                Status.SUCCESS -> {
                    progress_bar.visibility = View.GONE
                    logo.visibility = View.VISIBLE

                    Toast.makeText(context, "Sessión iniciada correctamente", Toast.LENGTH_SHORT).show()

                    Log.d(TAG, dataWrapper.data.toString())
                    //TODO: Abrir activity segun el tipo de usuario
                }
                Status.ERROR -> {
                    progress_bar.visibility = View.GONE
                    logo.visibility = View.VISIBLE

                    Log.e(TAG, "Error iniciando sessión")
                    Toast.makeText(context, dataWrapper.message, Toast.LENGTH_SHORT).show()
                }
            }


        })
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            TabLoginFragment().apply {
            }
    }
}