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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL
import org.json.JSONObject

//data class Currency(
//    val baseCurrency: CurrencyValue = CurrencyValue("USD", 1.0),
//    val targetCurrency: CurrencyValue = CurrencyValue("EUR", 0.0)
//)

class CurrencyValue(
    val type: String = "", var value: Double = 0.0
)

class MyCurrencies : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency)

        val currencyTypes = resources.getStringArray(R.array.currency_types)

        val inputCurrencyTypeSpinner: Spinner = findViewById(R.id.input_currency_type)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencyTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        inputCurrencyTypeSpinner.adapter = adapter

        val currencyContainer: LinearLayout = findViewById(R.id.currency_container)

        val convertButton: Button = findViewById(R.id.currency_converter)

        val inputCurrencyValueEditText: EditText = findViewById(R.id.currencyInput)

        val homeButton: Button = findViewById(R.id.home_button)
        homeButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        convertButton.setOnClickListener {
            val selectedInputType = inputCurrencyTypeSpinner.selectedItem.toString()
            val inputValue = inputCurrencyValueEditText.text.toString().toDouble()

            CoroutineScope(Dispatchers.Main).launch {
                val baseCurrencyValue = if (selectedInputType != "USD") convertToBaseCurrency(
                    inputValue, selectedInputType
                ) else inputValue

                currencyContainer.removeAllViews()

                val targetCurrencies = mutableListOf<CurrencyValue>()
                for (type in currencyTypes) {
                    val targetCurrencyValue = convertFromBaseCurrency(baseCurrencyValue, type)
                    targetCurrencies.add(CurrencyValue(type, targetCurrencyValue))
                }
                updateCurrencies(
                    currencyContainer,
                    CurrencyValue(selectedInputType, inputValue),
                    targetCurrencies
                )
            }

            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }

    }

    private suspend fun convertToBaseCurrency(inputValue: Double, type: String): Double {
        return withContext(Dispatchers.IO) {
            val url = URL("https://api.exchangerate-api.com/v4/latest/$type")
            val json = JSONObject(url.readText())
            val rate = json.getJSONObject("rates").getDouble("USD")
            inputValue / rate
        }
    }

    private suspend fun convertFromBaseCurrency(inputValue: Double, type: String): Double {
        return withContext(Dispatchers.IO) {
            val url = URL("https://api.exchangerate-api.com/v4/latest/USD")
            val json = JSONObject(url.readText())
            val rate = json.getJSONObject("rates").getDouble(type)
            inputValue * rate
        }
    }

  private fun updateCurrencies(
    container: LinearLayout,
    baseCurrency: CurrencyValue,
    targetCurrencies: List<CurrencyValue>
) {
      container.removeAllViews()
      val textView = TextView(this)
      textView.text = "${baseCurrency.type}: ${String.format("%.2f", baseCurrency.value)}"
      container.addView(textView)
      for (currencyValue in targetCurrencies) {
          if (currencyValue.type != baseCurrency.type) {
              val currencyTextView = TextView(this)
              currencyTextView.text = "${currencyValue.type}: ${String.format("%.2f", currencyValue.value)}"
              container.addView(currencyTextView)
          }
      }
  }
}