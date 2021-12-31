package com.nauk0a.dynasty.budget

import android.os.Bundle
import android.text.InputType
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.BaseInputConnection
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayoutMediator
import com.nauk0a.dynasty.R
import com.nauk0a.dynasty.budget.historyAndPlaning.TabAdapter
import com.nauk0a.dynasty.databinding.DetailBudgetFragmentBinding
import com.nauk0a.dynasty.databinding.EditBudgetSummBinding
import com.nauk0a.dynasty.income.IncomeBudget
import com.nauk0a.dynasty.utils.ToastFun
import com.nauk0a.dynasty.utils.db
import com.nauk0a.dynasty.utils.onFocusChange
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat


class DetailBudgetFragment : Fragment() {


    private val viewModel: DetailBudgetViewModel by activityViewModels()
    private val viewModelBudget: BudgetViewModel by activityViewModels()
    private var _binding: DetailBudgetFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var bindingEditSumm: EditBudgetSummBinding
    private lateinit var id: String
    private lateinit var budgetNew: IncomeBudget
    private lateinit var documentId: String
    private lateinit var currentMonth: String

    private lateinit var adapter: TabAdapter

    private val tabNames: Array<String> = arrayOf(
        "Текущие покупки",
        "Планирование",
        "История",
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DetailBudgetFragmentBinding.inflate(inflater, container, false)
        bindingEditSumm = EditBudgetSummBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TabAdapter(this)
        binding.pager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.text = tabNames[position]
        }.attach()


        val datee = System.currentTimeMillis()
        val format = SimpleDateFormat("MMMM")
        currentMonth = format.format(datee)
        val job = Job()
        val uiScope = CoroutineScope(Dispatchers.Main + job)

        val dialog = BottomSheetDialog(this.requireContext(), R.style.BottomSheetDialogTheme)

        dialog.setContentView(bindingEditSumm.root)
        bindingEditSumm.newSumm.setInputType(InputType.TYPE_CLASS_NUMBER)


        bindingEditSumm.newSumm.onFocusChange(bindingEditSumm.newSumm, "0") {}

        viewModelBudget.dateToDetailBudget.observe(requireActivity(), {
            val detailDate = it.toObject(Budget::class.java)
            viewModel.docid.value = it.id
            id = it.id
            binding.categoryNameEt.text = detailDate?.nameOfTheExpensetle
            binding.categotySummEt.text = detailDate?.expenseAmount
            binding.categoryNameIncom1.text = detailDate?.nameOfIncome1
            binding.categoryNameIncom2.text = detailDate?.nameOfIncome2
            binding.categoryNameIncom3.text = detailDate?.nameOfIncome3
            binding.categoryNameIncomSumm1.text = detailDate?.amountOfIncome1
            binding.categoryNameIncomSumm2.text = detailDate?.amountOfIncome2
            binding.categoryNameIncomSumm3.text = detailDate?.amountOfIncome3
            bindingEditSumm.radioButton.text = detailDate?.nameOfIncome1
            bindingEditSumm.radioButton2.text = detailDate?.nameOfIncome2
            bindingEditSumm.radioButton3.text = detailDate?.nameOfIncome3
            currentMonth = detailDate?.month.toString()
        })


