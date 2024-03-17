package com.bignerdranch.android.converter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MySettings : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val lengthVisible: CheckBox = findViewById(R.id.lengthVisible)
        val weightVisible: CheckBox = findViewById(R.id.weightVisible)
        val tempVisible: CheckBox = findViewById(R.id.temperatureVisible)
        val currencyVisible: CheckBox = findViewById(R.id.currencyVisible)

        // Load saved preferences
        val sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val lengthVisibleSaved = sharedPref.getBoolean("lengthVisible", true)
        val weightVisibleSaved = sharedPref.getBoolean("weightVisible", true)
        val tempVisibleSaved = sharedPref.getBoolean("tempVisible", true)
        val currencyVisibleSaved = sharedPref.getBoolean("currencyVisible", true)

        val homeButton: Button = findViewById(R.id.home_button)
        homeButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Set the RadioButtons to the saved preferences
        lengthVisible.isChecked = lengthVisibleSaved
        weightVisible.isChecked = weightVisibleSaved
        tempVisible.isChecked = tempVisibleSaved
        currencyVisible.isChecked = currencyVisibleSaved

        val checkListener = CompoundButton.OnCheckedChangeListener { _, isChecked ->
            if (!isChecked && !lengthVisible.isChecked && !weightVisible.isChecked && !tempVisible.isChecked && !currencyVisible.isChecked) {
                Toast.makeText(this, "At least one converter must be available in the main menu", Toast.LENGTH_SHORT).show()
                return@OnCheckedChangeListener
            }
            with(sharedPref.edit()) {
                putBoolean("lengthVisible", lengthVisible.isChecked)
                putBoolean("weightVisible", weightVisible.isChecked)
                putBoolean("tempVisible", tempVisible.isChecked)
                putBoolean("currencyVisible", currencyVisible.isChecked)
                apply()
            }
        }

        lengthVisible.setOnCheckedChangeListener(checkListener)
        weightVisible.setOnCheckedChangeListener(checkListener)
        tempVisible.setOnCheckedChangeListener(checkListener)
        currencyVisible.setOnCheckedChangeListener(checkListener)
    }
}