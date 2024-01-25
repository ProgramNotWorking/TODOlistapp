package com.example.todolistapp.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.todolistapp.data_classes.Task

class DatabaseManager(context: Context) {

    private val helper = DatabaseHelper(context)
    private var db: SQLiteDatabase? = null

    fun open() {
        db = helper.writableDatabase
    }

    fun close() {
        helper.close()
    }

    fun clearTable() {
        db?.delete(DatabaseNames.TABLE_NAME, null, null)
    }

    fun repopulateDatabase(tasksList: MutableList<Task>?) {
        db?.beginTransaction()
        try {
            db?.delete(DatabaseNames.TABLE_NAME, null, null)
            if (tasksList != null) {
                for (task in tasksList) {
                    addTask(task)
                }
            }
            db?.setTransactionSuccessful()
        } finally {
            db?.endTransaction()
        }
    }

    @SuppressLint("Recycle", "Range")
    fun readTable(): MutableList<Task> {
        val dataList = ArrayList<Task>()

        val cursor = db?.query(
            DatabaseNames.TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null
        )

        with(cursor) {
            while (this?.moveToNext()!!) {
                val dataTaskName = cursor?.getString(cursor.getColumnIndex(DatabaseNames.TASK_NAME_COLUMN))
                val dataTaskTime = cursor?.getString(cursor.getColumnIndex(DatabaseNames.TASK_TIME_COLUMN))
                val dataTaskDate = cursor?.getString(cursor.getColumnIndex(DatabaseNames.TASK_DATE_COLUMN))
                val dataTaskDescription = cursor?.getString(cursor.getColumnIndex(DatabaseNames.TASK_DESCRIPTION_COLUMN))

                val dataTask = Task(dataTaskName!!, dataTaskTime!!, dataTaskDate!!, dataTaskDescription!!)
                dataList.add(dataTask)
            }
        }

        cursor?.close()
        return dataList
    }

    private fun addTask(task: Task) {
        val values = ContentValues().apply {
            put(DatabaseNames.TASK_NAME_COLUMN, task.taskName)
            put(DatabaseNames.TASK_TIME_COLUMN, task.taskTime)
            put(DatabaseNames.TASK_DATE_COLUMN, task.taskDate)
            put(DatabaseNames.TASK_DESCRIPTION_COLUMN, task.taskDescription)
        }
        db?.insert(DatabaseNames.TABLE_NAME, null, values)
    }

}