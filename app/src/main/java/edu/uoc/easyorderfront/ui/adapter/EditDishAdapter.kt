package edu.uoc.easyorderfront.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.uoc.easyorderfront.R
import edu.uoc.easyorderfront.domain.model.Dish
import edu.uoc.easyorderfront.ui.menu.EditarMenuViewModel
import kotlinx.android.synthetic.main.item_dish.view.*

class EditDishAdapter(
        private val categoryId: String,
        private val viewModel: EditarMenuViewModel
) : ListAdapter<Dish, EditDishAdapter.DishViewHolder>(EditDishAdapter.dishDiffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DishViewHolder {
        return DishViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_dish, parent, false))
    }

    override fun onBindViewHolder(holder: DishViewHolder, position: Int) {
        holder.bindTo(getItem(position), viewModel, categoryId)
    }


    class DishViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindTo(dish: Dish, viewModel: EditarMenuViewModel, categoryId: String) {
            itemView.lbl_dish.text = dish.name
            itemView.lbl_dish_price.text = dish.price.toString() + "€"
            itemView.dish_layout.setOnLongClickListener {
                viewModel.deleteDish(viewModel.restaurantProfile.value?.data?.id, categoryId, dish.uid)
                true
            }
        }
    }

    companion object {
        private val dishDiffCallback = object : DiffUtil.ItemCallback<Dish>() {

            override fun areItemsTheSame(oldItem: Dish, newItem: Dish): Boolean {
                return oldItem.uid == newItem.uid
            }

            override fun areContentsTheSame(oldItem: Dish, newItem: Dish): Boolean {
                return oldItem.equals(newItem)
            }
        }
    }
}
