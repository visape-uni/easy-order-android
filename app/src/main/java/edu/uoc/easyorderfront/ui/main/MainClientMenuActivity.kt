package edu.uoc.easyorderfront.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.navigation.NavigationView
import com.google.zxing.integration.android.IntentIntegrator
import edu.uoc.easyorderfront.R
import edu.uoc.easyorderfront.ui.constants.EasyOrderConstants
import edu.uoc.easyorderfront.ui.profile.ClientProfileFragment
import edu.uoc.easyorderfront.ui.table.OcupyTableFragment
import kotlinx.android.synthetic.main.activity_main_client_menu.*
import kotlinx.android.synthetic.main.activity_ocupar_mesa.*

class MainClientMenuActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var currentFragment: String? = null
    val MENU_RESTAURANT_FRAGMENT_NAME = "edu.uoc.easyorderfront.ui.menu.MenuRestaurantFragment"

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
                val fragmentTag = ClientProfileFragment::class.qualifiedName.toString()
                val fragment = ClientProfileFragment.newInstance()
                replaceFragment(fragment, fragmentTag)
            }
            R.id.nav_ocupar_mesa -> {
                val fragmentTag = OcupyTableFragment::class.qualifiedName.toString()
                val fragment  = OcupyTableFragment.newInstance()
                replaceFragment(fragment, fragmentTag)
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }


    public fun replaceFragment(fragment: Fragment, fragmentTag: String){

        supportFragmentManager.popBackStack(fragmentTag, FragmentManager.POP_BACK_STACK_INCLUSIVE)

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.content_frame, fragment, fragmentTag).addToBackStack(fragmentTag).commit()

    }

    public fun removeFragment(fragment: Fragment) {
            supportFragmentManager.popBackStack(fragment.getTag(), FragmentManager.POP_BACK_STACK_INCLUSIVE)
            supportFragmentManager.beginTransaction().remove(fragment).commit()
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

    public fun setItemMenu(item: Int) {
        val menuItem = navigation_view.menu.getItem(item)
        menuItem.setChecked(true)
    }

    private fun setCurrentFragmentTitle() {
        //TODO: SET TITLE
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
}