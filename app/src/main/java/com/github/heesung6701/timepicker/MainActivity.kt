package com.github.heesung6701.timepicker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.heesung6701.timepicker.library.interfaces.Observer
import com.github.heesung6701.timepicker.library.model.TimeItem
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Integer.parseInt

class MainActivity : AppCompatActivity(), Observer<Array<TimeItem>> {
    private val heartResourceArray =
        arrayOf(0, R.drawable.img_heart1, R.drawable.img_heart2, R.drawable.img_heart3)
    private var header = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        time_picker.timeItemArray.add(this)
        btn.setOnClickListener {
            val num = parseInt(edt.text.toString())
            if (num in 1..23) {
                time_picker.timeItemArray[num].selected = !time_picker.timeItemArray[num].selected
            }
        }
    }

    override fun update(item: Array<TimeItem>) {
        tv.text = item.toList().map { if (it.selected) 1 else 0 }.toString()
        time_picker.options.centerImage = heartResourceArray[header]
        header = (header + 1) % heartResourceArray.size
        time_picker.invalidate()
    }
}
