package edu.uoc.easyorderfront.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.uoc.easyorderfront.R
import edu.uoc.easyorderfront.domain.model.OrderedDish
import kotlinx.android.synthetic.main.item_ordered_dish.view.*

class OrderClientAdapter(): ListAdapter<OrderedDish, OrderClientAdapter.OrderedDishViewHolder>(orderedDishDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderedDishViewHolder {
        return OrderedDishViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ordered_dish, parent, false))
    }

    override fun onBindViewHolder(holder: OrderedDishViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    class OrderedDishViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindTo(orderedDish: OrderedDish) {
            itemView.txt_titulo_plato.text = orderedDish.dish?.name
            itemView.txt_cantidad.text = orderedDish.quantity.toString()
            itemView.txt_precio.text = orderedDish.totalPrice.toString()

        }
    }

    companion object {
        private val orderedDishDiffCallback = object : DiffUtil.ItemCallback<OrderedDish>() {
            override fun areItemsTheSame(oldItem: OrderedDish, newItem: OrderedDish): Boolean {
                return oldItem.uid == newItem.uid
            }

            override fun areContentsTheSame(oldItem: OrderedDish, newItem: OrderedDish): Boolean {
                return oldItem.equals(newItem)
            }
        }
    }
}