package com.udacity.project4.authentication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.udacity.project4.R
import com.udacity.project4.databinding.ActivityAuthenticationBinding
import com.udacity.project4.locationreminders.RemindersActivity

/**
 * This class should be the starting point of the app, It asks the users to sign in / register, and redirects the
 * signed in users to the RemindersActivity.
 */
class AuthenticationActivity : AppCompatActivity() {

    private val viewModel by viewModels<AuthentucationViewModel>()
//    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener { launchSignInFlow() }

        viewModel.authenticationState.observe(this) {  authenticationState ->
            when (authenticationState) {
                AuthentucationViewModel.AuthenticationState.AUTHENTICATED -> {

                    startActivity(Intent(this , RemindersActivity::class.java))

                }
                else -> Log.d(TAG, "Failed to login")
            }
        }

//        val customLayout = AuthMethodPickerLayout.Builder(R.layout.firebase_custom_login_layout)
//            .setGoogleButtonId(R.id.google_login_btn)
//            .setEmailButtonId(R.id.email_login_btn)
//            .build()


    }

    private fun launchSignInFlow() {

        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(), AuthUI.IdpConfig.GoogleBuilder().build()


        )

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            AuthenticationActivity.SIGN_IN_REQUEST_CODE
        )
    }

    companion object {
        var SIGN_IN_REQUEST_CODE = 675
        var TAG = "Auth Activity"
    }
}
