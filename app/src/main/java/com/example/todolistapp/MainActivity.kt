package com.example.todolistapp

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistapp.constants.IntentConstants
import com.example.todolistapp.data_classes.Task
import com.example.todolistapp.databinding.MainActivityBinding
import com.example.todolistapp.db.DatabaseManager

class MainActivity : ComponentActivity(), TaskAdapter.OnEditListener {

    private lateinit var binding: MainActivityBinding

    private lateinit var editTaskInfoLauncher: ActivityResultLauncher<Intent>
    private val adapter = TaskAdapter(this@MainActivity)

    private var changebleTaskPos = -1

    private val db = DatabaseManager(this)
    // private var tasksList = mutableListOf<Task>() // Need do something with that thing

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.apply {

            itemsListRecyclerView.layoutManager = GridLayoutManager(this@MainActivity, 1)
            itemsListRecyclerView.adapter = adapter

            setOnDeletingListener()

            addButton.setOnClickListener {
                val intent = Intent(this@MainActivity, TaskEditActivity::class.java)
                intent.putExtra(IntentConstants.IS_EDIT, false)
                editTaskInfoLauncher.launch(intent)
            }

            itemsListRecyclerView.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
                private val gestureDetector = GestureDetectorCompat(
                    this@MainActivity, object : GestureDetector.SimpleOnGestureListener() {
                        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                            val view = itemsListRecyclerView.findChildViewUnder(e.x, e.y)

                            if (view != null) {
                                val position = itemsListRecyclerView.getChildAdapterPosition(view)

                                val intent = Intent(this@MainActivity, TaskEditActivity::class.java)
                                intent.putExtra(IntentConstants.TASK, adapter.tasksList[position].taskName)
                                intent.putExtra(IntentConstants.TIME, adapter.tasksList[position].taskTime)
                                intent.putExtra(IntentConstants.DATE, adapter.tasksList[position].taskDate)
                                intent.putExtra(IntentConstants.DESC, adapter.tasksList[position].taskDescription)
                                intent.putExtra(IntentConstants.IS_EDIT, true)

                                changebleTaskPos = adapter.getTaskPos(adapter.tasksList[position])

                                editTaskInfoLauncher.launch(intent)
                            }

                            return super.onSingleTapConfirmed(e)
                        }
                    }
                )

                override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                    gestureDetector.onTouchEvent(e)
                    return false
                }

                override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

                override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
            })

            editTaskInfoLauncher =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->

                    intent = result.data

                    val task = Task(
                        intent.getStringExtra(IntentConstants.TASK).toString(),
                        intent.getStringExtra(IntentConstants.TIME).toString(),
                        intent.getStringExtra(IntentConstants.DATE).toString(),
                        intent.getStringExtra(IntentConstants.DESC).toString()
                    )

                    if (!intent.getBooleanExtra(IntentConstants.IS_EDIT, false)) {
                        adapter.addTask(task)
                        // tasksList.add(task)
                    } else {
                        try {
                            adapter.changeTask(changebleTaskPos, task)
                            // tasksList[changebleTaskPos] = task
                        } catch (error: IndexOutOfBoundsException) {
                            error("Index out of Bounds")
                        }
                    }

                }

        }

    }

    override fun onStop() {
        super.onStop()

        // db.save(tasksList)
    }

    private fun setOnDeletingListener() = with(binding) {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        itemsListRecyclerView.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            private val gestureDetector = GestureDetectorCompat(
                this@MainActivity, object : GestureDetector.SimpleOnGestureListener() {
                    override fun onLongPress(e: MotionEvent) {
                        val view = itemsListRecyclerView.findChildViewUnder(e.x, e.y)

                        if (view != null) {
                            val position = itemsListRecyclerView.getChildAdapterPosition(view)
                            val currentTime = System.currentTimeMillis()
                            val elapsedTime = currentTime - adapter.lastClickTime

                            if (position == adapter.lastClickedPosition && elapsedTime >= 2000) {
                                adapter.deleteTask(position)

                                Toast.makeText(
                                    this@MainActivity, DELETED, Toast.LENGTH_LONG
                                ).show()

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    vibrator.vibrate(VibrationEffect.createOneShot(
                                        100, VibrationEffect.DEFAULT_AMPLITUDE
                                    ))
                                } else {
                                    vibrator.vibrate(100)
                                }
                            }

                        }

                        super.onLongPress(e)
                    }
                }
            )

            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                gestureDetector.onTouchEvent(e)
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

        })
    }

    override fun edit(task: Task) {
        val intent = Intent(this@MainActivity, TaskEditActivity::class.java)
        intent.putExtra(IntentConstants.TASK, task.taskName)
        intent.putExtra(IntentConstants.TIME, task.taskTime)
        intent.putExtra(IntentConstants.DATE, task.taskDate)
        intent.putExtra(IntentConstants.DESC, task.taskDescription)
        intent.putExtra(IntentConstants.IS_EDIT, true)

        changebleTaskPos = adapter.getTaskPos(task)

        editTaskInfoLauncher.launch(intent)
    }

    companion object {
        private var DELETED = R.string.deleted
    }

}