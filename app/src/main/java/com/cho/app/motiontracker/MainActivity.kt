package com.cho.app.motiontracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.AdapterView
import android.widget.RadioButton
import android.widget.Spinner
import androidx.core.content.edit
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        val pref = PreferenceManager.getDefaultSharedPreferences(this)
//        pref.edit().clear().commit()
        var sex: String =""
        var year: String =""

        spinner_years.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val spinner = parent as? Spinner
                    val item = spinner?.selectedItem as? String
                    item?.let {
                        if (it.isNotEmpty()) year = it
                    }
                }
            }
        radioGroup.setOnCheckedChangeListener {
                group, checkedId ->
            sex = findViewById<RadioButton>(checkedId).text as String
        }

        val intent = Intent(this, CircleSpeedActivity::class.java)
        circle_speed1.setOnClickListener {
            val pref = PreferenceManager.getDefaultSharedPreferences(this)
            pref.edit {
                putString("NAME", input_name.text.toString())
                putString("YEAR", year)
                putString("SEX", sex)
                putString("TITLE", "Speed1")
                putLong("DURATION", 10000L)
            }
//            val intent = Intent(this, CircleSpeedActivity1::class.java)
            startActivity(intent)
        }
        circle_speed2.setOnClickListener {
            val pref = PreferenceManager.getDefaultSharedPreferences(this)
            pref.edit {
                putString("NAME", input_name.text.toString())
                putString("YEAR", year)
                putString("SEX", sex)
                putString("TITLE", "Speed2")
                putLong("DURATION", 7500L)
            }
//            val intent = Intent(this, CircleSpeedActivity2::class.java)
            startActivity(intent)
        }
        circle_speed3.setOnClickListener {
            val pref = PreferenceManager.getDefaultSharedPreferences(this)
            pref.edit {
                putString("NAME", input_name.text.toString())
                putString("YEAR", year)
                putString("SEX", sex)
                putString("TITLE", "Speed3")
                putLong("DURATION", 5000L)
            }
//            val intent = Intent(this, CircleSpeedActivity3::class.java)
            startActivity(intent)
        }
        circle_speed4.setOnClickListener {
            val pref = PreferenceManager.getDefaultSharedPreferences(this)
            pref.edit {
                putString("NAME", input_name.text.toString())
                putString("YEAR", year)
                putString("SEX", sex)
                putString("TITLE", "Speed4")
                putLong("DURATION", 2500L)
            }
//            val intent = Intent(this, CircleSpeedActivity4::class.java)
            startActivity(intent)
        }
    }
    private fun onWriteStartTapped(year: String, sex: String) {
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
    }
}