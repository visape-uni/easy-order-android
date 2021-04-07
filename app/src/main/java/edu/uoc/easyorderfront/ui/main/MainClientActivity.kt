package edu.uoc.easyorderfront.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import edu.uoc.easyorderfront.R
import kotlinx.android.synthetic.main.activity_main.*


class MainClientActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
            }
        }

        return super.onOptionsItemSelected(item)
    }

    fun configurarTabs() {
        view_pager.adapter = MainClientPageAdapter(supportFragmentManager, applicationContext)
        tab_layout.setupWithViewPager(view_pager)
    }
}