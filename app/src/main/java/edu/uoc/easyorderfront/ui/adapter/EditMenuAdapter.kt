package edu.uoc.easyorderfront.ui.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.uoc.easyorderfront.R
import edu.uoc.easyorderfront.domain.model.Category
import edu.uoc.easyorderfront.ui.menu.CreateDishDialogFragment
import edu.uoc.easyorderfront.ui.menu.EditarMenuViewModel
import kotlinx.android.synthetic.main.item_menu.view.*


class EditMenuAdapter(
        private val restaurantId: String,
        private val viewModel: EditarMenuViewModel
) : ListAdapter<Category, EditMenuAdapter.CategoryViewHolder>(streamsDiffCallback) {

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): CategoryViewHolder {
        return CategoryViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_menu, parent, false))
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bindTo(getItem(position), restaurantId, viewModel)
    }

    class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindTo(category: Category, restaurantId: String, viewModel: EditarMenuViewModel) {

            itemView.categoria.text = category.name

            itemView.btn_edit.setOnClickListener({
                val createDishActivity = CreateDishDialogFragment(
                        restaurantId,
                        category,
                        viewModel.menu
                )
                createDishActivity.show((itemView.context as FragmentActivity).supportFragmentManager, "TAG")
            })


            val layoutManager = LinearLayoutManager(itemView.context)
            // Set Layout Manager
            itemView.recycler_view_platos_menu.layoutManager = layoutManager

            val adapter = EditDishAdapter(category.uid!!, viewModel)
            // Set Adapter
            itemView.recycler_view_platos_menu.adapter = adapter

            itemView.error_message.visibility = View.GONE
            if (category.dishes != null && category.dishes.isNotEmpty()) {
                adapter.submitList(category.dishes)

            } else {
                if (adapter.currentList.isEmpty()) {
                    itemView.error_message.visibility = View.VISIBLE
                }
            }

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