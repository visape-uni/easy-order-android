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
        //TODO: Observe workerProfile from viewModel
        viewModel.workerProfile.observe(this, {dataWrapper ->
            when(dataWrapper.status) {
                Status.LOADING -> {

                }
                Status.SUCCESS -> {
                    Log.d(TAG, dataWrapper.data.toString())
                    val worker = dataWrapper.data

                    if (worker != null) {
                        txt_id.text = worker.uid
                        txt_nombre.text = worker.username
                        txt_email.text = worker.email
                        txt_tipo.text = "Trabajador"

                        //TODO: GET RESTAURANT Y PONER NOMBRE DEL RESTAURANTE
                    }

                }
                Status.ERROR -> {

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