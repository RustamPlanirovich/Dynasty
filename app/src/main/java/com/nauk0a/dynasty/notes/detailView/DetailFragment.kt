package com.nauk0a.dynasty.notes.detailView

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nauk0a.dynasty.R
import com.nauk0a.dynasty.databinding.DetailFragmentBinding
import com.nauk0a.dynasty.databinding.NoteFragmentBinding
import com.nauk0a.dynasty.notes.NoteViewModel
import com.nauk0a.dynasty.notes.Notes
import com.nauk0a.dynasty.utils.ToastFun
import com.nauk0a.dynasty.utils.db
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class DetailFragment : Fragment() {


    private val viewModel: NoteViewModel by activityViewModels()
    private var _binding: DetailFragmentBinding? = null
    private val binding get() = _binding!!
    private var currentNoteId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val job = Job()
        val uiScope = CoroutineScope(Dispatchers.Main + job)
        db = Firebase.firestore

        viewModel.dateToDetail.observe(viewLifecycleOwner, Observer {
            val notes: Notes? = it.toObject(Notes::class.java)
            val datee = notes?.date
            val format = SimpleDateFormat("yyyy.MM.dd HH:mm")
            binding.noteTitleTv.text = notes?.title
            binding.noteDateTv.text = format.format(datee)
            binding.noteTextTv.text = notes?.notetext
            currentNoteId = it.id
        })

        binding.editCurrentNote.setOnClickListener {
            binding.nameCurrentNoteEditEt.apply {
                visibility = View.VISIBLE
                setText(binding.noteTitleTv.text)
            }
            binding.textCurrentNoteEditEt.apply {
                visibility = View.VISIBLE
                setText(binding.noteTextTv.text)
            }
            binding.saveEditNoteBtn.visibility = View.VISIBLE
            binding.noteTitleTv.visibility = View.GONE
            binding.noteTextTv.visibility = View.GONE
            binding.noteDateTv.visibility = View.GONE
            binding.editCurrentNote.visibility = View.GONE
        }

        binding.saveEditNoteBtn.setOnClickListener {
            uiScope.launch(Dispatchers.IO) {
                val newDate = hashMapOf(
                    "title" to "${binding.nameCurrentNoteEditEt.text}",
                    "notetext" to "${binding.textCurrentNoteEditEt.text}",
                    "date" to System.currentTimeMillis()
                )
                db.collection("notes").document(currentNoteId!!)
                    .set(newDate)
                    .addOnSuccessListener {

                    }
                    .addOnFailureListener { ex -> ToastFun("Error writing document $ex") }
            }
            it.findNavController().navigate(R.id.action_detailFragment_to_noteFragment)
        }


    }

}