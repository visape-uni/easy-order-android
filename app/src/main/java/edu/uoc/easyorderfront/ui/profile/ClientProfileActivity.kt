package edu.uoc.easyorderfront.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import edu.uoc.easyorderfront.R
import edu.uoc.easyorderfront.data.SessionManager
import edu.uoc.easyorderfront.ui.constants.UIMessages
import edu.uoc.easyorderfront.ui.table.OcupyTableActivity
import edu.uoc.easyorderfront.ui.utils.DataWrapper
import edu.uoc.easyorderfront.ui.utils.Status
import kotlinx.android.synthetic.main.activity_perfil_client.*
import org.koin.android.viewmodel.ext.android.viewModel

class ClientProfileActivity : AppCompatActivity() {

    private val viewModel: ClientProfileViewModel by viewModel()
    private val TAG = "WorkerProfileActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_client)

        prepareUI()
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_client_profile, menu)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.btn_table -> {
                startActivity(Intent(this, OcupyTableActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun prepareUI() {
        viewModel.clientProfile.observe(this, {dataWrapperUser ->
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

                        //TODO: Mirar si el usuario tiene alergias, si no tiene mostrar texto de no alergias
                        //TODO: Si tiene, mostrar el listado en el recycled view
                        txt_alergia.visibility = View.VISIBLE
                        txt_alergia.text = getString(R.string.no_has_a_adido_ninguna_alergia)
                    }
                }
                Status.ERROR -> {
                    progress_bar.visibility = View.GONE
                    Toast.makeText(applicationContext, UIMessages.ERROR_CARGANDO_PERFIL, Toast.LENGTH_LONG).show()
                }
            }
        })

        val profile = SessionManager(applicationContext).getUser()
        if (profile != null && profile.uid != null) {
            viewModel.clientProfile.postValue(DataWrapper.success(profile))
        } else {
            // Si falla obteniendo perfil de SessionManager
            val uid = SessionManager(applicationContext).getUserId()
            if (uid != null) {
                viewModel.getClientProfile(uid)
            } else {
                progress_bar.visibility = View.GONE
                Toast.makeText(
                        applicationContext,
                        UIMessages.ERROR_SESSION_EXPIRADA,
                        Toast.LENGTH_LONG
                ).show()
            }
        }

    }
}