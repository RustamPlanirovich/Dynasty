package com.nauk0a.dynasty

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nauk0a.dynasty.databinding.ActivityMainBinding
import com.nauk0a.dynasty.utils.APP_ACTIVITY
import java.util.concurrent.Executor

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding

    private lateinit var prefs: SharedPreferences
    private lateinit var navController: NavController


    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        executor = ContextCompat.getMainExecutor(this@MainActivity)
        //передаем в константы текущий контекст
        APP_ACTIVITY = this

        prefs = getSharedPreferences("settings", Context.MODE_PRIVATE)



        biometric()

        if (BiometricManager.from(this).canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS){

            binding?.biometricLoginButton?.setOnClickListener {
                biometricPrompt.authenticate(promptInfo)
            }


            val biometricLoginCheck = prefs.getBoolean("APP_PREFERENCES_COUNTER", true)
            when (biometricLoginCheck) {
                true -> biometricPrompt.authenticate(promptInfo)
            }
        }


        //Отключение пункта меню в BottomNavigation
//        bottomNav.menu.findItem(R.id.homeFragment).isEnabled = false





        NavigationUI.setupWithNavController(
            binding?.bottomNav!!,
            Navigation.findNavController(this, R.id.nav_host_fragment)
        )
    }

    fun biometric() {

        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(
                        this@MainActivity,
                        "Authentication error: $errString", Toast.LENGTH_SHORT
                    )
                        .show()
                    binding?.textInfo?.isVisible = true
                    binding?.biometricLoginButton?.isVisible = true
                    binding?.display?.isVisible = false
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    binding?.textInfo?.isVisible = false
                    binding?.biometricLoginButton?.isVisible = false
                    binding?.display?.isVisible = true
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    binding?.textInfo?.isVisible = true
                    binding?.biometricLoginButton?.isVisible = true
                    binding?.display?.isVisible = false
                    Toast.makeText(
                        this@MainActivity, "Authentication failed",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Вход в приложение")
            .setSubtitle("Вход в приложение по биометрическим данным")
            .setNegativeButtonText("Cancel")
            .build()

        // Prompt appears when user clicks "Log in".
        // Consider integrating with the keystore to unlock cryptographic operations,
        // if needed by your app.
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}