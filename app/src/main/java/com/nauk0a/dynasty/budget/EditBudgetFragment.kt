package com.nauk0a.dynasty.budget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nauk0a.dynasty.R
import com.nauk0a.dynasty.databinding.EditBudgetFragmentBinding
import com.nauk0a.dynasty.utils.ToastFun
import com.nauk0a.dynasty.utils.db
import com.nauk0a.dynasty.utils.onFocusChange
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

class EditBudgetFragment : Fragment() {


    private val viewModel: EditBudgetViewModel by activityViewModels()
    private val viewModelBudget: BudgetViewModel by activityViewModels()
    private var _binding: EditBudgetFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var id: String
    private lateinit var currentMonth: String

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


        val mCalendar = Calendar.getInstance()
        currentMonth = mCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())



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
            currentMonth = snapshot?.month.toString()

            binding.categoryNameEt.onFocusChange(
                binding.categoryNameEt,
                snapshot?.nameOfTheExpensetle.toString()
            ) {}
            binding.categotySummEt.onFocusChange(
                binding.categotySummEt,
                snapshot?.expenseAmount.toString()
            ) {}
            binding.categoryNameIncom1.onFocusChange(
                binding.categoryNameIncom1,
                snapshot?.nameOfIncome1.toString()
            ) {}
            binding.categoryNameIncom2.onFocusChange(
                binding.categoryNameIncom2,
                snapshot?.nameOfIncome2.toString()
            ) {}
            binding.categoryNameIncom3.onFocusChange(
                binding.categoryNameIncom3,
                snapshot?.nameOfIncome3.toString()
            ) {}
            binding.categoryNameIncomSumm1.onFocusChange(
                binding.categoryNameIncomSumm1,
                snapshot?.amountOfIncome1.toString()
            ) {}
            binding.categoryNameIncomSumm2.onFocusChange(
                binding.categoryNameIncomSumm2,
                snapshot?.amountOfIncome2.toString()
            ) {}
            binding.categoryNameIncomSumm3.onFocusChange(
                binding.categoryNameIncomSumm3,
                snapshot?.amountOfIncome3.toString()
            ) {}
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
                    "budgetAddDate" to System.currentTimeMillis(),
                    "month" to currentMonth
                )
                db.collection(currentMonth).document(id)
                    .set(newDate)
                    .addOnSuccessListener {
                        historySave()
                        binding.saveBudget.findNavController()
                            .navigate(R.id.action_editBudgetFragment_to_budgetFragment)
                    }
                    .addOnFailureListener { ex -> ToastFun("Error writing document $ex") }
            }
        }
    }

    fun historySave() {
        val newDate = hashMapOf(
            "nameOfTheExpensetle" to "${binding.categoryNameEt.text}",
            "expenseAmount" to "${binding.categotySummEt.text}",
            "nameOfIncome1" to "${binding.categoryNameIncom1.text}",
            "nameOfIncome2" to "${binding.categoryNameIncom2.text}",
            "nameOfIncome3" to "${binding.categoryNameIncom3.text}",
            "amountOfIncome1" to "${binding.categoryNameIncomSumm1.text}",
            "amountOfIncome2" to "${binding.categoryNameIncomSumm2.text}",
            "amountOfIncome3" to "${binding.categoryNameIncomSumm3.text}",
            "budgetAddDate" to System.currentTimeMillis(),
            "month" to currentMonth,
            "direction" to "Редактирование"
        )
        db.collection("history $id").document()
            .set(newDate)
            .addOnSuccessListener {
            }
            .addOnFailureListener { ex -> ToastFun("Error writing document $ex") }
    }
}