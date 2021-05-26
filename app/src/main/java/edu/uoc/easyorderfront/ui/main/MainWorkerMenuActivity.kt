package edu.uoc.easyorderfront.ui.main

import android.app.AlertDialog
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.navigation.NavigationView
import edu.uoc.easyorderfront.R
import edu.uoc.easyorderfront.data.SessionManager
import edu.uoc.easyorderfront.domain.model.Worker
import edu.uoc.easyorderfront.ui.constants.EasyOrderConstants
import edu.uoc.easyorderfront.ui.constants.UIMessages
import edu.uoc.easyorderfront.ui.menu.EditarMenuFragment
import edu.uoc.easyorderfront.ui.order.OrderWorkerFragment
import edu.uoc.easyorderfront.ui.profile.WorkerProfileFragment
import edu.uoc.easyorderfront.ui.restaurant.CreateRestaurantFragment
import edu.uoc.easyorderfront.ui.restaurant.RestaurantProfileFragment
import edu.uoc.easyorderfront.ui.table.TableListFragment
import edu.uoc.easyorderfront.ui.utils.OnTitleChangedListener
import kotlinx.android.synthetic.main.activity_main_worker_menu.*
import org.koin.android.viewmodel.ext.android.viewModel

class MainWorkerMenuActivity : AppCompatActivity(),
        NavigationView.OnNavigationItemSelectedListener,
        OnTitleChangedListener {

    private val viewModel: MainWorkerMenuViewModel by viewModel()
    private var currentFragment: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_worker_menu)

        currentFragment = intent.getStringExtra(EasyOrderConstants.FRAGMENT_KEY)

        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.isDrawerIndicatorEnabled = true
        toggle.syncState()

        navigation_view.setNavigationItemSelectedListener(this)

        showFragment()

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
                val fragment = WorkerProfileFragment.newInstance()
                replaceFragment(fragment)
            }
            R.id.nav_restaurant -> {
                val worker = SessionManager(applicationContext).getUser() as Worker
                if (worker.restaurant?.id != null) {
                    val fragment = RestaurantProfileFragment.newInstance(worker.restaurant?.id!!)
                    replaceFragment(fragment)
                } else {
                    Toast.makeText(this, UIMessages.ERROR_USUARIO_SIN_RESTAURANTE, Toast.LENGTH_LONG).show()
                }
            }
            R.id.nav_mesas -> {
                val worker = SessionManager(applicationContext).getUser() as Worker
                if (worker.restaurant?.id != null) {
                    val fragment = TableListFragment.newInstance(worker.restaurant?.id!!)
                    replaceFragment(fragment)
                } else {
                    Toast.makeText(this, UIMessages.ERROR_CARGANDO_RESTAURANTE, Toast.LENGTH_LONG).show()
                }
            }
            R.id.nav_menu -> {
                val worker = SessionManager(applicationContext).getUser() as Worker
                if (worker.restaurant?.id != null) {
                    val fragment = EditarMenuFragment.newInstance(worker.restaurant?.id!!)
                    replaceFragment(fragment)
                } else {
                    Toast.makeText(this, UIMessages.ERROR_CARGANDO_RESTAURANTE, Toast.LENGTH_LONG).show()
                }
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

    fun replaceFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.content_frame, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    public fun removeFragment(fragment: Fragment) {
        supportFragmentManager.popBackStack(fragment.getTag(), FragmentManager.POP_BACK_STACK_INCLUSIVE)
        supportFragmentManager.beginTransaction().remove(fragment).commit()
    }

    private fun showFragment() {
        when(currentFragment) {
            EasyOrderConstants.WORKER_PROFILE_FRAGMENT -> {
                val fragment = WorkerProfileFragment.newInstance()
                replaceFragment(fragment)
            }
            EasyOrderConstants.RESTAURANT_FRAGMENT -> {
                val restaurantId = intent.getStringExtra(EasyOrderConstants.RESTAURANT_ID_KEY)
                val fragment = RestaurantProfileFragment.newInstance(restaurantId)
                replaceFragment(fragment)
            }
            EasyOrderConstants.TABLE_LIST_FRAGMENT -> {
                val restaurantId = intent.getStringExtra(EasyOrderConstants.RESTAURANT_ID_KEY)
                val fragment = TableListFragment.newInstance(restaurantId)
                replaceFragment(fragment)
            }
        }
    }

    fun setItemMenu(item: Int) {
        val menuItem = navigation_view.menu.getItem(item)
        menuItem.setChecked(true)
    }

    override fun onAttachFragment(fragment: Fragment) {
        if (fragment is EditarMenuFragment) {
            fragment.setOnTitleChangedListener(this)
        } else if (fragment is OrderWorkerFragment) {
            fragment.setOnTitleChangedListener(this)
        } else if (fragment is WorkerProfileFragment) {
            fragment.setOnTitleChangedListener(this)
        } else if (fragment is CreateRestaurantFragment) {
            fragment.setOnTitleChangedListener(this)
        } else if (fragment is RestaurantProfileFragment) {
            fragment.setOnTitleChangedListener(this)
        } else if (fragment is TableListFragment) {
            fragment.setOnTitleChangedListener(this)
        }
    }

    override fun onTitleChanged(title: String) {
        supportActionBar?.title = title
    }
}