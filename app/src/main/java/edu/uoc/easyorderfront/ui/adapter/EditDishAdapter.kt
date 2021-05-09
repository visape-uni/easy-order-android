package edu.uoc.easyorderfront.ui.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.uoc.easyorderfront.domain.model.Category
import edu.uoc.easyorderfront.domain.model.Dish

class EditDishAdapter() : ListAdapter<Dish, EditDishAdapter.DishViewHolder>(EditDishAdapter.streamsDiffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EditDishAdapter.DishViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: EditDishAdapter.DishViewHolder, position: Int) {
        TODO("Not yet implemented")
    }


    class DishViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindTo(category: Category) {


        }
    }

    companion object {
        private val streamsDiffCallback = object : DiffUtil.ItemCallback<Dish>() {

            override fun areItemsTheSame(oldItem: Dish, newItem: Dish): Boolean {
                return oldItem.uid == newItem.uid
            }

            override fun areContentsTheSame(oldItem: Dish, newItem: Dish): Boolean {
                return oldItem.equals(newItem)
            }
        }
    }
}
