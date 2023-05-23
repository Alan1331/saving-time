package com.example.savingtime.ui.savingPlan

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.savingtime.R
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.savingtime.databinding.FragmentSavingPlanBinding
import com.example.savingtime.db.SavingPlanDb
import com.example.savingtime.db.SavingPlanEntity
import com.example.savingtime.db.SavingPlanRepository
import com.example.savingtime.ui.home.HomeViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.NumberFormat
import java.util.*

class SavingPlanFragment : Fragment(), ItemClickListener {

    private val viewModel: SavingPlanViewModel by lazy {
        val db = SavingPlanDb.getInstance(requireContext())
        val savingPlanRepository = SavingPlanRepository(db.dao)
        val factory = SavingPlanViewModelFactory(savingPlanRepository)
        ViewModelProvider(this, factory)[SavingPlanViewModel::class.java]
    }

    private lateinit var binding: FragmentSavingPlanBinding
    private lateinit var myAdapter: SavingPlanAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavingPlanBinding.inflate(layoutInflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        myAdapter = SavingPlanAdapter(this)
        with (binding.recyclerView) {
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
            adapter = myAdapter
            setHasFixedSize(true)
        }
        viewModel.data.observe(viewLifecycleOwner, {
            binding.emptyView.visibility = if (it.isEmpty())
                View.VISIBLE else View.GONE
            myAdapter.submitList(it)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.saving_plan_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_hapus) {
            hapusData()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun hapusData() {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(R.string.konfirmasi_hapus)
            .setPositiveButton(getString(R.string.hapus)) { _, _ ->
                viewModel.hapusData()
            }
            .setNegativeButton(getString(R.string.batal)) { dialog, _ ->
                dialog.cancel()
            }
            .show()
    }

    override fun onItemClickDelete(item: SavingPlanEntity) {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(getString(R.string.hapus_satu))
            .setPositiveButton(getString(R.string.hapus)) { _, _ ->
                viewModel.hapusSavingPlan(item)
            }
            .setNegativeButton(getString(R.string.batal)) { dialog, _ ->
                dialog.cancel()
            }
            .show()
    }

    override fun onItemClickSetorBulanan(id: Long, achievedAmount: Double, monthlyContribution: Double) {
        val localeID = Locale("in", "ID")
        val monthlyContributionString : String = NumberFormat.getCurrencyInstance(localeID).format(monthlyContribution)
        MaterialAlertDialogBuilder(requireContext())
            .setMessage("Setor tabungan untuk bulan ini sebesar: " + monthlyContributionString + " ?")
            .setPositiveButton(getString(R.string.setor)) { _, _ ->
                viewModel.setorBulanan(id, achievedAmount, monthlyContribution)
            }
            .setNegativeButton(getString(R.string.batal)) { dialog, _ ->
                dialog.cancel()
            }
            .show()
    }
}

interface ItemClickListener {
    fun onItemClickDelete(item: SavingPlanEntity)

    fun onItemClickSetorBulanan(id: Long, achievedAmount: Double, monthlyContribution: Double)
}