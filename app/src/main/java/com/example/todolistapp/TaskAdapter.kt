package com.example.todolistapp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistapp.data_classes.Task
import com.example.todolistapp.databinding.TaskItemBinding

class TaskAdapter(
    private val onEditListener: OnEditListener
) : RecyclerView.Adapter<TaskAdapter.TaskHolder>() {

    val tasksList = mutableListOf<Task>()
    var lastClickedPosition = -1
    var lastClickTime = 0L

    inner class TaskHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = TaskItemBinding.bind(view)
        val cardView: CardView = binding.taskItemHolder

        init {
            val cardViewColorSelector = ContextCompat.getColorStateList(itemView.context, R.color.items_colors)
            cardView.backgroundTintList = cardViewColorSelector
        }

        fun bind(task: Task) = with(binding) {
            taskNameTextView.text = task.taskName
            taskTimeTextView.text = task.taskTime
            taskDateTextView.text = task.taskDate

            taskItemHolder.setOnClickListener {
                onEditListener.edit(task)
            }
        }
    }

    interface OnEditListener {
        fun edit(task: Task)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.task_item, parent, false
        )
        return TaskHolder(view)
    }

    override fun getItemCount(): Int = tasksList.size

    override fun onBindViewHolder(holder: TaskHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.bind(tasksList[position])
        holder.cardView.setOnClickListener {
            lastClickedPosition = position
            lastClickTime = System.currentTimeMillis()
            holder.cardView.isSelected = it.isPressed
            false
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addTask(task: Task) {
        tasksList.add(task)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun deleteTask(position: Int) {
        tasksList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun getTaskPos(task: Task): Int = tasksList.indexOf(task)

    fun changeTask(position: Int, task: Task) {
        tasksList[position] = task
        notifyItemChanged(position)
    }

}