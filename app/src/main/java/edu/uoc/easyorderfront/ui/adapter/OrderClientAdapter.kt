package edu.uoc.easyorderfront.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.uoc.easyorderfront.R
import edu.uoc.easyorderfront.domain.model.Order
import edu.uoc.easyorderfront.domain.model.OrderedDish
import kotlinx.android.synthetic.main.item_ordered_dish.view.*

class OrderClientAdapter(
        val orderLive: MutableLiveData<Order>?
): ListAdapter<OrderedDish, OrderClientAdapter.OrderedDishViewHolder>(orderedDishDiffCallback) {
    lateinit var list: MutableList<OrderedDish>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderedDishViewHolder {
        return OrderedDishViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ordered_dish, parent, false))
    }

    override fun onBindViewHolder(holder: OrderedDishViewHolder, position: Int) {
        val item = list.get(position)
        holder.bindTo(item)

        if (item.newOrder!!) {
            holder.itemView.contraintLayout.setOnLongClickListener({
                list.removeAt(position)
                notifyDataSetChanged()
                orderLive?.value?.price = orderLive?.value?.price?.minus(item.totalPrice!!)
                orderLive?.postValue(orderLive.value)
                true
            })
        }
    }

    override fun submitList(list: MutableList<OrderedDish>?) {
        this.list = list!!
        super.submitList(list)
    }

    class OrderedDishViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindTo(orderedDish: OrderedDish) {
            itemView.txt_titulo_plato.text = orderedDish.dish?.name
            itemView.txt_cantidad.text = orderedDish.quantity.toString()

            val price = String.format("%.2f", orderedDish.totalPrice)
            itemView.txt_precio.text = price + "€"

            if (orderedDish.newOrder!!) {
                itemView.txt_cantidad.setBackgroundResource(R.color.green3)
            } else {
                itemView.txt_cantidad.setBackgroundResource(0)
            }
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