package com.nauk0a.dynasty.budget

import android.animation.LayoutTransition
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nauk0a.dynasty.R
import com.nauk0a.dynasty.budget.adapter.BudgetAdapter
import com.nauk0a.dynasty.databinding.BudgetFragmentBinding
import com.nauk0a.dynasty.income.IncomeBudget
import com.nauk0a.dynasty.utils.ToastFun
import com.nauk0a.dynasty.utils.db
import java.util.*

class BudgetFragment : Fragment(), BudgetAdapter.BudgetAdapterListener {


    private val viewModel: BudgetViewModel by activityViewModels()
    private var _binding: BudgetFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: BudgetAdapter
    private lateinit var query: Query

    private lateinit var database: DatabaseReference
    private lateinit var currentMonth: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BudgetFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ObjectAnimatorBinding")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = Firebase.firestore
        database = Firebase.database.reference

        val adapterSpinner = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.month,
            R.layout.spinner_text_color
        )
        adapterSpinner.setDropDownViewResource(R.layout.spinner_text_color)
        binding.spinner.adapter = adapterSpinner



        val mCalendar = Calendar.getInstance()
//        val month: String =
//            mCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
        val inf = mCalendar.get(Calendar.MONTH)


//        val datee = System.currentTimeMillis()
//        val format = SimpleDateFormat("MMMM")
        currentMonth = mCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
        binding.spinner.setSelection(inf)

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                currentMonth = binding.spinner.selectedItem.toString()
                query = FirebaseFirestore.getInstance().collection(currentMonth)
                    .orderBy("budgetAddDate", Query.Direction.DESCENDING)

                adapter = BudgetAdapter(query, this@BudgetFragment)
                binding.budgetListRv.setHasFixedSize(true)
                binding.budgetListRv.adapter = adapter

                expensiveSumm()
                adapter.startListening()

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }


        binding.addNewBudgetBtn.setOnClickListener {
            it.findNavController().navigate(R.id.action_budgetFragment_to_addNewBudgetFragment)
            viewModel.spinnerSelectedToAddNewBudget.value = binding.spinner.selectedItemPosition
        }



        binding.card.setOnClickListener {
            if (binding.detailCard.layoutParams.height != 0) {
//                binding.detailCard.visibility = View.GONE
                val layoutTransition = binding.detailCard.layoutTransition
                layoutTransition.setDuration(1000) // Change duration
                layoutTransition.enableTransitionType(LayoutTransition.CHANGING)

                binding.detailCard.layoutParams.height = 0// you can set number, example: 300

                binding.detailCard.requestLayout()

                val layoutTransition1 = binding.expensiveCard.layoutTransition
                layoutTransition1.setDuration(1000) // Change duration
                layoutTransition1.enableTransitionType(LayoutTransition.CHANGING)

                binding.expensiveCard.layoutParams.height = 0// you can set number, example: 300

                binding.expensiveCard.requestLayout()

                val layoutTransition2 = binding.one.layoutTransition
                layoutTransition2.setDuration(1000) // Change duration
                layoutTransition2.enableTransitionType(LayoutTransition.CHANGING)

                binding.one.layoutParams.height = 0// you can set number, example: 300

                binding.one.requestLayout()


            } else {
//                binding.detailCard.visibility = View.VISIBLE
                val layoutTransition = binding.detailCard.layoutTransition
                layoutTransition.setDuration(1000) // Change duration
                layoutTransition.enableTransitionType(LayoutTransition.CHANGING)

                binding.detailCard.layoutParams.height =
                    ViewGroup.LayoutParams.WRAP_CONTENT // you can set number, example: 300

                binding.detailCard.requestLayout()

                val layoutTransition1 = binding.expensiveCard.layoutTransition
                layoutTransition1.setDuration(1000) // Change duration
                layoutTransition1.enableTransitionType(LayoutTransition.CHANGING)

                binding.expensiveCard.layoutParams.height =
                    ViewGroup.LayoutParams.WRAP_CONTENT// you can set number, example: 300

                binding.expensiveCard.requestLayout()

                val layoutTransition2 = binding.one.layoutTransition
                layoutTransition2.setDuration(1000) // Change duration
                layoutTransition2.enableTransitionType(LayoutTransition.CHANGING)

                binding.one.layoutParams.height = 0// you can set number, example: 300

                binding.one.requestLayout()


            }

        }

        expensiveSumm()

    }


    fun expensiveSumm() {
        val source = Source.CACHE
        db.collection("$currentMonth incomeBudget")
            .get()
            .addOnSuccessListener { result ->
                if (result.isEmpty){
                    binding.expensiveSumm.text = "0"
                }else{
                    for (document in result) {

                        val budget: IncomeBudget = document.toObject(IncomeBudget::class.java)
                        var summExpensive =
                            budget.incomeSumm1.toString().toInt() +
                                    budget.incomeSumm2.toString().toInt() +
                                    budget.incomeSumm3.toString().toInt()

                        binding.expensiveSumm.text = summExpensive.toString()
                        binding.incomeOne.text = budget.incomeName1
                        binding.incomeTwo.text = budget.incomeName2
                        binding.incomeThree.text = budget.incomeName3
                        binding.incomeSummCardOne.text = budget.incomeSumm1
                        binding.incomeSummCardTwo.text = budget.incomeSumm2
                        binding.incomeSummCardThree.text = budget.incomeSumm3
                        viewModel.expenseSumm.value = document

                    }
                }


            }
            .addOnFailureListener { exception ->
                ToastFun("Error getting documents. $exception")
            }
    }

    override fun onNotesSelected(notes: DocumentSnapshot) {
        viewModel.dateToDetailBudget.value = notes
        this.findNavController().navigate(R.id.action_budgetFragment_to_detailBudgetFragment)
    }

    override fun delCurrentItem(id: String) {

        db.collection("budget").document(id)
            .delete()
//            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
//            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }

    }

    override fun onEditSelectedBudget(budget: DocumentSnapshot) {
        viewModel.dateToDetailBudgetEdit.value = budget
        this.findNavController().navigate(R.id.action_budgetFragment_to_editBudgetFragment)
    }

    override fun onStart() {
        super.onStart()

    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }


}

class Budget(
    val nameOfTheExpensetle: String? = null,
    val expenseAmount: String? = null,
    val nameOfIncome1: String? = null,
    val nameOfIncome2: String? = null,
    val nameOfIncome3: String? = null,
    val amountOfIncome1: String? = null,
    val amountOfIncome2: String? = null,
    val amountOfIncome3: String? = null,
    val budgetAddDate: Long? = null,
    val month: String? = null,
    val direction: String? = null
)