package edu.uoc.easyorderfront.ui.main

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import edu.uoc.easyorderfront.R
import edu.uoc.easyorderfront.data.SessionManager
import edu.uoc.easyorderfront.domain.model.Worker
import edu.uoc.easyorderfront.ui.constants.EasyOrderConstants
import edu.uoc.easyorderfront.ui.constants.UIMessages
import edu.uoc.easyorderfront.ui.profile.WorkerProfileFragment
import edu.uoc.easyorderfront.ui.restaurant.RestaurantProfileFragment
import edu.uoc.easyorderfront.ui.table.TableListFragment
import kotlinx.android.synthetic.main.activity_main_worker_menu.*

class MainWorkerMenuActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var currentFragment: String? = null

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
                //TODO: CERRAR SESION (LIMPIAR SESSION MANAGER)
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
                    val fragment = RestaurantProfileFragment.newInstance(worker.restaurant?.id)
                    replaceFragment(fragment)
                } else {
                    Toast.makeText(this, UIMessages.ERROR_CARGANDO_RESTAURANTE, Toast.LENGTH_LONG).show()
                }
            }
            R.id.nav_mesas -> {
                val worker = SessionManager(applicationContext).getUser() as Worker
                if (worker.restaurant?.id != null) {
                    val fragment = TableListFragment.newInstance(worker.restaurant?.id)
                    replaceFragment(fragment)
                } else {
                    Toast.makeText(this, UIMessages.ERROR_CARGANDO_RESTAURANTE, Toast.LENGTH_LONG).show()
                }
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    public fun replaceFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.content_frame, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    private fun showFragment() {
        when(currentFragment) {
            EasyOrderConstants.WORKER_PROFILE_FRAGMENT -> {
                val fragment = WorkerProfileFragment.newInstance()
                replaceFragment(fragment)
                val menuItem = navigation_view.menu.getItem(0)
                menuItem.setChecked(true)
            }
            EasyOrderConstants.RESTAURANT_FRAGMENT -> {
                val restaurantId = intent.getStringExtra(EasyOrderConstants.RESTAURANT_ID_KEY)
                val fragment = RestaurantProfileFragment.newInstance(restaurantId)
                replaceFragment(fragment)
                val menuItem = navigation_view.menu.getItem(1)
                menuItem.setChecked(true)
            }
            EasyOrderConstants.TABLE_LIST_FRAGMENT -> {
                val restaurantId = intent.getStringExtra(EasyOrderConstants.RESTAURANT_ID_KEY)
                val fragment = TableListFragment.newInstance(restaurantId)
                replaceFragment(fragment)
                val menuItem = navigation_view.menu.getItem(2)
                menuItem.setChecked(true)
            }
        }
    }

    private fun setCurrentFragmentTitle() {
        //TODO: SET TITLE
    }
}