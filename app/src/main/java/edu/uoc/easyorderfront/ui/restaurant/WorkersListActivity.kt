package edu.uoc.easyorderfront.ui.restaurant

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import edu.uoc.easyorderfront.R
import edu.uoc.easyorderfront.data.SessionManager
import edu.uoc.easyorderfront.domain.model.Restaurant
import edu.uoc.easyorderfront.ui.adapter.WorkersAdapter
import edu.uoc.easyorderfront.ui.constants.EasyOrderConstants
import edu.uoc.easyorderfront.ui.constants.UIMessages
import edu.uoc.easyorderfront.ui.utils.DataWrapper
import kotlinx.android.synthetic.main.activity_workers_list.*
import org.koin.android.viewmodel.ext.android.viewModel

class WorkersListActivity : AppCompatActivity() {
    private val TAG = "WorkerListActivity"
    private lateinit var adapter : WorkersAdapter

    private val viewModel : WorkerListViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workers_list)

        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = UIMessages.TITLE_WORKERS_LIST

        prepareUI()
    }

    override fun onResume() {
        updateWorkersList()
        super.onResume()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_workers_list, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                finish()
            }
            R.id.btn_add_worker -> {
                val addWorkerDialog = AddWorkerDialogFragment(viewModel.restaurantLiveData)
                addWorkerDialog.show(supportFragmentManager, "TAG")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun prepareUI() {

        viewModel.restaurantLiveData.observe(this, { rest ->
            updateWorkersList()
        })

        val restaurant = intent.getSerializableExtra(EasyOrderConstants.RESTAURANT_ID_KEY) as Restaurant

        viewModel.restaurantLiveData.value = DataWrapper.success(restaurant)
        initRecyclerView()


    }

    fun initRecyclerView () {
        recycler_view_trabajadores.layoutManager = LinearLayoutManager(applicationContext)

        adapter = WorkersAdapter(viewModel, applicationContext)
        recycler_view_trabajadores.adapter = adapter
    }

    fun updateWorkersList() {

        error_message.visibility = View.GONE
        if (!viewModel.restaurantLiveData.value?.data?.workers.isNullOrEmpty()) {
            progress_bar.visibility = View.GONE
            SessionManager(applicationContext)
            adapter.submitList(viewModel.restaurantLiveData.value?.data?.workers)
        } else {
            progress_bar.visibility = View.GONE
            if (adapter.currentList.isEmpty()) {
                error_message.visibility = View.VISIBLE
            }
        }
    }
}