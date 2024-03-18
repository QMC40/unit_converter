/*
 * File: MainActivity.kt
 * Author: Aaron Fortner
 * Version: 1.0
 * Class Information: COSC 6362 Mobile Software Development - Spring 2024
 * Professor: Dr. Yadav
 * Assignment 2
 */

package com.mobilesoftwaredev.android.converter

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

        // Get the CardViews for the different categories
        val weightCardView: CardView = findViewById(R.id.weight)
        val lengthCardView: CardView = findViewById(R.id.length)
        val tempCardView: CardView = findViewById(R.id.temperature)
        val currencyCardView: CardView = findViewById(R.id.currency)
        val settingsCardView: CardView = findViewById(R.id.settings)
        val exitCardView: CardView = findViewById(R.id.exit)

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

        // Set the onClickListeners for the CardViews
        // start the corresponding activity when a CardView is clicked
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

        exitCardView.setOnClickListener {
            finish()
        }

    }

    // Reload the main activity when the settings activity is finished to update the visibility of the CardViews
    // based on the saved preferences
    override fun onResume() {
        super.onResume()

        // Load saved preferences
        val sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val lengthVisibleSaved = sharedPref.getBoolean("lengthVisible", true)
        val weightVisibleSaved = sharedPref.getBoolean("weightVisible", true)
        val tempVisibleSaved = sharedPref.getBoolean("tempVisible", true)
        val currencyVisibleSaved = sharedPref.getBoolean("currencyVisible", true)

        // Get the CardViews for the different categories
        val weightCardView: CardView = findViewById(R.id.weight)
        val lengthCardView: CardView = findViewById(R.id.length)
        val tempCardView: CardView = findViewById(R.id.temperature)
        val currencyCardView: CardView = findViewById(R.id.currency)

        // Set the visibility of the CardViews based on the saved preferences
        lengthCardView.visibility = if (lengthVisibleSaved) View.VISIBLE else View.GONE
        weightCardView.visibility = if (weightVisibleSaved) View.VISIBLE else View.GONE
        tempCardView.visibility = if (tempVisibleSaved) View.VISIBLE else View.GONE
        currencyCardView.visibility = if (currencyVisibleSaved) View.VISIBLE else View.GONE
    }
}