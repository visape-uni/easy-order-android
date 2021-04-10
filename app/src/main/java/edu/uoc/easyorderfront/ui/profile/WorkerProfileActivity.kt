package edu.uoc.easyorderfront.ui.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.uoc.easyorderfront.R
import edu.uoc.easyorderfront.data.SessionManager
import edu.uoc.easyorderfront.ui.utils.Status
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