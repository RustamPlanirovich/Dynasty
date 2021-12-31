package com.nauk0a.dynasty.budget.historyAndPlaning.currentPurchases

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
import com.nauk0a.dynasty.databinding.CurrentPurchasesFragmentBinding
import com.nauk0a.dynasty.databinding.PurchasesElementItemBinding
import com.nauk0a.dynasty.utils.ToastFun
import com.nauk0a.dynasty.utils.db
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CurrentPurchasesFragment : Fragment(), CurrentPurchasesAdapter.CurrentPurchasesAdapterListener {

    private val viewModel: CurrentPurchasesViewModel by activityViewModels()
    private val viewModelDetail: DetailBudgetViewModel by activityViewModels()
    private var _binding: CurrentPurchasesFragmentBinding? = null
    private val binding get() = _binding!!
    private var bindingItem: PurchasesElementItemBinding? = null
    private lateinit var query: Query
    private lateinit var adapter: CurrentPurchasesAdapter
    private lateinit var idDoc: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CurrentPurchasesFragmentBinding.inflate(inflater, container, false)
        bindingItem = PurchasesElementItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val job = Job()
        val uiScope = CoroutineScope(Dispatchers.Main + job)

        viewModelDetail.docid.observe(requireActivity(), Observer {
            query = FirebaseFirestore.getInstance().collection("current purchases $it")
                .orderBy("budgetAddDate", Query.Direction.DESCENDING)
            idDoc = it
        })

        adapter = CurrentPurchasesAdapter(query, this)
        binding.currentPurchases.setHasFixedSize(true)
        binding.currentPurchases.adapter = adapter

        binding.addNewElementBtn.setOnClickListener {
            uiScope.launch(Dispatchers.IO) {
                val newDate = hashMapOf(
                    "expensesName" to "${bindingItem?.editPurchasedET?.text}",
                    "checked" to bindingItem?.checkedElement?.isChecked,
                    "priceItem" to "${bindingItem?.priceET?.text}",
                    "budgetAddDate" to System.currentTimeMillis(),
                )
                db.collection("current purchases $idDoc").document()
                    .set(newDate)
                    .addOnSuccessListener {
                    }
                    .addOnFailureListener { ex -> ToastFun("Error writing document $ex") }
            }
        }

    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onNotesSelected(notes: DocumentSnapshot, b: Boolean, id: String) {
        db.collection("current purchases $idDoc").document(id).update(
            "checked",
            b
        )
    }

    override fun delCurrentItem(id: String) {
        db.collection("current purchases $idDoc").document(id)
            .delete()
    }

    override fun save(id: String, text: String?, textPrice: String) {
        if (text!!.isNotEmpty()){
            db.collection("current purchases $idDoc").document(id).update(
                "expensesName",
                text
            )
        }
        if (textPrice!!.isNotEmpty()){
            db.collection("current purchases $idDoc").document(id).update(
                "priceItem",
                textPrice
            )
        }
    }



}