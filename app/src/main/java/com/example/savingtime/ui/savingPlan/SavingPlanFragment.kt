package com.example.savingtime.ui.savingPlan

import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.asLiveData
import com.example.savingtime.R
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.savingtime.data.SettingsDataStore
import com.example.savingtime.data.dataStore
import com.example.savingtime.databinding.FragmentSavingPlanBinding
import com.example.savingtime.db.SavingPlanDb
import com.example.savingtime.db.SavingPlanEntity
import com.example.savingtime.db.SavingPlanRepository
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*

class SavingPlanFragment : Fragment(), ItemClickListener {

    private val layoutDataStore: SettingsDataStore by lazy {
        SettingsDataStore(requireActivity().dataStore)
    }

    private val viewModel: SavingPlanViewModel by lazy {
        val db = SavingPlanDb.getInstance(requireContext())
        val savingPlanRepository = SavingPlanRepository(db.dao)
        val factory = SavingPlanViewModelFactory(savingPlanRepository)
        ViewModelProvider(this, factory)[SavingPlanViewModel::class.java]
    }

    private lateinit var binding: FragmentSavingPlanBinding
    private lateinit var myAdapter: SavingPlanAdapter
    private var isLinearLayout = true

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

        layoutDataStore.preferenceFlow.asLiveData().observe(viewLifecycleOwner) {
            isLinearLayout = it
            setLayout()
            activity?.invalidateOptionsMenu()
        }

        viewModel.data.observe(viewLifecycleOwner, {
            binding.emptyView.visibility = if (it.isEmpty())
                View.VISIBLE else View.GONE
            myAdapter.submitList(it)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.saving_plan_menu, menu)

        val menuItem = menu.findItem(R.id.action_switch_layout)
        setIcon(menuItem)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_hapus) {
            hapusData()
            return true
        }

        if (item.itemId == R.id.action_switch_layout) {
            lifecycleScope.launch {
                layoutDataStore.saveLayout(!isLinearLayout, requireContext())
            }
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setLayout() {
        binding.recyclerView.layoutManager = if (isLinearLayout)
            LinearLayoutManager(context)
        else
            GridLayoutManager(context, 2)
    }

    private fun setIcon(menuItem: MenuItem) {
        val iconId = if (isLinearLayout)
            R.drawable.ic_grid_view
        else
            R.drawable.ic_view_list
        menuItem.icon = ContextCompat.getDrawable(requireContext(), iconId)
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