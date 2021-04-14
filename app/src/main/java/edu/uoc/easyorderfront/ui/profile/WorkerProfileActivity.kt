package edu.uoc.easyorderfront.ui.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import edu.uoc.easyorderfront.R
import edu.uoc.easyorderfront.data.SessionManager
import edu.uoc.easyorderfront.ui.utils.Status
import kotlinx.android.synthetic.main.activity_perfil_worker.*
import org.koin.android.viewmodel.ext.android.viewModel

class WorkerProfileActivity : AppCompatActivity() {
    private val viewModel: WorkerProfileViewModel by viewModel()
    private val TAG = "WorkerProfileActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_worker)

        prepareUI()
    }

    fun prepareUI() {

        viewModel.workerProfile.observe(this, {dataWrapperUser ->
            when(dataWrapperUser.status) {
                Status.LOADING -> {
                    //TODO: LOADING
                }
                Status.SUCCESS -> {
                    Log.d(TAG, dataWrapperUser.data.toString())
                    val worker = dataWrapperUser.data

                    if (worker != null) {
                        txt_id.text = worker.uid
                        txt_nombre.text = worker.username
                        txt_email.text = worker.email
                        txt_tipo.text = "Trabajador"
                        txt_restaurante.text = worker.restaurant?.name
                                ?: getString(R.string.no_trabaja_en_restaurante_actualmente)

                    }

                }
                Status.ERROR -> {
                    //TODO: ERROR
                }
            }
        })

        val uid = SessionManager(applicationContext).getUserId()
        if (uid != null) {
            viewModel.getWorkerProfile(uid)
        } else {
            //TODO: ERROR USER ID IS NULL TRATAR ERROR Y MENSAJE
        }
    }
}