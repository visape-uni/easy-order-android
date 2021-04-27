package edu.uoc.easyorderfront.ui.profile


import android.content.ClipData
import android.content.ClipboardManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialog
import edu.uoc.easyorderfront.R
import edu.uoc.easyorderfront.data.SessionManager
import edu.uoc.easyorderfront.domain.model.Worker
import edu.uoc.easyorderfront.ui.constants.EasyOrderConstants
import edu.uoc.easyorderfront.ui.constants.UIMessages
import edu.uoc.easyorderfront.ui.table.CreateTableDialogFragment
import edu.uoc.easyorderfront.ui.utils.DataWrapper
import edu.uoc.easyorderfront.ui.utils.Status
import kotlinx.android.synthetic.main.activity_perfil_worker.*
import org.koin.android.viewmodel.ext.android.viewModel

class WorkerProfileActivity : AppCompatActivity() {
    private val viewModel: WorkerProfileViewModel by viewModel()
    private val TAG = "WorkerProfileActivity"

    private lateinit var bottomSheetDialog: BottomSheetDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_worker)

        prepareUI()
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.clear()
        if (viewModel.ownerMenu.value != null
                && viewModel.ownerMenu.value!!) {
            menuInflater.inflate(R.menu.menu_worker_profile, menu)
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.btn_add_table -> {
                val restaurant= (SessionManager(applicationContext).getUser() as Worker).restaurant
                if (restaurant != null) {
                    val createTableBottomActivity = CreateTableDialogFragment(restaurant)
                    createTableBottomActivity.show(supportFragmentManager, "TAG")
                } else {
                    Toast.makeText(applicationContext, "Error: El perfil del restaurante no se ha encontrado", Toast.LENGTH_LONG).show()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun prepareUI() {

        viewModel.ownerMenu.observe(this, { isOwner ->
            Log.d(TAG, "OwnerMenu $isOwner")
            // Si es el dueño, volver a cargar el menu
            if (isOwner != null && isOwner) {
                invalidateOptionsMenu()
            }
        })

        viewModel.workerProfile.observe(this, {dataWrapperUser ->
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
                        if (worker.isOwner != null && worker.isOwner) {
                            txt_tipo.text = getString(R.string.due_o)

                        } else {
                            txt_tipo.text = getString(R.string.trabajador)
                        }
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
            viewModel.ownerMenu.postValue(profile.isOwner)
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