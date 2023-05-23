package com.example.savingtime.ui.savingPlan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.savingtime.databinding.ItemSavingPlanBinding
import com.example.savingtime.db.SavingPlanDao
import com.example.savingtime.db.SavingPlanDb
import com.example.savingtime.db.SavingPlanEntity
import com.example.savingtime.db.SavingPlanRepository
import java.text.NumberFormat
import java.util.*
import kotlin.math.pow
import kotlin.math.roundToInt

class SavingPlanAdapter(private val itemClickListener: ItemClickListener) : ListAdapter<SavingPlanEntity, SavingPlanAdapter.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK =
            object : DiffUtil.ItemCallback<SavingPlanEntity>() {
                override fun areItemsTheSame(oldData: SavingPlanEntity, newData: SavingPlanEntity): Boolean {
                    return oldData.id == newData.id
                }

                override fun areContentsTheSame(oldData: SavingPlanEntity, newData: SavingPlanEntity): Boolean {
                    return oldData == newData
                }
            }
    }

    class ViewHolder(
        private val binding: ItemSavingPlanBinding,
        private val itemClickListener: ItemClickListener
    ) : RecyclerView.ViewHolder(binding.root) {

        val localeID = Locale("in", "ID")

        fun bind(item: SavingPlanEntity) = with(binding) {
            // Set description to the corresponding textView
            descriptionTextView.text = item.description

            // Set monthly contribution to the corresponding textView
            val formattedMonthlyContribution =
                NumberFormat.getCurrencyInstance(localeID).format(item.monthlyContribution)
            monthlyContribution.text = "Setoran bulanan: " + formattedMonthlyContribution

            // Set interest percentage to the corresponding textView
            val interest = item.interest
            bunga.text = "Bunga: " +  roundToNearest(item.interest, 2) + "%"

            // Set achieved and goals amount to the corresponding textView
            val achievedAmount =
                NumberFormat.getCurrencyInstance(localeID).format(item.achievedAmount)
            val goalsAmount = NumberFormat.getCurrencyInstance(localeID).format(item.goalsAmount)
            progres.text = "Progres: " + achievedAmount + " / " + goalsAmount

            deleteButton.setOnClickListener {
                itemClickListener.onItemClickDelete(item)
            }

            setorButton.setOnClickListener {
                itemClickListener.onItemClickSetorBulanan(item.id, item.achievedAmount, item.monthlyContribution)
            }
        }

        private fun roundToNearest(value: Double, decimalPlaces: Int): String {
            val factor = 10.0.pow(decimalPlaces)
            val result = (value * factor).roundToInt() / factor
            return result.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSavingPlanBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, itemClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}