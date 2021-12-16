package com.nauk0a.dynasty.notes

import android.content.Intent
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nauk0a.dynasty.R
import com.nauk0a.dynasty.databinding.NoteFragmentBinding
import com.nauk0a.dynasty.notes.adapter.NotesAdapter
import com.nauk0a.dynasty.notes.detailView.DetailFragment
import com.nauk0a.dynasty.utils.ToastFun
import com.nauk0a.dynasty.utils.db
import kotlinx.coroutines.*

class NoteFragment : Fragment(), NotesAdapter.NotesAdapterListener {


    private val viewModel: NoteViewModel by activityViewModels()
    private var _binding: NoteFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: NotesAdapter
    private lateinit var query: Query

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


        val job = Job()
        val uiScope = CoroutineScope(Dispatchers.Main + job)
        query = FirebaseFirestore.getInstance().collection("notes")

        adapter = NotesAdapter(query,this)
        binding.notesListRv.setHasFixedSize(true)
        binding.notesListRv.adapter = adapter

        binding.addNewNoteBtn.setOnClickListener {

            uiScope.launch(Dispatchers.IO) {
                val city = hashMapOf(
                    "title" to "Los Angeles1",
                    "notetext" to "CA",
                    "date" to System.currentTimeMillis()
                )

                db.collection("notes").document()
                    .set(city)
                    .addOnSuccessListener {
                        Log.d(
                            "Hello",
                            "DocumentSnapshot successfully written!"
                        )
                    }
                    .addOnFailureListener { ex -> ToastFun("Error writing document $ex") }
            }
        }
    }

    override fun onNotesSelected(notes: Notes?) {
        viewModel.dateToDetail.value = notes
        this.findNavController().navigate(R.id.action_noteFragment_to_detailFragment)
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