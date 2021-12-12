package com.sampam.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.todo_item.view.*

class todoadapter(val list: List<TodoModel>) : RecyclerView.Adapter<todoadapter.todoviewholder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): todoviewholder {
        return todoviewholder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.todo_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: todoviewholder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class todoviewholder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        fun bind(todoModel: TodoModel) {
            with(itemView) {

                tasktitle.text = todoModel.title
                tasksubtitle.text = todoModel.description
                categ.text = todoModel.category
                date.text = todoModel.date
                time.text = todoModel.time

            }
        }

    }


}