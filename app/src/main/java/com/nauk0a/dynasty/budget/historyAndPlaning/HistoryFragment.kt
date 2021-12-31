package com.nauk0a.dynasty.budget.historyAndPlaning

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.nauk0a.dynasty.budget.DetailBudgetViewModel
import com.nauk0a.dynasty.databinding.HistoryFragmentBinding

class HistoryFragment : Fragment(), HistoryAdapter.HistoryAdapterListener {
    private val viewModel: HistoryViewModel by activityViewModels()
    private val viewModelDetail: DetailBudgetViewModel by activityViewModels()
    private var _binding: HistoryFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: HistoryAdapter
    private lateinit var query: Query


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = HistoryFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModelDetail.docid.observe(requireActivity(), Observer {
            query = FirebaseFirestore.getInstance().collection("history $it")
                .orderBy("budgetAddDate", Query.Direction.DESCENDING)
        })



        adapter = HistoryAdapter(query, this@HistoryFragment)
        binding.historyRV.setHasFixedSize(true)
        binding.historyRV.adapter = adapter
    }

    override fun onNotesSelected(notes: DocumentSnapshot) {

    }

    override fun delCurrentItem(id: String) {

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