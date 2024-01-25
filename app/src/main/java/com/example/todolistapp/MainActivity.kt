package com.example.todolistapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import com.example.todolistapp.constants.IntentConstants
import com.example.todolistapp.data_classes.Task
import com.example.todolistapp.databinding.MainActivityBinding

class MainActivity : ComponentActivity(), TaskAdapter.OnEditListener {

    private lateinit var binding: MainActivityBinding
    private lateinit var editTaskInfoLauncher: ActivityResultLauncher<Intent>
    private val adapter = TaskAdapter(this@MainActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.apply {

            itemsListRecyclerView.layoutManager = GridLayoutManager(this@MainActivity, 1)
            itemsListRecyclerView.adapter = adapter

            addButton.setOnClickListener {
                val intent = Intent(this@MainActivity, TaskEditActivity::class.java)
                intent.putExtra(IntentConstants.IS_EDIT, false)
                editTaskInfoLauncher.launch(intent)
            }

            editTaskInfoLauncher =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->

                    intent = result.data

                    adapter.addTask(
                        Task(
                            intent.getStringExtra(IntentConstants.TASK).toString(),
                            intent.getStringExtra(IntentConstants.TIME).toString(),
                            intent.getStringExtra(IntentConstants.DATE).toString(),
                            intent.getStringExtra(IntentConstants.DESC).toString()
                        )
                    )

                }

        }

    }

    override fun edit(task: Task) {
        val intent = Intent(this@MainActivity, TaskEditActivity::class.java)
        intent.putExtra(IntentConstants.TASK, task.taskName)
        intent.putExtra(IntentConstants.TIME, task.taskTime)
        intent.putExtra(IntentConstants.DATE, task.taskDate)
        intent.putExtra(IntentConstants.DESC, task.taskDescription)
        intent.putExtra(IntentConstants.IS_EDIT, true)

        editTaskInfoLauncher.launch(intent)
    }

}