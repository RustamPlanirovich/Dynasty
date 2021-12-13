package com.nauk0a.dynasty.setting

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.nauk0a.dynasty.databinding.SettingFragmentBinding
import java.util.concurrent.Executor
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.nauk0a.dynasty.R
import com.nauk0a.dynasty.utils.ToastFun

class SettingFragment : Fragment() {


    private val viewModel: SettingViewModel by activityViewModels()
    private var _binding: SettingFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefs: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SettingFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        prefs = requireActivity().getSharedPreferences("settings", Context.MODE_PRIVATE)


        viewModel.darkThemeEnabled.value = prefs.getBoolean("APP_PREFERENCES_COUNTER", true)


        viewModel.darkThemeEnabled.observe(viewLifecycleOwner, Observer {
            val editor = prefs.edit()
            editor.apply {
                putBoolean("APP_PREFERENCES_COUNTER", it)
                apply()
            }
        })
    }
}