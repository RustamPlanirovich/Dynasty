package com.nauk0a.dynasty.notes.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.nauk0a.dynasty.R
import com.nauk0a.dynasty.notes.Notes
import java.text.SimpleDateFormat

class NotesAdapter(
    query: Query,
    private val listener: NotesAdapterListener
) : FirestoreAdapter<NotesAdapter.NotesViewHolder>(query) {


    class NotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val title: TextView? = itemView.findViewById(R.id.note_name_tv)
        private val notetext: TextView? = itemView.findViewById(R.id.note_text_tv)
        private val datefb: TextView? = itemView.findViewById(R.id.note_date_tv)
        private val cardView: CardView? = itemView.findViewById(R.id.home_note_card_view)
        private val delBtn: ImageView? = itemView.findViewById(R.id.note_delete_btn)


        @SuppressLint("SimpleDateFormat")
        fun bind(snapshot: DocumentSnapshot, listener: NotesAdapterListener) {
            val notes: Notes? = snapshot.toObject(Notes::class.java)
            val datee = notes?.date
            val format = SimpleDateFormat("yyyy.MM.dd HH:mm")
            title?.text = notes?.title
            notetext?.text = notes?.notetext
            datefb?.text = format.format(datee)


            cardView?.setOnClickListener {
                listener.onNotesSelected(snapshot)
            }
            delBtn?.setOnClickListener {
                listener.delCurrentItem(snapshot.id)
            }


        }
    }

    interface NotesAdapterListener {
        fun onNotesSelected(notes: DocumentSnapshot)
        fun delCurrentItem(id: String)
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        return NotesViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.note_item, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        getSnapshot(position)?.let { snapshot -> holder.bind(snapshot, listener) }
    }
}