package com.nauk0a.dynasty.budget.historyAndPlaning

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.nauk0a.dynasty.R
import com.nauk0a.dynasty.budget.Budget
import com.nauk0a.dynasty.notes.adapter.FirestoreAdapter
import java.text.SimpleDateFormat

class HistoryAdapter(
    query: Query,
    private val listener: HistoryAdapterListener
) : FirestoreAdapter<HistoryAdapter.HistoryViewHolder>(query) {

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val nameOfTheExpensetle: TextView? =
            itemView.findViewById(R.id.name_of_the_expense)
        private val expenseAmount: TextView? = itemView.findViewById(R.id.expense_amount)
        private val nameOfIncome1: TextView? = itemView.findViewById(R.id.name_of_income_1)
        private val nameOfIncome2: TextView? = itemView.findViewById(R.id.name_of_income_2)
        private val nameOfIncome3: TextView? = itemView.findViewById(R.id.name_of_income_3)
        private val amountOfIncome1: TextView? = itemView.findViewById(R.id.amount_of_income_1)
        private val amountOfIncome2: TextView? = itemView.findViewById(R.id.amount_of_income_2)
        private val amountOfIncome3: TextView? = itemView.findViewById(R.id.amount_of_income_3)
        private val editBudgetItem: ImageView? = itemView.findViewById(R.id.edit_budget_item)
        private val budgetAddDate: TextView? = itemView.findViewById(R.id.budget_add_date)
        private val delBtn: ImageView? = itemView.findViewById(R.id.del_budget_item_btn)
        private val budgetItevCV: CardView? = itemView.findViewById(R.id.budget_card_view)
        private val progress: ProgressBar? = itemView.findViewById(R.id.budgetProgressBar)
        private val direction: TextView? = itemView.findViewById(R.id.direction)


        @SuppressLint("SimpleDateFormat")
        fun bind(snapshot: DocumentSnapshot, listener: HistoryAdapterListener) {
            val notes: Budget? = snapshot.toObject(Budget::class.java)
            val datee = notes?.budgetAddDate
            val format = SimpleDateFormat("yyyy.MM.dd HH:mm")

            nameOfTheExpensetle?.text = notes?.nameOfTheExpensetle
            expenseAmount?.text = notes?.expenseAmount
            nameOfIncome1?.text = notes?.nameOfIncome1
            nameOfIncome2?.text = notes?.nameOfIncome2
            nameOfIncome3?.text = notes?.nameOfIncome3
            amountOfIncome1?.text = notes?.amountOfIncome1
            amountOfIncome2?.text = notes?.amountOfIncome2
            amountOfIncome3?.text = notes?.amountOfIncome3
            direction?.text = notes?.direction

            budgetAddDate?.text = format.format(datee)



            budgetItevCV?.setOnClickListener {
                listener.onNotesSelected(snapshot)
            }
            delBtn?.setOnClickListener {
                listener.delCurrentItem(snapshot.id)
            }
        }
    }


    interface HistoryAdapterListener {
        fun onNotesSelected(notes: DocumentSnapshot)
        fun delCurrentItem(id: String)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryAdapter.HistoryViewHolder {
        return HistoryAdapter.HistoryViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.history_budget_item, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: HistoryAdapter.HistoryViewHolder, position: Int) {
        getSnapshot(position)?.let { snapshot -> holder.bind(snapshot, listener) }
    }
}