        val textFieldInputConnection = BaseInputConnection(bindingEditSumm.newSumm, true)
        binding.editBudgetSumm.setOnClickListener {
            if (binding.categoryNameIncomSumm1.text.toString().toInt() == 0) {
                bindingEditSumm.radioButton.isEnabled = false
            } else {
                bindingEditSumm.radioButton.isChecked = true

                bindingEditSumm.newSumm?.doOnTextChanged { inputText, _, _, _ ->
                    if (inputText != null) {
                        if (inputText.isNotEmpty()) {
                            if (inputText.toString()
                                    .toInt() > binding.categoryNameIncomSumm1.text.toString()
                                    .toInt()
                            ) {
                                ToastFun("Сумма списания не может быть больше выделенной")
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
            }
            if (binding.categoryNameIncomSumm2.text.toString().toInt() == 0) {
                bindingEditSumm.radioButton2.isEnabled = false
            } else {
                bindingEditSumm.radioButton2.isChecked = true
                bindingEditSumm.newSumm?.doOnTextChanged { inputText, _, _, _ ->
                    if (inputText != null) {
                        if (inputText.isNotEmpty()) {
                            if (inputText.toString()
                                    .toInt() > binding.categoryNameIncomSumm2.text.toString()
                                    .toInt()
                            ) {
                                ToastFun("Сумма списания не может быть больше выделенной")
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
            }
            if (binding.categoryNameIncomSumm3.text.toString().toInt() == 0) {
                bindingEditSumm.radioButton3.isEnabled = false
            } else {
                bindingEditSumm.radioButton3.isChecked = true
                bindingEditSumm.newSumm?.doOnTextChanged { inputText, _, _, _ ->
                    if (inputText != null) {
                        if (inputText.isNotEmpty()) {
                            if (inputText.toString()
                                    .toInt() > binding.categoryNameIncomSumm3.text.toString()
                                    .toInt()
                            ) {
                                ToastFun("Сумма списания не может быть больше выделенной")
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
            }
            dialog.show()
        }

        expensiveSumm()

        bindingEditSumm.editSumm.setOnClickListener {

            if (bindingEditSumm.radioButton.isChecked) {
                binding.categoryNameIncomSumm1.text =
                    (binding.categoryNameIncomSumm1.text.toString().toInt() -
                            bindingEditSumm.newSumm.text.toString().toInt()).toString()
            }
            if (bindingEditSumm.radioButton2.isChecked) {
                binding.categoryNameIncomSumm2.text =
                    (binding.categoryNameIncomSumm2.text.toString().toInt() -
                            bindingEditSumm.newSumm.text.toString().toInt()).toString()
            }
            if (bindingEditSumm.radioButton3.isChecked) {
                binding.categoryNameIncomSumm3.text =
                    (binding.categoryNameIncomSumm3.text.toString().toInt() -
                            bindingEditSumm.newSumm.text.toString().toInt()).toString()
            }
            uiScope.launch(Dispatchers.IO) {

                val budget = hashMapOf(
                    "nameOfTheExpensetle" to "${binding.categoryNameEt.text}",
                    "expenseAmount" to "${
                        binding.categotySummEt.text.toString()
                            .toInt() - bindingEditSumm.newSumm.text.toString().toInt()
                    }",
                    "nameOfIncome1" to "${binding.categoryNameIncom1.text}",
                    "nameOfIncome2" to "${binding.categoryNameIncom2.text}",
                    "nameOfIncome3" to "${binding.categoryNameIncom3.text}",
                    "amountOfIncome1" to "${binding.categoryNameIncomSumm1.text}",
                    "amountOfIncome2" to "${binding.categoryNameIncomSumm2.text}",
                    "amountOfIncome3" to "${binding.categoryNameIncomSumm3.text}",
                    "budgetAddDate" to System.currentTimeMillis(),
                    "month" to "$currentMonth"
                )
                db.collection(currentMonth).document(id)
                    .set(budget)
                    .addOnSuccessListener {
                        historySave()
                        dialog.dismiss()
                        new()
                    }
                    .addOnFailureListener { ex -> ToastFun("Error writing document $ex") }

                if (bindingEditSumm.radioButton.isChecked) {
                    db.collection("$currentMonth incomeBudget").document(documentId).update(
                        "incomeSumm1",
                        "${
                            budgetNew.incomeSumm1.toString()
                                .toInt() - bindingEditSumm.newSumm.text.toString().toInt()
                        }"
                    )
                }
                if (bindingEditSumm.radioButton2.isChecked) {
                    db.collection("$currentMonth incomeBudget").document(documentId).update(
                        "incomeSumm2",
                        "${
                            budgetNew.incomeSumm2.toString()
                                .toInt() - bindingEditSumm.newSumm.text.toString().toInt()
                        }"
                    )
                }
                if (bindingEditSumm.radioButton3.isChecked) {
                    db.collection("$currentMonth incomeBudget").document(documentId).update(
                        "incomeSumm3",
                        "${
                            budgetNew.incomeSumm3.toString()
                                .toInt() - bindingEditSumm.newSumm.text.toString().toInt()
                        }"
                    )
                }

            }
        }
    }

    fun historySave() {
        var direction = ""
        if (bindingEditSumm.newSumm.text.toString().toInt() > binding.categotySummEt.text.toString()
                .toInt()
        ) {
            direction = "+"
        } else {
            direction = "-"
        }
        val budget = hashMapOf(
            "nameOfTheExpensetle" to "${binding.categoryNameEt.text}",
            "expenseAmount" to "${
                binding.categotySummEt.text.toString()
                    .toInt() - bindingEditSumm.newSumm.text.toString().toInt()
            }",
            "nameOfIncome1" to "${binding.categoryNameIncom1.text}",
            "nameOfIncome2" to "${binding.categoryNameIncom2.text}",
            "nameOfIncome3" to "${binding.categoryNameIncom3.text}",
            "amountOfIncome1" to "${binding.categoryNameIncomSumm1.text}",
            "amountOfIncome2" to "${binding.categoryNameIncomSumm2.text}",
            "amountOfIncome3" to "${binding.categoryNameIncomSumm3.text}",
            "budgetAddDate" to System.currentTimeMillis(),
            "month" to "$currentMonth",
            "direction" to direction
        )
        db.collection("history $id").document()
            .set(budget)
            .addOnSuccessListener {
            }
            .addOnFailureListener { ex -> ToastFun("Error writing document $ex") }
    }

    fun new() {
        db.collection(currentMonth).document(id)
            .get()
            .addOnSuccessListener { result ->

                val detail = result.toObject(Budget::class.java)
                binding.categoryNameEt.text = detail?.nameOfTheExpensetle
                binding.categotySummEt.text = detail?.expenseAmount
                binding.categoryNameIncom1.text = detail?.nameOfIncome1
                binding.categoryNameIncom2.text = detail?.nameOfIncome2
                binding.categoryNameIncom3.text = detail?.nameOfIncome3
                binding.categoryNameIncomSumm1.text = detail?.amountOfIncome1
                binding.categoryNameIncomSumm2.text = detail?.amountOfIncome2
                binding.categoryNameIncomSumm3.text = detail?.amountOfIncome3

            }
            .addOnFailureListener { exception ->

            }
    }

    fun expensiveSumm() {

        db.collection("$currentMonth incomeBudget")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    budgetNew = document.toObject(IncomeBudget::class.java)
                    documentId = document.id

                }
            }
            .addOnFailureListener { exception ->
                ToastFun("Error getting documents. $exception")

            }

    }

}