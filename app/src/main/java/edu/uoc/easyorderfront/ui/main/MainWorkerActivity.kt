package edu.uoc.easyorderfront.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import edu.uoc.easyorderfront.R
import kotlinx.android.synthetic.main.activity_main.*

class MainWorkerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_worker_register)
        configurarTabs()
        supportActionBar?.title = "Area de trabajadores"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main_worker, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.client_btn -> {
                val intent = Intent(this, MainClientActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    fun configurarTabs() {
        view_pager.adapter = MainWorkerPageAdapter(supportFragmentManager, applicationContext)
        tab_layout.setupWithViewPager(view_pager)
    }
}