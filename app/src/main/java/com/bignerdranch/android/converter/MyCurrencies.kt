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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

// Data class to represent a currency value
data class CurrencyValue(
    val type: String = "", var value: Double = 0.0
)

// Activity to convert currency values
class MyCurrencies : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency)

        // Get the string array of currency types from the resources
        val currencyTypes = resources.getStringArray(R.array.currency_types)
        // Get the spinner from the layout
        val inputCurrencyTypeSpinner: Spinner = findViewById(R.id.input_currency_type)
        // Create an adapter for the spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencyTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Set the adapter for the spinner
        inputCurrencyTypeSpinner.adapter = adapter
        // Set the default selection for the spinner
        val currencyContainer: LinearLayout = findViewById(R.id.currency_container)
        // Get the button from the layout
        val convertButton: Button = findViewById(R.id.currency_converter)
        // Get the input value from the layout
        val currencyInputEditText: EditText = findViewById(R.id.currencyInput)
        // Get the input method manager from the system
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        // Set the editor action listener for the input value to hide the keyboard when the user clicks done
        currencyInputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Hide the keyboard
                inputMethodManager.hideSoftInputFromWindow(currencyInputEditText.windowToken, 0)
                true
            } else {
                false
            }
        }

        // Get the home button from the layout and set the click listener to go back to the main activity
        val homeButton: Button = findViewById(R.id.home_button)
        homeButton.setOnClickListener {
            finish()
        }

        // Set the click listener for the convert button
        convertButton.setOnClickListener {
            // Get the selected input type and the input value from the layout when the 'convert' button is clicked
            val selectedInputType = inputCurrencyTypeSpinner.selectedItem.toString()
            val inputValue = currencyInputEditText.text.toString().toDouble()

            // Use the coroutine scope to launch a new coroutine on the main dispatcher
            CoroutineScope(Dispatchers.Main).launch {
                // Convert the input value to the base currency (USD) if it is not already in USD
                val baseCurrencyValue = if (selectedInputType != "USD") convertToBaseCurrency(
                    inputValue, selectedInputType
                    // If the input value is already in USD, use it as the base currency value
                ) else inputValue

                // Clear the contents of the currency container before adding new views
                currencyContainer.removeAllViews()

                // Create a list of target currencies and add the converted values to the list
                // for each currency type
                val targetCurrencies = mutableListOf<CurrencyValue>()
                for (type in currencyTypes) {
                    val targetCurrencyValue = convertFromBaseCurrency(baseCurrencyValue, type)
                    targetCurrencies.add(CurrencyValue(type, targetCurrencyValue))
                }

                // Update the currency container with the new currency values
                updateCurrencies(
                    currencyContainer,
                    CurrencyValue(selectedInputType, inputValue),
                    targetCurrencies
                )
            }

            // Hide the keyboard after the user clicks the convert button
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }

    }

    // Download the exchange rates from the API and convert the input value to the base currency (USD)
    // if it is not already in USD
    private suspend fun convertToBaseCurrency(inputValue: Double, type: String): Double {

        // Use the withContext function to switch to the IO dispatcher and make the network request
        return withContext(Dispatchers.IO) {
            // Create a URL object with the API endpoint to get the exchange rates for the target currency
            val url = URL("https://api.exchangerate-api.com/v4/latest/$type")
            // Get the exchange rates from the JSON response and convert the input value to USD
            val json = JSONObject(url.readText())
            // Get the exchange rate for USD
            val rate = json.getJSONObject("rates").getDouble("USD")
            // Convert the input value to USD
            inputValue / rate
        }
    }

    // Download the exchange rates from the API and convert the input value to the target currency
    // to populate the currency container with the new currency values
    private suspend fun convertFromBaseCurrency(inputValue: Double, type: String): Double {
        // Use the withContext function to switch to the IO dispatcher and make the network request
        return withContext(Dispatchers.IO) {

            // Create a URL object with the API endpoint to get the exchange rates for the target currency
            val url = URL("https://api.exchangerate-api.com/v4/latest/USD")
            // Get the exchange rates from the JSON response and convert the input value to the target currency
            val json = JSONObject(url.readText())
            // Get the exchange rate for the target currency
            val rate = json.getJSONObject("rates").getDouble(type)
            // Convert the input value to the target currency
            inputValue * rate
        }
    }

    // Update the currency container with the new currency values after the input value is converted
    private fun updateCurrencies(
        container: LinearLayout,
        baseCurrency: CurrencyValue,
        targetCurrencies: List<CurrencyValue>
    ) {
        // Clear the contents of the currency container before adding new views
        container.removeAllViews()
        // Create a text view for the base currency and add it to the container
        val textView = TextView(this)
        // Set the text for the base currency
        textView.text = getString(R.string.currency_format, baseCurrency.type, baseCurrency.value)
        // Set the text size to 20sp
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
        // Add the text view to the container
        container.addView(textView)
        // Create a list of target currency values and add the converted values to the list and add
        // them to the container in the same manner as the base currency
        for (currencyValue in targetCurrencies) {
            if (currencyValue.type != baseCurrency.type) {
                val currencyTextView = TextView(this)
                currencyTextView.text =
                    getString(R.string.currency_format, currencyValue.type, currencyValue.value)
                currencyTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
                container.addView(currencyTextView)
            }
        }
    }
}