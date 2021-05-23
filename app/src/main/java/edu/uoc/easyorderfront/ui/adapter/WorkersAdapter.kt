package edu.uoc.easyorderfront.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.uoc.easyorderfront.R
import edu.uoc.easyorderfront.data.SessionManager
import edu.uoc.easyorderfront.domain.model.Worker
import edu.uoc.easyorderfront.ui.restaurant.WorkerListViewModel
import kotlinx.android.synthetic.main.item_worker.view.*

class WorkersAdapter(
        val viewModel: WorkerListViewModel,
        val context: Context
) : ListAdapter<Worker, WorkersAdapter.WorkerViewHolder>(workerDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkerViewHolder {
        return WorkerViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_worker, parent, false))
    }

    override fun onBindViewHolder(holder: WorkerViewHolder, position: Int) {
        holder.bindTo(getItem(position), viewModel, context)
    }

    class WorkerViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bindTo(worker: Worker, viewModel:  WorkerListViewModel, context: Context) {
            itemView.lbl_worker_name.text = worker.username
            itemView.lbl_worker_uid.text = worker.uid

            itemView.workers_constraint_layout.setOnLongClickListener({
                //TODO: BORRAR WORKER
                val workers = viewModel.restaurantLiveData.value?.data?.workers!!
                if (workers.contains(worker)) {
                    workers.remove(worker)
                    SessionManager(context).removeWorker(worker)
                    viewModel.removeWorkerRestaurant(viewModel.restaurantLiveData.value!!.data?.id!!, worker.uid!!)
                }
                true
            })
        }
    }

    companion object{
        private val workerDiffCallback = object : DiffUtil.ItemCallback<Worker>() {
            override fun areItemsTheSame(oldItem: Worker, newItem: Worker): Boolean {
                return oldItem.uid == newItem.uid
            }

            override fun areContentsTheSame(oldItem: Worker, newItem: Worker): Boolean {
                return oldItem.equals(newItem)
            }
        }
    }
}