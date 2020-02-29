package com.github.heesung6701.timepicker.example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.heesung6701.timepicker.R
import com.github.heesung6701.timepicker.timepicker.TimePickerView
import com.github.heesung6701.timepicker.timepicker.model.TimeItem
import com.github.heesung6701.timepicker.timepicker.interfaces.Observer
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Integer.parseInt

class MainActivity : AppCompatActivity(),
    Observer<Array<TimeItem>> {
    private lateinit var timePickerView : TimePickerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        timePickerView = findViewById<TimePickerView>(R.id.time_picker)
        timePickerView.timeItemArray.add(this)

        btn.setOnClickListener {
            val num = parseInt(edt.text.toString())
            if(num in 1..23) {
                timePickerView.timeItemArray[num].selected = !timePickerView.timeItemArray[num].selected
            }
        }
    }

    override fun update(item: Array<TimeItem>) {
        tv.text = item.toList().map { if(it.selected) 1 else 0 }.toString()
    }
}
