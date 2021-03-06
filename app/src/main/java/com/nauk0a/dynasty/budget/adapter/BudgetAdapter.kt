package com.nauk0a.dynasty.budget.adapter

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
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
import com.nauk0a.dynasty.budget.BudgetFragment
import com.nauk0a.dynasty.notes.adapter.FirestoreAdapter
import java.text.SimpleDateFormat

class BudgetAdapter(
    query: Query,
    private val listener: BudgetFragment
) : FirestoreAdapter<BudgetAdapter.BudgetViewHolder>(query) {

    class BudgetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

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


        @SuppressLint("SimpleDateFormat", "ObjectAnimatorBinding")
        fun bind(snapshot: DocumentSnapshot, listener: BudgetFragment) {
            val budget: Budget? = snapshot.toObject(Budget::class.java)
            val datee = budget?.budgetAddDate
            val format = SimpleDateFormat("yyyy.MM.dd HH:mm")

            nameOfTheExpensetle?.text = budget?.nameOfTheExpensetle
//            expenseAmount?.text = budget?.expenseAmount
            nameOfIncome1?.text = budget?.nameOfIncome1
            nameOfIncome2?.text = budget?.nameOfIncome2
            nameOfIncome3?.text = budget?.nameOfIncome3
            amountOfIncome1?.text = budget?.amountOfIncome1
            amountOfIncome2?.text = budget?.amountOfIncome2
            amountOfIncome3?.text = budget?.amountOfIncome3
            budgetAddDate?.text = format.format(datee)


            val summ = budget?.amountOfIncome1!!.toInt() +
                    budget.amountOfIncome2!!.toInt() +
                    budget.amountOfIncome3!!.toInt()
//            progress?.progress = ((budget.expenseAmount!!.toDouble() / summ) * 100).toInt()
           

            ObjectAnimator.ofInt(
                progress,
                "progress",
                ((budget.expenseAmount!!.toDouble() / summ) * 100).toInt()
            )
                .setDuration(1000)
                .start()

            val animator = ValueAnimator.ofInt(0, budget.expenseAmount.toString().toInt())
            animator.duration = 1000
            animator.addUpdateListener { animator ->
                expenseAmount?.text = animator.animatedValue.toString()
            }
            animator.start()

            if (budget?.expenseAmount.toInt() == 0){
                budgetItevCV?.isEnabled = false
                editBudgetItem?.isEnabled = false
                delBtn?.isEnabled = false
            }

            budgetItevCV?.setOnClickListener {
                listener.onNotesSelected(snapshot)
            }
            delBtn?.setOnClickListener {
                listener.delCurrentItem(snapshot.id)
            }
            editBudgetItem?.setOnClickListener {
                listener.onEditSelectedBudget(snapshot)
            }
        }
    }


    interface BudgetAdapterListener {
        fun onNotesSelected(notes: DocumentSnapshot)
        fun delCurrentItem(id: String)
        fun onEditSelectedBudget(budget: DocumentSnapshot)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetViewHolder {
        return BudgetViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.budget_item, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: BudgetViewHolder, position: Int) {
        getSnapshot(position)?.let { snapshot -> holder.bind(snapshot, listener) }
    }
}

