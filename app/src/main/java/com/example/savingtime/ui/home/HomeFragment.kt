package com.example.savingtime.ui.home

import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.savingtime.R
import com.example.savingtime.databinding.FragmentHomeBinding
import java.text.NumberFormat
import java.util.*

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.calculateButton.setOnClickListener { calculate() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun calculate() {
        // Takes user input as string
        val startingAmountString = binding.startingAmountEditText.text.toString()
        val monthlyContributionString = binding.monthlyContributionsEditText.text.toString()
        if (TextUtils.isEmpty(monthlyContributionString)) {
            Toast.makeText(activity, R.string.invalid_monthly_contribution, Toast.LENGTH_LONG).show()
            displayTotal(0.0)
            return
        }
        val durationInMonthString = binding.durationEditText.text.toString()
        if (TextUtils.isEmpty(durationInMonthString)) {
            Toast.makeText(activity, R.string.invalid_duration, Toast.LENGTH_LONG).show()
            displayTotal(0.0)
            return
        }
        val interestString = binding.interestEditText.text.toString()

        // Define data type of the input
        val startingAmount = startingAmountString.toDoubleOrNull()
        val monthlyContribution = monthlyContributionString.toDouble()
        val durationInMonth = durationInMonthString.toInt()
        val interest = interestString.toDoubleOrNull()

        // Define total var
        var totalAmount : Double
        if (startingAmount == null) {
            totalAmount = 0.0
        } else {
            totalAmount = startingAmount
        }

        // Calculation if interest is not applied
        if (interest == null || interest == 0.0) {
            for (i in 1..durationInMonth) {
                totalAmount += monthlyContribution
            }
        } else { // Calculation if interest is applied
            val monthlyInterestRate = interest / 12 / 100
            for (i in 1..durationInMonth) {
                totalAmount += monthlyContribution;
                totalAmount *= (1 + monthlyInterestRate)
            }
        }

        // Round the total to the nearest thousand
        if (binding.roundSwitch.isChecked) {
            totalAmount /= 1000
            totalAmount = kotlin.math.round(totalAmount)
            totalAmount *= 1000
        }

        // Display the total
        displayTotal(totalAmount)
    }

    private fun displayTotal (total : Double) {
        val localeID = Locale("in", "ID")
        val formattedTotal = NumberFormat.getCurrencyInstance(localeID).format(total)
        binding.result.text = getString(R.string.tip_amount, formattedTotal)
    }

}