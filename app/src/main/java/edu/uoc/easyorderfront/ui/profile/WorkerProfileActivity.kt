package edu.uoc.easyorderfront.ui.profile


import android.content.ClipData
import android.content.ClipboardManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import edu.uoc.easyorderfront.R
import edu.uoc.easyorderfront.data.SessionManager
import edu.uoc.easyorderfront.domain.model.Worker
import edu.uoc.easyorderfront.ui.constants.EasyOrderConstants
import edu.uoc.easyorderfront.ui.constants.UIMessages
import edu.uoc.easyorderfront.ui.utils.DataWrapper
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
                    progress_bar.visibility = View.VISIBLE
                    window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                }
                Status.SUCCESS -> {
                    Log.d(TAG, dataWrapperUser.data.toString())
                    val worker = dataWrapperUser.data

                    if (worker != null) {
                        txt_id.text = worker.uid
                        txt_nombre.text = worker.username
                        txt_email.text = worker.email
                        txt_tipo.text = getString(R.string.trabajador)
                        txt_restaurante.text = worker.restaurant?.name
                                ?: getString(R.string.no_trabaja_en_restaurante_actualmente)


                        btn_copy.setOnClickListener({
                            val clipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText(EasyOrderConstants.WORKERID_LABEL, worker.uid)
                            clipboardManager.setPrimaryClip(clip)
                            Toast.makeText(applicationContext, "ID del trabajador copiado correctamente", Toast.LENGTH_LONG).show()
                        })
                        progress_bar.visibility = View.GONE
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
            viewModel.workerProfile.postValue(DataWrapper.success(profile as Worker))
        } else {
            // Si falla obteniendo perfil de SessionManager
            val uid = SessionManager(applicationContext).getUserId()
            if (uid != null) {
                viewModel.getWorkerProfile(uid)
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