package com.nauk0a.dynasty.income.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.nauk0a.dynasty.R
import com.nauk0a.dynasty.income.IncomeBudget
import com.nauk0a.dynasty.notes.adapter.FirestoreAdapter
import java.text.SimpleDateFormat

class IncomeHistoryAdapter(
    query: Query,
    private val listener: IncomeHistoryAdapterListener
) : FirestoreAdapter<IncomeHistoryAdapter.IncomeHistoryViewHolder>(query) {

    class IncomeHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val incomeName1: TextView? = itemView.findViewById(R.id.income_name_h1)
        private val incomeName2: TextView? = itemView.findViewById(R.id.income_name_h2)
        private val incomeName3: TextView? = itemView.findViewById(R.id.income_name_h3)
        private val incomeSumm1: TextView? = itemView.findViewById(R.id.income_summ_h1)
        private val incomeSumm2: TextView? = itemView.findViewById(R.id.income_summ_h2)
        private val incomeSumm3: TextView? = itemView.findViewById(R.id.income_summ_h3)
        private val incomeAddDate: TextView? = itemView.findViewById(R.id.income_date)


        @SuppressLint("SimpleDateFormat")
        fun bind(snapshot: DocumentSnapshot, listener: IncomeHistoryAdapterListener) {
            val budget: IncomeBudget? = snapshot.toObject(IncomeBudget::class.java)
            val datee = budget?.incomeAddDate
            val format = SimpleDateFormat("yyyy.MM.dd HH:mm")
            incomeName1?.text = budget?.incomeName1
            incomeName2?.text = budget?.incomeName2
            incomeName3?.text = budget?.incomeName3
            incomeSumm1?.text = budget?.incomeSumm1
            incomeSumm2?.text = budget?.incomeSumm2
            incomeSumm3?.text = budget?.incomeSumm3
            incomeAddDate?.text = format.format(datee)


            incomeName1?.setOnClickListener {
                listener.onNotesSelected(snapshot)
            }
            incomeName2?.setOnClickListener {
                listener.delCurrentItem(snapshot.id)
            }
            incomeName3?.setOnClickListener {
                listener.onEditSelectedBudget(snapshot)
            }
        }
    }


    interface IncomeHistoryAdapterListener {
        fun onNotesSelected(notes: DocumentSnapshot)
        fun delCurrentItem(id: String)
        fun onEditSelectedBudget(budget: DocumentSnapshot)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IncomeHistoryViewHolder {
        return IncomeHistoryViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.budget_history_item, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: IncomeHistoryViewHolder, position: Int) {
        getSnapshot(position)?.let { snapshot -> holder.bind(snapshot, listener) }
    }
}