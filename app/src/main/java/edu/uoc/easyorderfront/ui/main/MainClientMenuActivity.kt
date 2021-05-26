package edu.uoc.easyorderfront.ui.main

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.navigation.NavigationView
import com.google.zxing.integration.android.IntentIntegrator
import edu.uoc.easyorderfront.R
import edu.uoc.easyorderfront.data.SessionManager
import edu.uoc.easyorderfront.ui.constants.EasyOrderConstants
import edu.uoc.easyorderfront.ui.constants.UIMessages
import edu.uoc.easyorderfront.ui.menu.MenuRestaurantActivity
import edu.uoc.easyorderfront.ui.profile.ClientProfileFragment
import edu.uoc.easyorderfront.ui.table.OcupyTableFragment
import edu.uoc.easyorderfront.ui.utils.OnTitleChangedListener
import edu.uoc.easyorderfront.ui.utils.Status
import kotlinx.android.synthetic.main.activity_main_client_menu.*
import kotlinx.android.synthetic.main.activity_ocupar_mesa.txt_codigo_mesa
import org.koin.android.viewmodel.ext.android.viewModel

class MainClientMenuActivity : AppCompatActivity(),
        NavigationView.OnNavigationItemSelectedListener,
        OnTitleChangedListener {

    private val viewModel: MainClientMenuViewModel by viewModel()
    private var currentFragment: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_client_menu)

        currentFragment = intent.getStringExtra(EasyOrderConstants.FRAGMENT_KEY)

        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.isDrawerIndicatorEnabled = true
        toggle.syncState()

        navigation_view.setNavigationItemSelectedListener(this)

        showFragment()

        prepareUI()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            if (supportFragmentManager.backStackEntryCount > 1) {
                super.onBackPressed()
            } else {
                logout()
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_user -> {
                val fragmentTag = ClientProfileFragment::class.qualifiedName.toString()
                val fragment = ClientProfileFragment.newInstance()
                replaceFragment(fragment, fragmentTag)
            }
            R.id.nav_ocupar_mesa -> {
                val fragmentTag = OcupyTableFragment::class.qualifiedName.toString()
                val fragment  = OcupyTableFragment.newInstance()
                replaceFragment(fragment, fragmentTag)
            }
            R.id.nav_cerrar_sesion -> {
                logout()
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun logout() {
        val dialog = AlertDialog.Builder(this)
                .setTitle("Cerrar sesión")
                .setMessage("Estas seguro que quieres cerrar sesión?")
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton("Si") { dialog, _ ->
                    viewModel.signOut()
                    SessionManager(applicationContext).clearAccessToken()
                    SessionManager(applicationContext).clearUser()
                    SessionManager(applicationContext).clearUserId()

                    dialog.dismiss()
                    finish()
                }
        dialog.show()
    }


    fun replaceFragment(fragment: Fragment, fragmentTag: String){

        supportFragmentManager.popBackStack(fragmentTag, FragmentManager.POP_BACK_STACK_INCLUSIVE)

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.content_frame, fragment, fragmentTag)
                .addToBackStack(fragmentTag).commit()

    }

    private fun showFragment() {

        when(currentFragment) {
            EasyOrderConstants.CLIENT_PROFILE_FRAGMENT -> {
                val fragmentTag = ClientProfileFragment::class.qualifiedName.toString()
                val fragment  = ClientProfileFragment.newInstance()
                replaceFragment(fragment, fragmentTag)
            }
        }
    }

    private fun prepareUI() {
        viewModel.clientProfile.observe(this, {dataWrapper ->
            when(dataWrapper.status) {
                Status.LOADING -> {
                    progress_bar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    progress_bar.visibility = View.GONE

                    val user = dataWrapper.data
                    if (user != null) {
                        SessionManager(applicationContext).saveUser(user)
                        if (user.tableId != null && user.tableId.isNotBlank()) {

                            val splitTableId = user.tableId.split("/")

                            val restaurantId = splitTableId.get(0)
                            val tableId = user.tableId

                            val intent = Intent(applicationContext, MenuRestaurantActivity::class.java)
                            intent.putExtra(EasyOrderConstants.RESTAURANT_ID_KEY, restaurantId)
                            intent.putExtra(EasyOrderConstants.TABLE_ID_KEY, tableId)
                            startActivity(intent)
                        }
                    } else {
                        Toast.makeText(applicationContext, getString(R.string.error_usuario_incorrect), Toast.LENGTH_LONG).show()
                    }
                }
                Status.ERROR -> {
                    progress_bar.visibility = View.GONE
                    Toast.makeText(applicationContext, UIMessages.ERROR_CARGANDO_PERFIL, Toast.LENGTH_LONG).show()
                }
            }
        })

        val user = SessionManager(applicationContext).getUser()
        if (user != null && user.uid != null) {
            viewModel.getClientProfile(user.uid!!)
        } else {
            Toast.makeText(applicationContext, getString(R.string.error_usuario_incorrect), Toast.LENGTH_LONG).show()
        }
    }

    fun setItemMenu(item: Int) {
        val menuItem = navigation_view.menu.getItem(item)
        menuItem.setChecked(true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(applicationContext, "Cancelado", Toast.LENGTH_LONG).show()
            } else {
                txt_codigo_mesa.setText(result.contents)
                Log.d("OcupyTableFragment", "El valor escaneado es: " + result.contents)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onAttachFragment(fragment: Fragment) {
        if (fragment is ClientProfileFragment) {
            fragment.setOnTitleChangedListener(this)
        } else if (fragment is OcupyTableFragment) {
            fragment.setOnTitleChangedListener(this)
        }
    }

    override fun onTitleChanged(title: String) {
        supportActionBar?.title = title
    }
}