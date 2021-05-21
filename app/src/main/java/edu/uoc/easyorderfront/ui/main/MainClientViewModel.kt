package edu.uoc.easyorderfront.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.uoc.easyorderfront.data.authentication.AuthenticationRepository
import kotlinx.coroutines.launch

class MainClientViewModel(
        private val authenticationRepository: AuthenticationRepository
): ViewModel() {
    fun signOut() {
        viewModelScope.launch {
            authenticationRepository.signOut()
        }
    }
}