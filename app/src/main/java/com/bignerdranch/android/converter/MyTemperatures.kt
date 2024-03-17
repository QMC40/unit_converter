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

data class Temperature(
    val celsius: TemperatureValue = TemperatureValue("Celsius", 0.0),
    val fahrenheit: TemperatureValue = TemperatureValue("Fahrenheit", 0.0),
    val kelvin: TemperatureValue = TemperatureValue("Kelvin", 0.0)
)

class TemperatureValue(
    val type: String = "", val value: Double = 0.0
)

class MyTemperatures : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temperature)

        val temperatureTypes = resources.getStringArray(R.array.temp_types)

        val inputTemperatureTypeSpinner: Spinner = findViewById(R.id.input_temperature_type)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, temperatureTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        inputTemperatureTypeSpinner.adapter = adapter

        val temperatureContainer: LinearLayout = findViewById(R.id.temperature_container)

        val convertButton: Button = findViewById(R.id.temperature_converter)

        val inputTemperatureValueEditText: EditText = findViewById(R.id.temperatureInput)

        val homeButton: Button = findViewById(R.id.home_button)
        homeButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        convertButton.setOnClickListener {
            val selectedType = inputTemperatureTypeSpinner.selectedItem.toString()
            val inputValue = inputTemperatureValueEditText.text.toString().toDouble()

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
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }

    }

    private fun convertToCelsius(inputValue: Double, type: String): Double {
        return when (type) {
            "Fahrenheit" -> (inputValue - 32) * 5/9
            "Kelvin" -> inputValue - 273.15
            else -> inputValue
        }
    }

    private fun convertFromCelsius(inputValue: Double, type: String): Double {
        return when (type) {
            "Fahrenheit" -> inputValue * 9/5 + 32
            "Kelvin" -> inputValue + 273.15
            else -> inputValue
        }
    }

    private fun updateTemperatures(container: LinearLayout, temperature: Temperature) {
        container.removeAllViews()
        val temperatures = listOf(temperature.celsius, temperature.fahrenheit, temperature.kelvin)
        for (temperatureValue in temperatures) {
            val textView = TextView(this)
            textView.text = "${temperatureValue.type}: ${String.format("%.2f", temperatureValue.value)}"
            container.addView(textView)
        }
    }
}