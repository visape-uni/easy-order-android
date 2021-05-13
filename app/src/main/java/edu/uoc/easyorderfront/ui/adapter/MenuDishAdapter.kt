package edu.uoc.easyorderfront.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.uoc.easyorderfront.R
import edu.uoc.easyorderfront.domain.model.Dish
import edu.uoc.easyorderfront.ui.menu.AddDishToOrderFragment
import edu.uoc.easyorderfront.ui.menu.MenuRestaurantViewModel
import kotlinx.android.synthetic.main.item_dish.view.*

class MenuDishAdapter(
        private val categoryId: String,
        private val viewModel: MenuRestaurantViewModel
) : ListAdapter<Dish, MenuDishAdapter.DishViewHolder>(dishDiffCallback) {

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
        fun bindTo(dish: Dish, viewModel: MenuRestaurantViewModel, categoryId: String) {
            itemView.lbl_dish.text = dish.name
            itemView.lbl_dish_price.text = dish.price.toString() + "€"

            itemView.dish_layout.setOnClickListener({
                val addDishActivity = AddDishToOrderFragment(viewModel.order.value?.data!!, dish, viewModel)
                addDishActivity.show((itemView.context as FragmentActivity).supportFragmentManager, "TAG")
            })
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
