package com.nauk0a.dynasty.budget

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.nauk0a.dynasty.R
import com.nauk0a.dynasty.databinding.DetailBudgetFragmentBinding
import com.nauk0a.dynasty.databinding.NoteFragmentBinding

class DetailBudgetFragment : Fragment() {


    private val viewModel: DetailBudgetViewModel by activityViewModels()
    private val viewModelBudget: BudgetViewModel by activityViewModels()
    private var _binding: DetailBudgetFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DetailBudgetFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelBudget.dateToDetailBudget.observe(requireActivity(), Observer {
            val detailDate = it.toObject(Budget::class.java)
            binding.categoryNameEt.text = detailDate?.nameOfTheExpensetle
            binding.categotySummEt.text = detailDate?.expenseAmount
            binding.categoryNameIncom1.text = detailDate?.nameOfIncome1
            binding.categoryNameIncom2.text = detailDate?.nameOfIncome2
            binding.categoryNameIncom3.text = detailDate?.nameOfIncome3
            binding.categoryNameIncomSumm1.text = detailDate?.amountOfIncome1
            binding.categoryNameIncomSumm2.text = detailDate?.amountOfIncome2
            binding.categoryNameIncomSumm3.text = detailDate?.amountOfIncome3
        })
    }
}