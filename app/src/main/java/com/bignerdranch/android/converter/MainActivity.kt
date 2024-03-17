package com.bignerdranch.android.converter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val weightCardView: CardView = findViewById(R.id.weight)
        val lengthCardView: CardView = findViewById(R.id.length)
        val tempCardView: CardView = findViewById(R.id.temperature)
        val currencyCardView: CardView = findViewById(R.id.currency)
        val settingsCardView: CardView = findViewById(R.id.settings)

        // Load saved preferences
        val sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val lengthVisibleSaved = sharedPref.getBoolean("lengthVisible", true)
        val weightVisibleSaved = sharedPref.getBoolean("weightVisible", true)
        val tempVisibleSaved = sharedPref.getBoolean("tempVisible", true)
        val currencyVisibleSaved = sharedPref.getBoolean("currencyVisible", true)

        // Set the visibility of the CardViews based on the saved preferences
        lengthCardView.visibility = if (lengthVisibleSaved) View.VISIBLE else View.GONE
        weightCardView.visibility = if (weightVisibleSaved) View.VISIBLE else View.GONE
        tempCardView.visibility = if (tempVisibleSaved) View.VISIBLE else View.GONE
        currencyCardView.visibility = if (currencyVisibleSaved) View.VISIBLE else View.GONE

        weightCardView.setOnClickListener {
            val intent = Intent(this, MyWeights::class.java)
            startActivity(intent)
        }

        lengthCardView.setOnClickListener {
            val intent = Intent(this, MyLengths::class.java)
            startActivity(intent)
        }

        tempCardView.setOnClickListener {
            val intent = Intent(this, MyTemperatures::class.java)
            startActivity(intent)
        }

        currencyCardView.setOnClickListener {
            val intent = Intent(this, MyCurrencies::class.java)
            startActivity(intent)
        }

        settingsCardView.setOnClickListener {
            val intent = Intent(this, MySettings::class.java)
            startActivity(intent)
        }

    }
}