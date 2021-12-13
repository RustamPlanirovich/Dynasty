package com.nauk0a.dynasty.biometric_login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.nauk0a.dynasty.MainActivity
import com.nauk0a.dynasty.R
import com.nauk0a.dynasty.databinding.BiometricLoginFragmentBinding
import java.util.concurrent.Executor

class BiometricLoginFragment : Fragment() {

    private val viewModel: BiometricLoginViewModel by activityViewModels()
    private var _binding: BiometricLoginFragmentBinding? = null
    private val binding get() = _binding!!
    private val mainActivity = MainActivity()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BiometricLoginFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.biometricLoginButton.setOnClickListener {
            mainActivity.biometric()
        }

    }

}