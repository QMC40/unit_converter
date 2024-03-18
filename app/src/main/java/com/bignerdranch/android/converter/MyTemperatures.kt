package com.bignerdranch.android.converter

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

// Data class to contain all three temperature values
data class Temperature(
    val celsius: TemperatureValue = TemperatureValue("Celsius", 0.0),
    val fahrenheit: TemperatureValue = TemperatureValue("Fahrenheit", 0.0),
    val kelvin: TemperatureValue = TemperatureValue("Kelvin", 0.0)
)

// Data class to represent an individual temperature value
data class TemperatureValue(
    val type: String = "", val value: Double = 0.0
)

// Activity to convert temperature values
class MyTemperatures : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temperature)
        // Get the string array of temperature types from the resources
        val temperatureTypes = resources.getStringArray(R.array.temp_types)
        // Get the spinner from the layout
        val inputTemperatureTypeSpinner: Spinner = findViewById(R.id.input_temperature_type)
        // Create an adapter for the spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, temperatureTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Set the adapter for the spinner
        inputTemperatureTypeSpinner.adapter = adapter
        // Set the default selection for the spinner
        val temperatureContainer: LinearLayout = findViewById(R.id.temperature_container)
        // Get the button from the layout
        val convertButton: Button = findViewById(R.id.temperature_converter)
        // Get the id of the temperature input from the layout
        val temperatureInputEditText: EditText = findViewById(R.id.temperatureInput)
        // Get the input method manager from the system
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        // Set the editor action listener for the input value to hide the keyboard when the user clicks done
        temperatureInputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Hide the keyboard
                inputMethodManager.hideSoftInputFromWindow(temperatureInputEditText.windowToken, 0)
                true
            } else {
                false
            }
        }

        // Get the home button from the layout and set a click listener to finish the activity
        val homeButton: Button = findViewById(R.id.home_button)
        homeButton.setOnClickListener {
            finish()
        }

        // Set the click listener for the convert button
        convertButton.setOnClickListener {
            // Get the selected temperature type from the spinner and the input value from the layout when the 'convert' button is clicked
            val selectedType = inputTemperatureTypeSpinner.selectedItem.toString()
            val inputValue = temperatureInputEditText.text.toString().toDouble()

            // Convert the input value to Celsius if the selected type is not "Celsius"
            val celsiusValue = if (selectedType != "Celsius") convertToCelsius(
                inputValue, selectedType
            ) else inputValue

            // Convert the Celsius value to the other temperature types
            val fahrenheitValue = convertFromCelsius(celsiusValue, "Fahrenheit")
            val kelvinValue = convertFromCelsius(celsiusValue, "Kelvin")

            // Create a Temperature object populated with the results
            val temperature = Temperature(
                celsius = TemperatureValue("Celsius", celsiusValue),
                fahrenheit = TemperatureValue("Fahrenheit", fahrenheitValue),
                kelvin = TemperatureValue("Kelvin", kelvinValue)
            )

            // Update the UI with the converted temperatures
            updateTemperatures(temperatureContainer, temperature)

            // Hide the keyboard
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }

    }


    // Convert the input value to Celsius as the base temperature
    private fun convertToCelsius(inputValue: Double, type: String): Double {
        return when (type) {
            "Fahrenheit" -> (inputValue - 32) * 5 / 9
            "Kelvin" -> inputValue - 273.15
            else -> inputValue
        }
    }

    // Convert the Celsius value to the other temperature types
    private fun convertFromCelsius(inputValue: Double, type: String): Double {
        return when (type) {
            "Fahrenheit" -> inputValue * 9 / 5 + 32
            "Kelvin" -> inputValue + 273.15
            else -> inputValue
        }
    }

    // Update the UI with the converted temperatures
    private fun updateTemperatures(container: LinearLayout, temperature: Temperature) {
        // Clear the contents of the temperature container before adding new views
        container.removeAllViews()
        // Create a list of temperature values and add the converted values to the list and add them to the container
        // for display in the UI
        val temperatures = listOf(temperature.celsius, temperature.fahrenheit, temperature.kelvin)
        for (temperatureValue in temperatures) {
            val textView = TextView(this)
            textView.text = getString(
                R.string.temperature_format,
                temperatureValue.type,
                temperatureValue.value
            )
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f) // Set text size to 20sp
            container.addView(textView)
        }
    }
}