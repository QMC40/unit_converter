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

// Data class to represent all weight values
data class Weight(
    val kilograms: WeightValue = WeightValue("Kilograms", 0.0),
    val pounds: WeightValue = WeightValue("Pounds", 0.0),
    val ounces: WeightValue = WeightValue("Ounces", 0.0),
    val grams: WeightValue = WeightValue("Grams", 0.0)
)

// Data class to represent a single weight value
data class WeightValue(
    val type: String = "", val value: Double = 0.0
)

// Activity to convert weight values
class MyWeights : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weight)
        // Get the string array of weight types from the resources
        val weightTypes = resources.getStringArray(R.array.weight_types)
        // Get the spinner from the layout
        val inputWeightTypeSpinner: Spinner = findViewById(R.id.input_weight_type)
        // Create an adapter for the spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, weightTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Set the adapter for the spinner
        inputWeightTypeSpinner.adapter = adapter
        // Set the default selection for the spinner
        val weightContainer: LinearLayout = findViewById(R.id.weight_container)
        // Get the button from the layout
        val convertButton: Button = findViewById(R.id.weight_converter)
        // Get the id of the weight input from the layout
        val weightInputEditText: EditText = findViewById(R.id.weightInput)
        // Get the input method manager from the system
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        // Set the editor action listener for the input value to hide the keyboard when the user clicks done
        weightInputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Hide the keyboard
                inputMethodManager.hideSoftInputFromWindow(weightInputEditText.windowToken, 0)
                true
            } else {
                false
            }
        }

        // Get the home button from the layout and set the click listener to finish the activity
        val homeButton: Button = findViewById(R.id.home_button)
        homeButton.setOnClickListener {
            finish()
        }

        // Set the click listener for the convert button
        convertButton.setOnClickListener {
            // Get the selected weight type from the spinner and the input value from the layout when the 'convert' button is clicked
            val selectedType = inputWeightTypeSpinner.selectedItem.toString()
            val inputValue = weightInputEditText.text.toString().toDouble()

            // Convert the input value to kilograms if the selected type is not "Kilograms"
            val kilogramsValue = if (selectedType != "Kilograms") convertToKilograms(
                inputValue, selectedType
            ) else inputValue

            // Convert the kilograms value to the other weight types
            val poundsValue = convertFromKilograms(kilogramsValue, "Pounds")
            val ouncesValue = convertFromKilograms(kilogramsValue, "Ounces")
            val gramsValue = convertFromKilograms(kilogramsValue, "Grams")

            // Create a Weight object populated with the results
            val weight = Weight(
                kilograms = WeightValue("Kilograms", kilogramsValue),
                pounds = WeightValue("Pounds", poundsValue),
                ounces = WeightValue("Ounces", ouncesValue),
                grams = WeightValue("Grams", gramsValue)
            )

            // Update the UI with the converted weights
            updateWeights(weightContainer, weight)

            // Hide the keyboard
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }

    }


    // Convert the input value to kilograms
    private fun convertToKilograms(inputValue: Double, type: String): Double {
        return when (type) {
            "Pounds" -> inputValue * 0.453592
            "Ounces" -> inputValue * 0.0283495
            "Grams" -> inputValue * 0.001
            else -> inputValue
        }
    }

    // Convert the kilograms value to the other weight types
    private fun convertFromKilograms(kilogramsValue: Double, type: String): Double {
        return when (type) {
            "Pounds" -> kilogramsValue * 2.20462
            "Ounces" -> kilogramsValue * 35.274
            "Grams" -> kilogramsValue * 1000
            else -> kilogramsValue
        }
    }

    // Update the UI with the converted weights
    private fun updateWeights(container: LinearLayout, weight: Weight) {
        // Clear the contents of the weight container before adding new views
        container.removeAllViews()
        // Create a list of weight values and step through the list and add the converted value
        // views to the list and add them to the container for display in the UI
        val weights = listOf(weight.kilograms, weight.pounds, weight.ounces, weight.grams)
        for (weightValue in weights) {
            val textView = TextView(this)
            textView.text = getString(R.string.weight_format, weightValue.type, weightValue.value)
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
            container.addView(textView)
        }
    }
}