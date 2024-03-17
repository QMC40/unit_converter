package com.bignerdranch.android.converter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

data class Length(
    val kilometers: LengthValue = LengthValue("Kilometers", 0.0),
    val meters: LengthValue = LengthValue("Meters", 0.0),
    val centimeters: LengthValue = LengthValue("Centimeters", 0.0),
    val miles: LengthValue = LengthValue("Miles", 0.0),
    val feet: LengthValue = LengthValue("Feet", 0.0),
    val inches: LengthValue = LengthValue("Inches", 0.0)
)

class LengthValue(
    val type: String = "", val value: Double = 0.0
)

class MyLengths : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_length)

        val lengthTypes = resources.getStringArray(R.array.length_types)

        val inputLengthTypeSpinner: Spinner = findViewById(R.id.input_length_type)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, lengthTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        inputLengthTypeSpinner.adapter = adapter

        val lengthContainer: LinearLayout = findViewById(R.id.length_container)

        val convertButton: Button = findViewById(R.id.length_converter)

        val inputLengthValueEditText: EditText = findViewById(R.id.lengthInput)

        val homeButton: Button = findViewById(R.id.home_button)
        homeButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        convertButton.setOnClickListener {
            val selectedType = inputLengthTypeSpinner.selectedItem.toString()
            val inputValue = inputLengthValueEditText.text.toString().toDouble()

            // Convert the input value to meters if the selected type is not "Meters"
            val metersValue = if (selectedType != "Meters") convertToMeters(
                inputValue, selectedType
            ) else inputValue

            // Convert the meters value to the other length types
            val feetValue = convertFromMeters(metersValue, "Feet")
            val inchesValue = convertFromMeters(metersValue, "Inches")
            val centimetersValue = convertFromMeters(metersValue, "Centimeters")

            // Create a Length object populated with the results
            val length = Length(
                kilometers = LengthValue("Kilometers", metersValue / 1000),
                meters = LengthValue("Meters", metersValue),
                centimeters = LengthValue("Centimeters", centimetersValue),
                miles = LengthValue("Miles", metersValue / 1609.34),
                feet = LengthValue("Feet", feetValue),
                inches = LengthValue("Inches", inchesValue)
            )

            // Update the UI with the converted lengths
            updateLengths(lengthContainer, length)

            // Hide the keyboard
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }

    }

   private fun convertToMeters(inputValue: Double, type: String): Double {
    return when (type) {
        "Miles" -> inputValue * 1609.34
        "Feet" -> inputValue * 0.3048
        "Inches" -> inputValue * 0.0254
        "Kilometers" -> inputValue * 1000
        "Meters" -> inputValue
        "Centimeters" -> inputValue * 0.01
        else -> inputValue
    }
}

   private fun convertFromMeters(inputValue: Double, type: String): Double {
    return when (type) {
        "Miles" -> inputValue * 0.000621371
        "Feet" -> inputValue * 3.28084
        "Inches" -> inputValue * 39.3701
        "Kilometers" -> inputValue * 0.001
        "Meters" -> inputValue
        "Centimeters" -> inputValue * 100
        else -> inputValue
    }
}

    private fun updateLengths(container: LinearLayout, length: Length) {
        container.removeAllViews()
        val lengths = listOf(length.miles, length.feet, length.inches, length.kilometers, length.meters,length.centimeters)
        for (lengthValue in lengths) {
            val textView = TextView(this)
            textView.text = "${lengthValue.type}: ${String.format("%.4f", lengthValue.value)}"
            container.addView(textView)
        }
    }
}