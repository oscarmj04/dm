package com.example.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemHeaderBinding
import com.example.myapplication.databinding.ItemTaskBinding
import com.example.myapplication.domain.Task

class TaskListAdapter(
    private val onTaskClick: (Task) -> Unit
) : ListAdapter<TaskListItem, RecyclerView.ViewHolder>(DiffCallback) {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_TASK = 1

        val DiffCallback = object : DiffUtil.ItemCallback<TaskListItem>() {

            override fun areItemsTheSame(old: TaskListItem, new: TaskListItem): Boolean {
                return when {
                    old is TaskListItem.Header && new is TaskListItem.Header ->
                        old.category == new.category

                    old is TaskListItem.TaskItem && new is TaskListItem.TaskItem ->
                        old.task.localId == new.task.localId

                    else -> false
                }
            }

            override fun areContentsTheSame(old: TaskListItem, new: TaskListItem): Boolean {
                return old == new
            }
        }
    }

    override fun getItemViewType(position: Int): Int =
        when (getItem(position)) {
            is TaskListItem.Header -> TYPE_HEADER
            is TaskListItem.TaskItem -> TYPE_TASK
        }

    class HeaderViewHolder(val binding: ItemHeaderBinding) :
        RecyclerView.ViewHolder(binding.root)

    class TaskViewHolder(val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            TYPE_HEADER ->
                HeaderViewHolder(ItemHeaderBinding.inflate(inflater, parent, false))

            TYPE_TASK ->
                TaskViewHolder(ItemTaskBinding.inflate(inflater, parent, false))

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {

            is TaskListItem.Header -> {
                val h = holder as HeaderViewHolder
                h.binding.tvHeader.text = item.category.name   // ENUM → STRING
            }

            is TaskListItem.TaskItem -> {
                val t = holder as TaskViewHolder
                val task = item.task

                t.binding.tvTaskTitle.text = task.title
                t.binding.tvDueDate.text = task.dueDate
                t.binding.tvStatus.text = if (task.done) "✅" else "⏳"

                t.binding.root.setOnClickListener { onTaskClick(task) }
            }
        }
    }
}
