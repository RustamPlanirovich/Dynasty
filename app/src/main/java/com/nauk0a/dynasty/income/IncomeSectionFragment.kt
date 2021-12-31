package com.nauk0a.dynasty.income

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nauk0a.dynasty.R
import com.nauk0a.dynasty.budget.Budget
import com.nauk0a.dynasty.budget.adapter.BudgetAdapter
import com.nauk0a.dynasty.databinding.AddBudgetBottomsheetLayoutBinding
import com.nauk0a.dynasty.databinding.IncomeSectionFragmentBinding
import com.nauk0a.dynasty.income.adapter.IncomeHistoryAdapter
import com.nauk0a.dynasty.utils.ToastFun
import com.nauk0a.dynasty.utils.convertnumtocharmonths
import com.nauk0a.dynasty.utils.db
import com.nauk0a.dynasty.utils.onFocusChange
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class IncomeSectionFragment : Fragment(), IncomeHistoryAdapter.IncomeHistoryAdapterListener {

    private val viewModel: IncomeSectionViewModel by activityViewModels()
    private lateinit var _binding: IncomeSectionFragmentBinding
    private lateinit var bindingBottomSheet: AddBudgetBottomsheetLayoutBinding
    private val binding get() = _binding
    private var documentId: String = "no"

    private lateinit var adapter: IncomeHistoryAdapter
    private lateinit var query: Query

    private lateinit var database: DatabaseReference

    private lateinit var currentMonth: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = IncomeSectionFragmentBinding.inflate(inflater, container, false)
        bindingBottomSheet = AddBudgetBottomsheetLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val datee = System.currentTimeMillis()
        val format = SimpleDateFormat("MMMM")
        currentMonth = format.format(datee)

        readBudget()

        val job = Job()
        val uiScope = CoroutineScope(Dispatchers.Main + job)

        db = Firebase.firestore

        database = Firebase.database.reference

        query = FirebaseFirestore.getInstance().collection("${currentMonth} historyBudget")
            .orderBy("incomeAddDate", Query.Direction.DESCENDING)

        adapter = IncomeHistoryAdapter(query, this)
        binding.budgetAddHistoryRV.setHasFixedSize(true)
        binding.budgetAddHistoryRV.adapter = adapter


        val dialog = BottomSheetDialog(this.requireContext(), R.style.BottomSheetDialogTheme)

        dialog.setContentView(bindingBottomSheet.root)

        binding.budgetEditBtn.setOnClickListener {
            dialog.show()
        }

        bindingBottomSheet.spinner.setSelection(convertnumtocharmonths(currentMonth)!!-1)
        bindingBottomSheet.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                currentMonth = bindingBottomSheet.spinner.selectedItem.toString()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        bindingBottomSheet.incomeNameOne.setText(getString(R.string.income_one))
        bindingBottomSheet.incomeNameTwo.setText(getString(R.string.income_two))
        bindingBottomSheet.incomeNameThree.setText(getString(R.string.income_three))
        bindingBottomSheet.incomeSummOne.setText(getString(R.string.edittext_null))
        bindingBottomSheet.incomeSummTwo.setText(getString(R.string.edittext_null))
        bindingBottomSheet.incomeSummThree.setText(getString(R.string.edittext_null))

        bindingBottomSheet.incomeNameOne.onFocusChange(
            bindingBottomSheet.incomeNameOne,
            getString(R.string.income_one)
        ) {}
        bindingBottomSheet.incomeNameTwo.onFocusChange(
            bindingBottomSheet.incomeNameTwo,
            getString(R.string.income_two)
        ) {}
        bindingBottomSheet.incomeNameThree.onFocusChange(
            bindingBottomSheet.incomeNameThree,
            getString(R.string.income_three)
        ) {}
        bindingBottomSheet.incomeSummOne.onFocusChange(
            bindingBottomSheet.incomeSummOne,
            getString(R.string.edittext_null)
        ) {}
        bindingBottomSheet.incomeSummTwo.onFocusChange(
            bindingBottomSheet.incomeSummTwo,
            getString(R.string.edittext_null)
        ) {}
        bindingBottomSheet.incomeSummThree.onFocusChange(
            bindingBottomSheet.incomeSummThree,
            getString(R.string.edittext_null)
        ) {}



        bindingBottomSheet.saveButton.setOnClickListener {

            uiScope.launch(Dispatchers.IO) {

                val budget = hashMapOf(
                    "incomeName1" to "${bindingBottomSheet.incomeNameOne.text}",
                    "incomeName2" to "${bindingBottomSheet.incomeNameTwo.text}",
                    "incomeName3" to "${bindingBottomSheet.incomeNameThree.text}",
                    "incomeSumm1" to "${bindingBottomSheet.incomeSummOne.text}",
                    "incomeSumm2" to "${bindingBottomSheet.incomeSummTwo.text}",
                    "incomeSumm3" to "${bindingBottomSheet.incomeSummThree.text}",
                    "incomeAddDate" to System.currentTimeMillis()
                )

                if (documentId.equals("no")) {
                    db.collection("$currentMonth incomeBudget").document()
                        .set(budget)
                        .addOnSuccessListener {
                            dialog.dismiss()
                            readBudget()
                        }
                        .addOnFailureListener { ex -> ToastFun("Error writing document $ex") }
                } else {
                    db.collection("$currentMonth incomeBudget").document(documentId)
                        .set(budget)
                        .addOnSuccessListener {
                            dialog.dismiss()
                            readBudget()
                        }
                        .addOnFailureListener { ex -> ToastFun("Error writing document $ex") }
                }


                val historyBudget = hashMapOf(
                    "incomeName1" to "${bindingBottomSheet.incomeNameOne.text}",
                    "incomeName2" to "${bindingBottomSheet.incomeNameTwo.text}",
                    "incomeName3" to "${bindingBottomSheet.incomeNameThree.text}",
                    "incomeSumm1" to "${bindingBottomSheet.incomeSummOne.text}",
                    "incomeSumm2" to "${bindingBottomSheet.incomeSummTwo.text}",
                    "incomeSumm3" to "${bindingBottomSheet.incomeSummThree.text}",
                    "incomeAddDate" to System.currentTimeMillis()
                )
                db.collection("$currentMonth historyBudget").document()
                    .set(historyBudget)
                    .addOnSuccessListener {
                        dialog.dismiss()
                    }
                    .addOnFailureListener { ex -> ToastFun("Error writing document $ex") }
            }

        }


    }

    fun readBudget() {
        db.collection("$currentMonth incomeBudget")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {

                    documentId = document.id
                    val budget: IncomeBudget = document.toObject(IncomeBudget::class.java)

                    binding.incomeName1.text = budget.incomeName1
                    binding.incomeName2.text = budget.incomeName2
                    binding.incomeName3.text = budget.incomeName3
                    binding.incomeSumm1.text = budget.incomeSumm1
                    binding.incomeSumm2.text = budget.incomeSumm2
                    binding.incomeSumm3.text = budget.incomeSumm3

                }
            }
            .addOnFailureListener { exception ->
                ToastFun("Error getting documents. $exception")
                documentId = "no"

            }

    }

    override fun onNotesSelected(notes: DocumentSnapshot) {

    }

    override fun delCurrentItem(id: String) {

    }

    override fun onEditSelectedBudget(budget: DocumentSnapshot) {

    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }
}


class IncomeBudget(
    val incomeName1: String? = null,
    val incomeName2: String? = null,
    val incomeName3: String? = null,
    val incomeSumm1: String? = null,
    val incomeSumm2: String? = null,
    val incomeSumm3: String? = null,
    val incomeAddDate: Long? = null
)


