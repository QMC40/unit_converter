package com.mobilesoftwaredev.android.converter

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

// Data class to represent all length values
data class Length(
    val kilometers: LengthValue = LengthValue("Kilometers", 0.0),
    val meters: LengthValue = LengthValue("Meters", 0.0),
    val centimeters: LengthValue = LengthValue("Centimeters", 0.0),
    val miles: LengthValue = LengthValue("Miles", 0.0),
    val feet: LengthValue = LengthValue("Feet", 0.0),
    val inches: LengthValue = LengthValue("Inches", 0.0)
)

// Data class to represent a single length value
data class LengthValue(
    val type: String = "", val value: Double = 0.0
)

// Activity to convert length values
class MyLengths : AppCompatActivity() {

    // variables for the input value
    private lateinit var selectedType: String
    private var inputValue = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_length)
        // Get the string array of length types from the resources
        val lengthTypes = resources.getStringArray(R.array.length_types)
        // Get the spinner from the layout
        val inputLengthTypeSpinner: Spinner = findViewById(R.id.input_length_type)
        // Create an adapter for the spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, lengthTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Set the adapter for the spinner
        inputLengthTypeSpinner.adapter = adapter
        // Set the default selection for the spinner
        val lengthContainer: LinearLayout = findViewById(R.id.length_container)
        // Get the button from the layout
        val convertButton: Button = findViewById(R.id.length_converter)
        // Get the id of the length input from the layout
        val lengthInputEditText: EditText = findViewById(R.id.lengthInput)
        // Get the input method manager from the system
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        // Set the editor action listener for the input value to hide the keyboard when the user clicks done
        lengthInputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Hide the keyboard
                inputMethodManager.hideSoftInputFromWindow(lengthInputEditText.windowToken, 0)
                true
            } else {
                false
            }
        }

        // Get the button from the layout and set the onClickListener to finish the activity
        val homeButton: Button = findViewById(R.id.home_button)
        homeButton.setOnClickListener {
            finish()
        }

        // Set the click listener for the convert button
        convertButton.setOnClickListener {
            // Get the selected length type and the input value
            selectedType = inputLengthTypeSpinner.selectedItem.toString()
            inputValue = lengthInputEditText.text.toString().toDouble()

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
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }

    }


    // Convert the input value to meters
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

    // Convert the meters value to the other length types
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

    // Update the UI with the converted lengths
    private fun updateLengths(container: LinearLayout, length: Length) {
        container.removeAllViews()
        val lengths = listOf(
            length.miles,
            length.feet,
            length.inches,
            length.kilometers,
            length.meters,
            length.centimeters
        )
        for (lengthValue in lengths) {
            val textView = TextView(this)
            textView.text = getString(R.string.length_format, inputValue, selectedType, lengthValue.value, lengthValue.type)
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f) // Set text size to 20sp
            container.addView(textView)
        }
    }
}