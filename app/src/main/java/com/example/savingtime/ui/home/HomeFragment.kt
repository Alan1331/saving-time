package com.example.savingtime.ui.home

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.savingtime.R
import com.example.savingtime.databinding.FragmentHomeBinding
import com.example.savingtime.db.SavingPlanDb
import com.example.savingtime.db.SavingPlanRepository
import java.text.NumberFormat
import java.util.*


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by lazy {
        val db = SavingPlanDb.getInstance(requireContext())
        val savingPlanRepository = SavingPlanRepository(db.dao)
        val factory = HomeViewModelFactory(savingPlanRepository)
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
        setHasOptionsMenu(true)

        // add action to navigate from home fragment into news fragment
        binding.buttonToNews.setOnClickListener {
            findNavController().navigate(
                R.id.action_homeFragment_to_newsFragment
            )
        }

        val view = binding.root
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.home_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_saving_plan -> {
                findNavController().navigate(
                    R.id.action_homeFragment_to_savingPlanFragment
                )
                return true
            }
            R.id.menu_about -> {
                findNavController().navigate(
                    R.id.action_homeFragment_to_aboutFragment
                )
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.calculateButton.setOnClickListener { calculate() }
        binding.saveButton.setOnClickListener { preSave() }

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
            return
        }
        val durationInMonthString = binding.durationEditText.text.toString()
        if (TextUtils.isEmpty(durationInMonthString)) {
            Toast.makeText(activity, R.string.invalid_duration, Toast.LENGTH_LONG).show()
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

    private fun preSave() {
        // reject blank input at certain columns
        val monthlyContributionString = binding.monthlyContributionsEditText.text.toString()
        if (TextUtils.isEmpty(monthlyContributionString)) {
            Toast.makeText(activity, R.string.invalid_monthly_contribution, Toast.LENGTH_LONG).show()
            return
        }
        val durationInMonthString = binding.durationEditText.text.toString()
        if (TextUtils.isEmpty(durationInMonthString)) {
            Toast.makeText(activity, R.string.invalid_duration, Toast.LENGTH_LONG).show()
            return
        }

        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Masukkan deskripsi saving plan!")

        // Set up the input
        val input = EditText(requireContext())

        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        // Set up the buttons
        builder.setPositiveButton("Simpan",
            DialogInterface.OnClickListener { dialog, which -> save( input.text.toString()) })
        builder.setNegativeButton("Batalkan",
            DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

        builder.show()
    }

    private fun save(description: String) {
        // Takes user input as string
        val startingAmountString = binding.startingAmountEditText.text.toString()
        val monthlyContributionString = binding.monthlyContributionsEditText.text.toString()
        if (TextUtils.isEmpty(monthlyContributionString)) {
            Toast.makeText(activity, R.string.invalid_monthly_contribution, Toast.LENGTH_LONG).show()
            return
        }
        val durationInMonthString = binding.durationEditText.text.toString()
        if (TextUtils.isEmpty(durationInMonthString)) {
            Toast.makeText(activity, R.string.invalid_duration, Toast.LENGTH_LONG).show()
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
            description
        )

        // clear all input
        binding.startingAmountEditText.text?.clear()
        binding.monthlyContributionsEditText.text?.clear()
        binding.durationEditText.text?.clear()
        binding.interestEditText.text?.clear()
        binding.result.setText("")
    }

    private fun displayTotal (total : Double?) {
        if (total == null) return

        val localeID = Locale("in", "ID")
        val formattedTotal = NumberFormat.getCurrencyInstance(localeID).format(total)
        binding.result.text = getString(R.string.tip_amount, formattedTotal)
    }

}