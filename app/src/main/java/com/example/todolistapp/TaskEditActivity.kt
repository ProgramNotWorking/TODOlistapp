package com.example.todolistapp

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import com.example.todolistapp.constants.IntentConstants
import com.example.todolistapp.databinding.ActivityTaskEditBinding
import java.util.Calendar
import kotlin.properties.Delegates

class TaskEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskEditBinding
    private var isEdit: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isEdit = intent.getBooleanExtra(IntentConstants.IS_EDIT, false)
        onTouchCloseKeyboard()

        binding.apply {

            checkOnEdit()
            // When open it second time, desc field show us date, not desc

            editTaskTimeButton.setOnClickListener { callTimePickerDialog() }

            editTaskDateButton.setOnClickListener { callDatePickerDialog() }

            saveTaskButton.setOnClickListener { onSave() }

        }

    }

    private fun checkOnEdit() = with(binding) {
        if (isEdit) {
            editTaskNamePlainText.setText(intent.getStringExtra(IntentConstants.TASK).toString())
            editTaskTimeButton.text = intent.getStringExtra(IntentConstants.TIME).toString()
            editTaskDateButton.text = intent.getStringExtra(IntentConstants.DATE).toString()
            editTaskDescPlainText.setText(intent.getStringExtra(IntentConstants.DESC).toString())
        }
    }

    private fun onSave() = with(binding) {
        val data = mutableMapOf(
            "task" to getString(R.string.enter_task_name_on_plain_text_view),
            "time" to getString(R.string.enter_task_time_on_button),
            "date" to getString(R.string.enter_task_date_on_button),
            "desc" to getString(R.string.enter_task_desc_on_plain_text)
        )

        for ((key, value) in data) {
            when (key) {
                "task" -> if (value != editTaskNamePlainText.text.toString())
                    data[key] = editTaskNamePlainText.text.toString()
                else
                    data[key] = "NONE"

                "time" -> if (value != editTaskTimeButton.text.toString())
                    data[key] = editTaskTimeButton.text.toString()
                else
                    data[key] = "NONE"

                "date" -> if (value != editTaskDateButton.text.toString())
                    data[key] = editTaskDateButton.text.toString()
                else
                    data[key] = "NONE"

                "desc" -> if (value != editTaskDescPlainText.text.toString())
                    data[key] = editTaskDateButton.text.toString()
                else
                    data[key] = "NONE"
            }
        }

        val intent = Intent(this@TaskEditActivity, MainActivity::class.java)
        intent.putExtra(IntentConstants.TASK, data["task"])
        intent.putExtra(IntentConstants.TIME, data["time"])
        intent.putExtra(IntentConstants.DATE, data["date"])
        intent.putExtra(IntentConstants.DESC, data["desc"])
        intent.putExtra(IntentConstants.IS_EDIT, isEdit)

        setResult(RESULT_OK, intent)
        finish()
    }

    private fun callTimePickerDialog() = with(binding) {
        val timePickerDialog = TimePickerDialog(this@TaskEditActivity, { _, hour, minute ->
            val time = String.format("%02d:%02d", hour, minute)
            editTaskTimeButton.text = time
        }, 0, 0, true)
        timePickerDialog.window?.setBackgroundDrawableResource(R.color.light_green)
        timePickerDialog.show()

        val okButton = timePickerDialog.getButton(Dialog.BUTTON_POSITIVE)
        val cancelButton = timePickerDialog.getButton(Dialog.BUTTON_NEGATIVE)

        okButton.setTextColor(
            ContextCompat.getColor(this@TaskEditActivity, R.color.white)
        )
        cancelButton.setTextColor(
            ContextCompat.getColor(this@TaskEditActivity, R.color.white)
        )
    }

    private fun callDatePickerDialog() = with(binding) {
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            val selectedDate = "$day/${month + 1}/$year"

            if (selectedDate.isNotEmpty()) {
                editTaskDateButton.text = selectedDate
            }
        }

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(this@TaskEditActivity, dateSetListener, year, month, day)
        datePickerDialog.show()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun onTouchCloseKeyboard() {
        binding.mainEditHolder.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val keyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                keyboard.hideSoftInputFromWindow(binding.mainEditHolder.windowToken, 0)
            }

            true
        }
    }
}