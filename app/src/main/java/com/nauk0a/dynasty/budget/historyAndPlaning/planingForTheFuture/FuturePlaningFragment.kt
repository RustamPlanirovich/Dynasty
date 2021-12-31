package com.nauk0a.dynasty.budget.historyAndPlaning.planingForTheFuture

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
import com.nauk0a.dynasty.databinding.FuturePlaningFragmentBinding
import com.nauk0a.dynasty.databinding.PurchasesElementItemBinding
import com.nauk0a.dynasty.utils.ToastFun
import com.nauk0a.dynasty.utils.db
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class FuturePlaningFragment : Fragment(), FutureAdapter.FutureAdapterListener {

    private val viewModel: FuturePlaningViewModel by activityViewModels()
    private val viewModelDetail: DetailBudgetViewModel by activityViewModels()
    private var bindingItem: PurchasesElementItemBinding? = null
    private var _binding: FuturePlaningFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var query: Query
    private lateinit var adapter: FutureAdapter
    private lateinit var idDoc: String
    private lateinit var uiScope: CoroutineScope


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FuturePlaningFragmentBinding.inflate(inflater, container, false)
        bindingItem = PurchasesElementItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val job = Job()
        uiScope = CoroutineScope(Dispatchers.Main + job)

        viewModelDetail.docid.observe(requireActivity(), Observer {
            query = FirebaseFirestore.getInstance().collection("purchases $it")
                .orderBy("budgetAddDate", Query.Direction.DESCENDING)
            idDoc = it
        })

        adapter = FutureAdapter(query, this)
        binding.futureRV.setHasFixedSize(true)
        binding.futureRV.adapter = adapter


        binding.addNewElementBtn.setOnClickListener {
            uiScope.launch(Dispatchers.IO) {
                val newDate = hashMapOf(
                    "expensesName" to "${bindingItem?.editPurchasedET?.text}",
                    "checked" to bindingItem?.checkedElement?.isChecked,
                    "priceItem" to "${bindingItem?.priceET?.text}",
                    "budgetAddDate" to System.currentTimeMillis(),
                )
                db.collection("purchases $idDoc").document()
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
        db.collection("purchases $idDoc").document(id).update(
            "checked",
            b
        )
    }

    override fun save(id: String, text: String?, textPrice: String) {
        if (text!!.isNotEmpty()){
            db.collection("purchases $idDoc").document(id).update(
                "expensesName",
                text
            )
        }
        if (textPrice!!.isNotEmpty()){
            db.collection("purchases $idDoc").document(id).update(
                "priceItem",
                textPrice
            )
        }

    }

    override fun moveToCurrentPurchases(notes: Future?, id: String) {


        uiScope.launch(Dispatchers.IO) {
            val newDate = hashMapOf(
                "expensesName" to "${notes?.expensesName}",
                "checked" to notes?.checked,
                "priceItem" to "${notes?.priceItem}",
                "budgetAddDate" to System.currentTimeMillis(),
            )
            db.collection("current purchases $idDoc").document()
                .set(newDate)
                .addOnSuccessListener {
                    delCurrentItem(id)
                }
                .addOnFailureListener { ex -> ToastFun("Error writing document $ex") }
        }
    }

    override fun delCurrentItem(id: String) {
        db.collection("purchases $idDoc").document(id)
            .delete()
    }

}

class Future(
    val expensesName: String? = null,
    val checked: Boolean? = null,
    val budgetAddDate: Long? = null,
    val priceItem: String? = null
)