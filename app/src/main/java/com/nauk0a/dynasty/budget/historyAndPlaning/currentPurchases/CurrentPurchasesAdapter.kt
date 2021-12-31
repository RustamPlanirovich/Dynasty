package com.nauk0a.dynasty.budget.historyAndPlaning.currentPurchases

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.nauk0a.dynasty.R
import com.nauk0a.dynasty.budget.historyAndPlaning.planingForTheFuture.Future
import com.nauk0a.dynasty.notes.adapter.FirestoreAdapter

class CurrentPurchasesAdapter(
    query: Query,
    private val listener: CurrentPurchasesAdapterListener
) : FirestoreAdapter<CurrentPurchasesAdapter.CurrentPurchasesViewHolder>(query) {

    class CurrentPurchasesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val expensesName: TextView? =
            itemView.findViewById(R.id.purchasedName)
        private val checked: CheckBox? = itemView.findViewById(R.id.checkedElement)
        private val cardPurchases: CardView? = itemView.findViewById(R.id.cardPurchases)
        private val editPurchasedET: EditText? = itemView.findViewById(R.id.editPurchasedET)
        private val saveEditPurchases: ImageView? = itemView.findViewById(R.id.saveEditPurchases)
        private val layout: ConstraintLayout? = itemView.findViewById(R.id.layout)
        private val delBtn: ImageView? = itemView.findViewById(R.id.del_item)
        private val price: TextView? =
            itemView.findViewById(R.id.price)
        private val priceET: EditText? = itemView.findViewById(R.id.priceET)


        @SuppressLint("SimpleDateFormat")
        fun bind(
            snapshot: DocumentSnapshot,
            listener: CurrentPurchasesAdapter.CurrentPurchasesAdapterListener
        ) {
            val notes: Future? = snapshot.toObject(Future::class.java)


            expensesName?.text = notes?.expensesName
            checked?.setOnCheckedChangeListener(null)
            checked?.isChecked = notes?.checked!!
            price?.text = notes.priceItem



            if (notes.checked) {
                expensesName?.paintFlags =
                    expensesName?.paintFlags!! or (Paint.STRIKE_THRU_TEXT_FLAG)
                price?.paintFlags = price?.paintFlags!! or (Paint.STRIKE_THRU_TEXT_FLAG)
                expensesName.isEnabled = false
                price.isEnabled = false
            } else {
                expensesName?.paintFlags =
                    expensesName?.paintFlags!! and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                price?.paintFlags = price?.paintFlags!! and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                expensesName.isEnabled = true
                price.isEnabled = true
            }

            checked?.setOnCheckedChangeListener { compoundButton, b ->
                listener.onNotesSelected(snapshot, b, snapshot.id)
            }


            layout?.setOnClickListener {
                if (editPurchasedET!!.isVisible) {

                } else {
                    editPurchasedET.setText(expensesName?.text)
                    expensesName?.visibility = View.GONE
                    checked?.visibility = View.GONE
                    editPurchasedET?.visibility = View.VISIBLE
                    saveEditPurchases?.visibility = View.VISIBLE
                    delBtn?.visibility = View.GONE
                    priceET?.visibility = View.VISIBLE
                    priceET?.setText(price?.text)
                    price?.visibility = View.GONE
                }


            }
            delBtn?.setOnClickListener {
                listener.delCurrentItem(snapshot.id)
            }

            saveEditPurchases?.setOnClickListener {
                listener.save(
                    snapshot.id,
                    editPurchasedET?.text.toString(),
                    priceET?.text.toString()
                )
                expensesName?.visibility = View.VISIBLE
                editPurchasedET?.text?.clear()
                checked?.visibility = View.VISIBLE
                editPurchasedET?.visibility = View.GONE
                saveEditPurchases?.visibility = View.GONE
                delBtn?.visibility = View.VISIBLE
                priceET?.visibility = View.GONE
                priceET?.text?.clear()
                price?.visibility = View.VISIBLE
            }

        }
    }


    interface CurrentPurchasesAdapterListener {
        fun onNotesSelected(notes: DocumentSnapshot, b: Boolean, id: String)
        fun delCurrentItem(id: String)
        fun save(id: String, text: String?, toString: String)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CurrentPurchasesAdapter.CurrentPurchasesViewHolder {
        return CurrentPurchasesAdapter.CurrentPurchasesViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.purchases_element_item, parent, false
            )
        )
    }

    override fun onBindViewHolder( holder: CurrentPurchasesAdapter.CurrentPurchasesViewHolder,
        position: Int
    ) {
        getSnapshot(position)?.let { snapshot -> holder.bind(snapshot, listener) }
    }
}