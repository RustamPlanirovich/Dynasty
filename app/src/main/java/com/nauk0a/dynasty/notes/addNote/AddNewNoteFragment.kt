package com.nauk0a.dynasty.notes.addNote

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nauk0a.dynasty.R
import com.nauk0a.dynasty.databinding.AddNewNoteFragmentBinding
import com.nauk0a.dynasty.databinding.NoteFragmentBinding
import com.nauk0a.dynasty.notes.NoteViewModel
import com.nauk0a.dynasty.utils.ToastFun
import com.nauk0a.dynasty.utils.db
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AddNewNoteFragment : Fragment() {

    private val viewModel: AddNewNoteViewModel by activityViewModels()
    private var _binding: AddNewNoteFragmentBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AddNewNoteFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val job = Job()
        val uiScope = CoroutineScope(Dispatchers.Main + job)
        db = Firebase.firestore

        binding.addNewNoteBtn.setOnClickListener {
            uiScope.launch(Dispatchers.IO) {
                val city = hashMapOf(
                    "title" to "${binding.titleTextEt.text}",
                    "notetext" to "${binding.textNoteEt.text}",
                    "date" to System.currentTimeMillis()
                )
                db.collection("notes").document()
                    .set(city)
                    .addOnSuccessListener {

                    }
                    .addOnFailureListener { ex -> ToastFun("Error writing document $ex") }
            }
            it.findNavController().navigate(R.id.action_addNewNoteFragment_to_noteFragment)
        }
    }
}