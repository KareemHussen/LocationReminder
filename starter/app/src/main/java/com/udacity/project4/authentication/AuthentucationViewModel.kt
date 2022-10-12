package com.udacity.project4.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.map

class AuthentucationViewModel : ViewModel() {



    val authenticationState = AuthenticationLiveData().map { user ->
        if (user != null) {
            AuthenticationState.AUTHENTICATED
        } else {
            AuthenticationState.UNAUTHENTICATED
        }
    }


    enum class AuthenticationState {
        AUTHENTICATED, UNAUTHENTICATED
    }
}