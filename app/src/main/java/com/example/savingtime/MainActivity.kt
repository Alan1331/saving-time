package com.example.savingtime

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.savingtime.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.calculateButton.setOnClickListener { calculate() }
    }

    private fun calculate() {
        // Takes user input as string
        val startingAmountString = binding.startingAmountEditText.text.toString()
        val monthlyContributionString = binding.monthlyContributionsEditText.text.toString()
        val durationInMonthString = binding.durationEditText.text.toString()
        val interestString = binding.interestEditText.text.toString()

        // Define data type of the input
        val startingAmount = startingAmountString.toDouble()
        val monthlyContribution = monthlyContributionString.toDouble()
        val durationInMonth = durationInMonthString.toInt()
        val interest = interestString.toDouble()

        // Define temp var
        var totalAmount = startingAmount
    }

}