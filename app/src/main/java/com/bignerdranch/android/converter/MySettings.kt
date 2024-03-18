package com.bignerdranch.android.converter

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

// Activity to set the visibility of the different converters
class MySettings : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Get the CheckBoxes from the layout
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

        // Get the button from the layout and set the onClickListener to finish the activity
        val homeButton: Button = findViewById(R.id.home_button)
        homeButton.setOnClickListener {
            finish()
        }

        // Set the RadioButtons to the saved preferences
        lengthVisible.isChecked = lengthVisibleSaved
        weightVisible.isChecked = weightVisibleSaved
        tempVisible.isChecked = tempVisibleSaved
        currencyVisible.isChecked = currencyVisibleSaved

        // Set the OnCheckedChangeListener for the CheckBoxes to save the preferences when they are changed
        val checkListener = CompoundButton.OnCheckedChangeListener { _, isChecked ->
            // Check if at least one converter is available in the main menu and show a toast if not
            // If not, set the currency CheckBox to checked to prevent the user from unchecking all converters
            if (!isChecked && !lengthVisible.isChecked && !weightVisible.isChecked && !tempVisible.isChecked && !currencyVisible.isChecked) {
                currencyVisible.isChecked = true
                Toast.makeText(
                    this,
                    "At least one converter must be available in the main menu",
                    Toast.LENGTH_SHORT
                ).show()
                return@OnCheckedChangeListener
            }
            // Save the preferences
            with(sharedPref.edit()) {
                putBoolean("lengthVisible", lengthVisible.isChecked)
                putBoolean("weightVisible", weightVisible.isChecked)
                putBoolean("tempVisible", tempVisible.isChecked)
                putBoolean("currencyVisible", currencyVisible.isChecked)
                apply()
            }
        }

        // Set the OnCheckedChangeListener for the CheckBoxes
        lengthVisible.setOnCheckedChangeListener(checkListener)
        weightVisible.setOnCheckedChangeListener(checkListener)
        tempVisible.setOnCheckedChangeListener(checkListener)
        currencyVisible.setOnCheckedChangeListener(checkListener)
    }

}