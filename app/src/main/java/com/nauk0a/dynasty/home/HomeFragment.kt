package com.nauk0a.dynasty.home

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.nauk0a.dynasty.R
import com.nauk0a.dynasty.databinding.HomeFragmentBinding
import com.nauk0a.dynasty.internet_connection.NetworkStatusTracker
import com.nauk0a.dynasty.mainActivityPackage.MainViewModel
import com.nauk0a.dynasty.mainActivityPackage.MyState
import kotlinx.coroutines.ExperimentalCoroutinesApi


class HomeFragment : Fragment() {


    private val viewModel: HomeViewModel by activityViewModels()

    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)


        binding.btnSetting.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_settingFragment)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}