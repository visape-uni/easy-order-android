package edu.uoc.easyorderfront.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import edu.uoc.easyorderfront.R
import edu.uoc.easyorderfront.data.SessionManager
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel


class MainClientActivity : AppCompatActivity(){
    private val viewModel: MainClientViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        clearSessionManager()

        setSupportActionBar(toolbar)

        configurarTabs()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main_client, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.worker_btn -> {
                val intent = Intent(this, MainWorkerActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    fun configurarTabs() {
        view_pager.adapter = MainClientPageAdapter(supportFragmentManager, applicationContext)
        tab_layout.setupWithViewPager(view_pager)
    }

    fun clearSessionManager() {
        viewModel.signOut()
        val sessionManager = SessionManager(applicationContext)
        sessionManager.clearAccessToken()
        sessionManager.clearUser()
        sessionManager.clearUserId()
    }
}