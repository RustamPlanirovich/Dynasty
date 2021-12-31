package com.nauk0a.dynasty.budget.addBudget

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.BaseInputConnection
import android.widget.ArrayAdapter
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nauk0a.dynasty.R
import com.nauk0a.dynasty.budget.BudgetViewModel
import com.nauk0a.dynasty.databinding.AddNewBudgetFragmentBinding
import com.nauk0a.dynasty.income.IncomeBudget
import com.nauk0a.dynasty.utils.ToastFun
import com.nauk0a.dynasty.utils.db
import com.nauk0a.dynasty.utils.onFocusChange
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat


class AddNewBudgetFragment : Fragment() {

    private val viewModel: AddNewBudgetViewModel by activityViewModels()
    private val viewModelBudgetFragment: BudgetViewModel by activityViewModels()
    private var _binding: AddNewBudgetFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var currentMonth: String

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

        val datee = System.currentTimeMillis()
        val format = SimpleDateFormat("MMMM")


        currentMonth = format.format(datee)


        val adapterSpinner = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.month,
            R.layout.spinner_text_color
        )
        adapterSpinner.setDropDownViewResource(R.layout.spinner_text_color)
        binding.spinner.adapter = adapterSpinner
//        binding.spinner.setSelection(convertnumtocharmonths(currentMonth)!!-1)
        viewModelBudgetFragment.spinnerSelectedToAddNewBudget.observe(requireActivity(), Observer {
            binding.spinner.setSelection(it)
        })


        viewModelBudgetFragment.expenseSumm.observe(requireActivity(), Observer {
            val notes: IncomeBudget? = it.toObject(IncomeBudget::class.java)

//TODO  //Сделай и для остальных полей
            val textFieldInputConnection = BaseInputConnection(binding.categoryNameIncomSumm1, true)

            binding.categoryNameIncomSumm1?.doOnTextChanged { inputText, _, _, _ ->
                if (inputText != null) {
                    if (inputText.isNotEmpty()) {
                        if (inputText.toString().toInt() > notes?.incomeSumm1!!.toInt()
                        ) {
                            ToastFun("Noooo")
                            textFieldInputConnection.sendKeyEvent(
                                KeyEvent(
                                    KeyEvent.ACTION_DOWN,
                                    KeyEvent.KEYCODE_DEL
                                )
                            )
                        }
                    }
                }
            }

        })






        binding.addNewBudget.setOnClickListener {
            if (binding.categoryNameEt.text.isNotEmpty() && binding.categotySummEt.text.isNotEmpty()) {
                val summ = binding.categoryNameIncomSumm1.text.toString()
                    .toInt() + binding.categoryNameIncomSumm2.text.toString()
                    .toInt() + binding.categoryNameIncomSumm3.text.toString().toInt()

                if (binding.categotySummEt.text.toString().toInt() > summ) {
                    ToastFun("Неверная сумма. Сумма расходов должна быть меньше или равна общей выделяемой сумме из всех доходов.")
                } else {
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
                            "budgetAddDate" to System.currentTimeMillis(),
                            "month" to "${binding.spinner.selectedItem}"
                        )
                        db.collection(binding.spinner.selectedItem.toString()).document()
                            .set(budget)
                            .addOnSuccessListener {
                                binding.addNewBudget.findNavController()
                                    .navigate(R.id.action_addNewBudgetFragment_to_budgetFragment)
                            }
                            .addOnFailureListener { ex -> ToastFun("Error writing document $ex") }
                    }
                }
            } else {
                ToastFun("Поля не могут быть пустыми")
            }

        }


        binding.categoryNameEt.onFocusChange(
            binding.categoryNameEt,
            getString(R.string.category_name)
        ) {}
        binding.categotySummEt.onFocusChange(
            binding.categotySummEt,
            getString(R.string.edittext_null)
        ) {}
        binding.categoryNameIncomSumm1.onFocusChange(
            binding.categoryNameIncomSumm1,
            getString(R.string.edittext_null)
        ) {}
        binding.categoryNameIncomSumm2.onFocusChange(
            binding.categoryNameIncomSumm2,
            getString(R.string.edittext_null)
        ) {}
        binding.categoryNameIncomSumm3.onFocusChange(
            binding.categoryNameIncomSumm3,
            getString(R.string.edittext_null)
        ) {}
        binding.categoryNameIncom1.onFocusChange(
            binding.categoryNameIncom1,
            getString(R.string.income_one)
        ) {}
        binding.categoryNameIncom2.onFocusChange(
            binding.categoryNameIncom2,
            getString(R.string.income_two)
        ) {}
        binding.categoryNameIncom3.onFocusChange(
            binding.categoryNameIncom3,
            getString(R.string.income_three)
        ) {}

    }


}