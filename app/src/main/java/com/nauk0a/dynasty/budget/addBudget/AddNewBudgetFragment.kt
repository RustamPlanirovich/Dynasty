package com.nauk0a.dynasty.budget.addBudget

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nauk0a.dynasty.R
import com.nauk0a.dynasty.databinding.AddNewBudgetFragmentBinding
import com.nauk0a.dynasty.databinding.BudgetFragmentBinding
import com.nauk0a.dynasty.utils.ToastFun
import com.nauk0a.dynasty.utils.db
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AddNewBudgetFragment : Fragment() {

    private val viewModel: AddNewBudgetViewModel by activityViewModels()
    private var _binding: AddNewBudgetFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AddNewBudgetFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val job = Job()
        val uiScope = CoroutineScope(Dispatchers.Main + job)
        db = Firebase.firestore

        binding.addNewBudget.setOnClickListener {
            uiScope.launch(Dispatchers.IO) {
                val budget = hashMapOf(
                    "nameOfTheExpensetle" to "${binding.categoryNameEt.text}",
                    "expenseAmount" to "${binding.categotySummEt.text}",
                    "nameOfIncome1" to "${binding.categoryNameIncom1.text}",
                    "nameOfIncome2" to "${binding.categoryNameIncom2.text}",
                    "nameOfIncome3" to "${binding.categoryNameIncom3.text}",
                    "amountOfIncome1" to "${binding.categoryNameIncomSumm1.text}",
                    "amountOfIncome2" to "${binding.categoryNameIncomSumm2.text}",
                    "amountOfIncome3" to "${binding.categoryNameIncomSumm3.text}",
                    "budgetAddDate" to System.currentTimeMillis()
                )
                db.collection("budget").document()
                    .set(budget)
                    .addOnSuccessListener {

                    }
                    .addOnFailureListener { ex -> ToastFun("Error writing document $ex") }
            }
            it.findNavController().navigate(R.id.action_addNewBudgetFragment_to_budgetFragment)
        }
    }
}