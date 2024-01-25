package com.example.todolistapp.db

import android.provider.BaseColumns

object DatabaseNames {
    const val TABLE_NAME = "TODO_LIST TABLE"
    const val TASK_NAME_COLUMN = "task_name_column"
    const val TASK_TIME_COLUMN = "task_time_column"
    const val TASK_DATE_COLUMN = "task_date_column"
    const val TASK_DESCRIPTION_COLUMN = "task_description_column"

    const val DATABASE_VERSION = 1
    const val DATABASE = "TODO.db"

    const val CREATE_TODOLIST_TABLE = "CREATE TABLE $TABLE_NAME (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY, " +
            "$TASK_NAME_COLUMN TEXT, " +
            "$TASK_TIME_COLUMN TEXT, " +
            "$TASK_DATE_COLUMN TEXT, " +
            "$TASK_DESCRIPTION_COLUMN TEXT)"
    const val SQL_DELETER_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
}