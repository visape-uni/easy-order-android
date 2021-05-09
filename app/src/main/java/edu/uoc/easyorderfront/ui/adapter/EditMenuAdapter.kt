package edu.uoc.easyorderfront.ui.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.uoc.easyorderfront.R
import edu.uoc.easyorderfront.domain.model.Category
import kotlinx.android.synthetic.main.item_menu.view.*

class EditMenuAdapter() : ListAdapter<Category, EditMenuAdapter.CategoryViewHolder>(streamsDiffCallback) {

    private val adapter = EditMenuAdapter()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryViewHolder {
        return CategoryViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_menu, parent, false))
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindTo(category: Category) {

            itemView.categoria1.text = category.name

            itemView.btn_edit.setOnClickListener({
                //TODO: EDIT
            })


            val layoutManager = LinearLayoutManager(itemView.context)
            // Set Layout Manager
            itemView.recycler_view_platos_menu.layoutManager = layoutManager

            val adapter = EditDishAdapter()
            // Set Adapter
            itemView.recycler_view_platos_menu.adapter = adapter

        }
    }


    companion object {
        private val streamsDiffCallback = object : DiffUtil.ItemCallback<Category>() {

            override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
                return oldItem.uid == newItem.uid
            }

            override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
                return oldItem.equals(newItem)
            }
        }
    }
}