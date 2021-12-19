package com.nauk0a.dynasty.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
import com.nauk0a.dynasty.databinding.NoteFragmentBinding
import com.nauk0a.dynasty.notes.adapter.NotesAdapter
import com.nauk0a.dynasty.utils.db
import java.util.*


class NoteFragment : Fragment(), NotesAdapter.NotesAdapterListener {


    private val viewModel: NoteViewModel by activityViewModels()
    private var _binding: NoteFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: NotesAdapter
    private lateinit var query: Query

    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = NoteFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = Firebase.firestore
        database = Firebase.database.reference

        query = FirebaseFirestore.getInstance().collection("notes").orderBy("date", Query.Direction.DESCENDING)
//        query.orderBy("date", Query.Direction.DESCENDING)





        adapter = NotesAdapter(query, this)
        binding.notesListRv.setHasFixedSize(true)
//        binding.notesListRv.itemAnimator = null
        binding.notesListRv.adapter = adapter
        binding.addNewNoteBtn.setOnClickListener {
            it.findNavController().navigate(R.id.action_noteFragment_to_addNewNoteFragment)

//            val panelIntent = Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY)
//            startActivity(panelIntent )

//
        }
    }

    override fun onNotesSelected(notes: DocumentSnapshot) {
        viewModel.dateToDetail.value = notes
        this.findNavController().navigate(R.id.action_noteFragment_to_detailFragment)
    }

    override fun delCurrentItem(id: String) {
        db.collection("notes").document(id)
            .delete()
//            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
//            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
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


class Notes(
    val title: String? = null,
    val notetext: String? = null,
    val date: Long? = null
)