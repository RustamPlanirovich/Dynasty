package com.nauk0a.dynasty.budget

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nauk0a.dynasty.R
import com.nauk0a.dynasty.databinding.BudgetFragmentBinding
import com.nauk0a.dynasty.databinding.EditBudgetFragmentBinding
import com.nauk0a.dynasty.utils.ToastFun
import com.nauk0a.dynasty.utils.db
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class EditBudgetFragment : Fragment() {


    private val viewModel: EditBudgetViewModel by activityViewModels()
    private val viewModelBudget: BudgetViewModel by activityViewModels()
    private var _binding: EditBudgetFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var id: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = EditBudgetFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val job = Job()
        val uiScope = CoroutineScope(Dispatchers.Main + job)
        db = Firebase.firestore

        viewModelBudget.dateToDetailBudgetEdit.observe(requireActivity(), Observer {
            val snapshot = it.toObject(Budget::class.java)
            id = it.id
            binding.categoryNameEt.setText(snapshot?.nameOfTheExpensetle)
            binding.categotySummEt.setText(snapshot?.expenseAmount)
            binding.categoryNameIncom1.setText(snapshot?.nameOfIncome1)
            binding.categoryNameIncom2.setText(snapshot?.nameOfIncome2)
            binding.categoryNameIncom3.setText(snapshot?.nameOfIncome3)
            binding.categoryNameIncomSumm1.setText(snapshot?.amountOfIncome1)
            binding.categoryNameIncomSumm2.setText(snapshot?.amountOfIncome2)
            binding.categoryNameIncomSumm3.setText(snapshot?.amountOfIncome3)
        })

        binding.saveBudget.setOnClickListener {
            uiScope.launch(Dispatchers.IO) {
                val newDate = hashMapOf(
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
                db.collection("budget").document(id)
                    .set(newDate)
                    .addOnSuccessListener {

                    }
                    .addOnFailureListener { ex -> ToastFun("Error writing document $ex") }
            }
        }
    }

}