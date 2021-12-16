package com.nauk0a.dynasty.notes.detailView

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.nauk0a.dynasty.R
import com.nauk0a.dynasty.databinding.DetailFragmentBinding
import com.nauk0a.dynasty.databinding.NoteFragmentBinding
import com.nauk0a.dynasty.notes.NoteViewModel
import java.text.SimpleDateFormat

class DetailFragment : Fragment() {


    private  val viewModel: NoteViewModel by activityViewModels()
    private var _binding: DetailFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.dateToDetail.observe(viewLifecycleOwner, Observer{
            val datee = it.date
            val format = SimpleDateFormat("yyyy.MM.dd HH:mm")
            binding.noteTitleTv.text = it.title
            binding.noteDateTv.text = format.format(datee)
            binding.noteTextTv.text = it.notetext
        })


    }

}