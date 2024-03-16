package com.bignerdranch.android.converter

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var weight: CardView;
    private lateinit var length: CardView;
    private lateinit var temp: CardView;
    private lateinit var currency: CardView;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val weightCardView: CardView = findViewById(R.id.weight)
        val lengthCardView: CardView = findViewById(R.id.length)
        val tempCardView: CardView = findViewById(R.id.temperature)
        val currencyCardView: CardView = findViewById(R.id.currency)


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


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}