package com.nauk0a.dynasty.mainActivityPackage

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nauk0a.dynasty.R
import com.nauk0a.dynasty.databinding.ActivityMainBinding
import com.nauk0a.dynasty.internet_connection.NetworkStatusTracker
import com.nauk0a.dynasty.setting.SettingViewModel
import com.nauk0a.dynasty.utils.APP_ACTIVITY
import com.nauk0a.dynasty.utils.ToastFun
import com.nauk0a.dynasty.utils.db
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*
import java.util.concurrent.Executor

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var viewModelSetting: SettingViewModel

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
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)


        val networkStatusTracker = NetworkStatusTracker(this)
        val viewModelFactory = VMFactory(networkStatusTracker)
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        viewModelSetting = ViewModelProvider(this)[SettingViewModel::class.java]


        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        executor = ContextCompat.getMainExecutor(this@MainActivity)
        //передаем в константы текущий контекст
        APP_ACTIVITY = this

        prefs = getSharedPreferences("settings", Context.MODE_PRIVATE)

        db = Firebase.firestore


        viewModel.state.observe(this) { state ->
            when (state) {
                MyState.Fetched -> {
                    binding?.noInternetConnection?.visibility = View.GONE
                    binding?.display?.visibility = View.VISIBLE
                }
                MyState.Error -> {
                    binding?.noInternetConnection?.visibility = View.VISIBLE
                    binding?.display?.visibility = View.GONE
                }
            }
        }


        biometric()

        if (BiometricManager.from(this).canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS) {

            binding?.biometricLoginButton?.setOnClickListener {
                biometricPrompt.authenticate(promptInfo)
            }

            val biometricLoginCheck = prefs.getBoolean("APP_PREFERENCES_COUNTER", true)
            when (biometricLoginCheck) {
                true -> biometricPrompt.authenticate(promptInfo)
            }
        }
        viewModelSetting.sincFirebaseOnlain.observe(this, androidx.lifecycle.Observer {
            when (it) {
                true -> db.enableNetwork()
                false -> db.disableNetwork()
            }
        })
        val firebaseOnlainSinc = prefs.getBoolean("APP_PREFERENCES_SINC", false)
        when (firebaseOnlainSinc) {
            true -> db.enableNetwork()
            false -> db.disableNetwork()
        }


        //Отключение пункта меню в BottomNavigation
//        bottomNav.menu.findItem(R.id.homeFragment).isEnabled = false


        NavigationUI.setupWithNavController(
            binding?.bottomNav!!,
            Navigation.findNavController(this, R.id.nav_host_fragment)
        )
        binding!!.checkInternetConnections.setOnClickListener {
            val panelIntent = Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY)
            startActivity(panelIntent)
        }
    }

    fun biometric() {

        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    ToastFun("Authentication error: $errString")
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
                    ToastFun("Authentication failed")
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