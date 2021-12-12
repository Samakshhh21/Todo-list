package com.sampam.myapplication

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_task.*
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*


const val todotask = "tododatabase"

class TaskActivity : AppCompatActivity(), View.OnClickListener {

    val db by lazy {
        tododb.getdatabase(this)
    }

    lateinit var mycalender: Calendar
    lateinit var dateSetListener: DatePickerDialog.OnDateSetListener
    lateinit var timeSetListener: TimePickerDialog.OnTimeSetListener

    private val cats = arrayListOf("Business", "Personal", "Banking", "Shopping", "Billing")


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)
        dateedt.setOnClickListener { onClick(dateedt) }
        timeedt.setOnClickListener { onClick(timeedt) }
        savebtn.setOnClickListener { onClick(savebtn) }
        setupspinner()

    }

    private fun setupspinner() {
        val adap = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, cats)
        cats.sort()
        spncategory.adapter = adap
    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onClick(v: View) {
        when (v.id) {
            R.id.dateedt -> {
                setlistner()
            }
            R.id.timeedt -> {
                settimelistner()
            }
            R.id.savebtn -> {
                saveTodo()
            }
        }
    }

    @DelicateCoroutinesApi
    private fun saveTodo() {
        val category = spncategory.selectedItem.toString()
        val title = titleinplay.editText?.text.toString()
        val description = titleinplay2.editText?.text.toString()

        GlobalScope.launch(Dispatchers.Main) {
            val id = withContext(Dispatchers.IO) {
                return@withContext db.tododao().insertTask(
                    TodoModel(
                        title,
                        description,
                        category,
                        dateedt.text.toString(),
                        timeedt.text.toString()
                    )
                )
            }
            finish()
        }

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun settimelistner() {
        mycalender = Calendar.getInstance()
        timeSetListener =
            TimePickerDialog.OnTimeSetListener { v: TimePicker, hourofday: Int, min: Int ->
                mycalender.set(Calendar.HOUR_OF_DAY, hourofday)
                mycalender.set(Calendar.MINUTE, min)
                updatetime()
            }

        val timePickerDialog = TimePickerDialog(
            this,
            timeSetListener,
            mycalender.get(Calendar.HOUR_OF_DAY),
            mycalender.get(Calendar.MINUTE),
            false
        )
        timePickerDialog.show()
    }

    private fun updatetime() {
        val myformat = "h:mm a"
        val sdf = SimpleDateFormat(myformat)
        timeedt.setText(sdf.format(mycalender.time))
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setlistner() {
        mycalender = Calendar.getInstance()
        dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            mycalender.set(Calendar.YEAR, year)
            mycalender.set(Calendar.MONTH, month)
            mycalender.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updatedate()
        }
        val datePickerDialog = DatePickerDialog(
            this,
            dateSetListener,
            mycalender.get(Calendar.YEAR),
            mycalender.get(Calendar.MONTH),
            mycalender.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()

    }

    private fun updatedate() {
        val myformat = "EEE, d MMM yyyy"
        val sdf = SimpleDateFormat(myformat)
        dateedt.setText(sdf.format(mycalender.time))
        titleinplay4.visibility = View.VISIBLE
    }
}




