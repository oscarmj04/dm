package com.example.myapplication


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class TaskAdapter(
    private val tareas: List<Task>,
    private val onTaskClick: (Task) -> Unit // <-- lambda para el click
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(tareas[position], onTaskClick)
    }

    override fun getItemCount(): Int = tareas.size

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTaskTitle)
        private val tvDueDate: TextView = itemView.findViewById(R.id.tvDueDate)
        private val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)

        fun bind(task: Task, onTaskClick: (Task) -> Unit) {
            tvTitle.text = "${task.title} #${task.id}"
            tvDueDate.text = task.dueDate.toString()
            tvStatus.text = if (task.done) "✅" else "⏳"

            itemView.setOnClickListener { onTaskClick(task) } // <-- disparar la acción
        }
    }
}
