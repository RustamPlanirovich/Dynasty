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
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class HomeFragment : Fragment() {


    private val viewModel: HomeViewModel? = null
    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = HomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val viewModel: HomeViewModel by lazy {
            ViewModelProvider(
                this,
                object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        val networkStatusTracker = NetworkStatusTracker(requireActivity())
                        return HomeViewModel(networkStatusTracker) as T
                    }
                },
            ).get(HomeViewModel::class.java)
        }


        binding.btnSetting.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_settingFragment)
        }

        viewModel.state.observe(requireActivity()) { state ->
            when (state) {
                MyState.Fetched -> binding.noInternetConnection.visibility = View.GONE
                MyState.Error -> binding.noInternetConnection.visibility = View.VISIBLE


            }
        }



//        when (isOnline(requireContext())) {
//            false -> binding.noInternetConnection.visibility = View.VISIBLE
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    return true
                }
            }
        }
        return false
    }


}