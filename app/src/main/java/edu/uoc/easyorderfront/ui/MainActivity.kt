package edu.uoc.easyorderfront.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import edu.uoc.easyorderfront.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        configurarTabs()
    }

    fun configurarTabs() {
        view_pager.adapter = MainPageAdapter(supportFragmentManager, applicationContext)
        tab_layout.setupWithViewPager(view_pager)
    }
}