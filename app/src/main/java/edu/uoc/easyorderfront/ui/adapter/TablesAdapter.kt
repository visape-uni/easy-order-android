package edu.uoc.easyorderfront.ui.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.uoc.easyorderfront.R
import edu.uoc.easyorderfront.domain.model.Table
import edu.uoc.easyorderfront.ui.main.MainWorkerMenuActivity
import edu.uoc.easyorderfront.ui.order.OrderWorkerFragment
import kotlinx.android.synthetic.main.item_estat_taules.view.*

class TablesAdapter(
) : ListAdapter<Table, TablesAdapter.TableViewHolder>(tableDiffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TableViewHolder {
        return TableViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_estat_taules, parent, false))
    }

    override fun onBindViewHolder(holder: TableViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    class TableViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindTo(table: Table) {
            val context = itemView.txt_codigo_mesa.context

            itemView.txt_codigo_mesa.text = context.getString(R.string.mesa_num, table.uid)
            val estado = table.state

            if (estado.equals("EMPTY")) {
                // Set Color green
                itemView.table_constraint_layout.setBackgroundColor(ContextCompat.getColor(context, R.color.green3))
            } else {
                // Set Color red
                itemView.table_constraint_layout.setBackgroundColor(ContextCompat.getColor(context, R.color.red))
            }

            itemView.table_constraint_layout.setOnClickListener({
                val fragment = OrderWorkerFragment.newInstance(table)
                (itemView.context as MainWorkerMenuActivity).replaceFragment(fragment)
            })

            itemView.table_constraint_layout.setOnLongClickListener({
                //TODO: BORRAR MESA
                true
            })
        }
    }

    companion object {
        private val tableDiffCallback = object : DiffUtil.ItemCallback<Table>() {

            override fun areItemsTheSame(oldItem: Table, newItem: Table): Boolean {
                return oldItem.uid == newItem.uid
            }

            override fun areContentsTheSame(oldItem: Table, newItem: Table): Boolean {
                return oldItem.equals(newItem)
            }
        }
    }
}