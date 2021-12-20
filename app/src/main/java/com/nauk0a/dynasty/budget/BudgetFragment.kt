package com.nauk0a.dynasty.budget

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nauk0a.dynasty.R
import com.nauk0a.dynasty.budget.adapter.BudgetAdapter
import com.nauk0a.dynasty.databinding.BudgetFragmentBinding
import com.nauk0a.dynasty.databinding.NoteFragmentBinding
import com.nauk0a.dynasty.notes.adapter.NotesAdapter
import com.nauk0a.dynasty.utils.db

class BudgetFragment : Fragment(), BudgetAdapter.BudgetAdapterListener {


    private val viewModel: BudgetViewModel by activityViewModels()
    private var _binding: BudgetFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: BudgetAdapter
    private lateinit var query: Query

    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BudgetFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = Firebase.firestore
        database = Firebase.database.reference

        query = FirebaseFirestore.getInstance().collection("budget")
            .orderBy("budgetAddDate", Query.Direction.DESCENDING)

        adapter = BudgetAdapter(query, this)
        binding.budgetListRv.setHasFixedSize(true)
        binding.budgetListRv.adapter = adapter

        binding.addNewBudgetBtn.setOnClickListener {
            it.findNavController().navigate(R.id.action_budgetFragment_to_addNewBudgetFragment)
        }

    }

    override fun onNotesSelected(notes: DocumentSnapshot) {
//            viewModel.dateToDetail.value = notes
//            this.findNavController().navigate(R.id.action_noteFragment_to_detailFragment)
    }

    override fun delCurrentItem(id: String) {
        TODO("Not yet implemented")
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

class Budget(
    val nameOfTheExpensetle: String? = null,
    val expenseAmount: String? = null,
    val nameOfIncome1: String? = null,
    val nameOfIncome2: String? = null,
    val nameOfIncome3: String? = null,
    val amountOfIncome1: String? = null,
    val amountOfIncome2: String? = null,
    val amountOfIncome3: String? = null,
    val budgetAddDate: Long? = null
)