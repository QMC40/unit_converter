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

data class Weight(
    val kilograms: WeightValue = WeightValue("Kilograms", 0.0),
    val pounds: WeightValue = WeightValue("Pounds", 0.0),
    val ounces: WeightValue = WeightValue("Ounces", 0.0),
    val grams: WeightValue = WeightValue("Grams", 0.0)
)

class WeightValue(
    val type: String = "", val value: Double = 0.0
)

class MyWeights : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weight)

        val weightTypes = resources.getStringArray(R.array.weight_types)

        val inputWeightTypeSpinner: Spinner = findViewById(R.id.input_weight_type)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, weightTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        inputWeightTypeSpinner.adapter = adapter

        val weightContainer: LinearLayout = findViewById(R.id.weight_container)

        val convertButton: Button = findViewById(R.id.weight_converter)

        val inputWeightValueEditText: EditText = findViewById(R.id.weightInput)

        val homeButton: Button = findViewById(R.id.home_button)
        homeButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        convertButton.setOnClickListener {
            val selectedType = inputWeightTypeSpinner.selectedItem.toString()
            val inputValue = inputWeightValueEditText.text.toString().toDouble()

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
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }

    }

private fun convertToKilograms(inputValue: Double, type: String): Double {
    return when (type) {
        "Pounds" -> inputValue * 0.453592
        "Ounces" -> inputValue * 0.0283495
        "Grams" -> inputValue * 0.001
        else -> inputValue
    }
}

private fun convertFromKilograms(kilogramsValue: Double, type: String): Double {
    return when (type) {
        "Pounds" -> kilogramsValue * 2.20462
        "Ounces" -> kilogramsValue * 35.274
        "Grams" -> kilogramsValue * 1000
        else -> kilogramsValue
    }
}

private fun updateWeights(container: LinearLayout, weight: Weight) {
    container.removeAllViews()
    val weights = listOf(weight.kilograms, weight.pounds, weight.ounces, weight.grams)
    for (weightValue in weights) {
        val textView = TextView(this)
        textView.text = "${weightValue.type}: ${String.format("%.4f", weightValue.value)}"
        container.addView(textView)
    }
}
}