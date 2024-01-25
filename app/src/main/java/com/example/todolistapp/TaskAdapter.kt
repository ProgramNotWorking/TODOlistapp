package com.example.todolistapp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistapp.data_classes.Task
import com.example.todolistapp.databinding.TaskItemBinding

class TaskAdapter(
    private val onEditListener: OnEditListener
) : RecyclerView.Adapter<TaskAdapter.TaskHolder>() {

    private val tasksList = ArrayList<Task>()

    inner class TaskHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = TaskItemBinding.bind(view)

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

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        holder.bind(tasksList[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addTask(task: Task) {
        tasksList.add(task)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun deleteTask(position: Int) {
        tasksList.removeAt(position)
        notifyDataSetChanged()
    }

}