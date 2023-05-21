package com.example.savingtime.ui.home

import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.savingtime.R
import com.example.savingtime.databinding.FragmentHomeBinding
import com.example.savingtime.db.SavingPlanDb
import java.text.NumberFormat
import java.util.*
import kotlin.math.round

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by lazy {
        val db = SavingPlanDb.getInstance(requireContext())
        val factory = HomeViewModelFactory(db.dao)
        ViewModelProvider(this, factory)[HomeViewModel::class.java]
    }

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

        viewModel.getTotalAmount().observe(requireActivity(), { displayTotal(it) })
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
        var startingAmount = startingAmountString.toDoubleOrNull()
        val monthlyContribution = monthlyContributionString.toDouble()
        val durationInMonth = durationInMonthString.toInt()
        var interest = interestString.toDoubleOrNull()

        if (startingAmount == null) {
            startingAmount = 0.00
        }

        if (interest == null) {
            interest = 0.00
        }

        // Round or not
        var rounded: Boolean = false
        if (binding.roundSwitch.isChecked) {
            rounded = true
        }

        viewModel.calculate(
            startingAmount,
            monthlyContribution,
            durationInMonth,
            interest,
            rounded,
            null
        )
    }

    private fun displayTotal (total : Double?) {
        if (total == null) return

        val localeID = Locale("in", "ID")
        val formattedTotal = NumberFormat.getCurrencyInstance(localeID).format(total)
        binding.result.text = getString(R.string.tip_amount, formattedTotal)
    }

}