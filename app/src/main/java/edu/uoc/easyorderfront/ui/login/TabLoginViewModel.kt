package edu.uoc.easyorderfront.ui.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GetTokenResult
import edu.uoc.easyorderfront.data.authentication.AuthenticationRepository
import edu.uoc.easyorderfront.ui.utils.DataWrapper
import kotlinx.coroutines.launch
import java.lang.Exception

class TabLoginViewModel (private val repository: AuthenticationRepository) : ViewModel() {
    lateinit var isLogged  : MutableLiveData<DataWrapper<FirebaseUser?>>
    lateinit var getTokenResult: MutableLiveData<DataWrapper<GetTokenResult>>

    private val TAG = "TabLoginViewModel"

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                isLogged = repository.login(email, password)

            } catch (e : Exception) {
                Log.e(TAG, e.toString())
                isLogged = repository.login(email, password)
            }
        }
    }

    fun getTokenId() {
        viewModelScope.launch {
            try {
                getTokenResult = repository.getIdToken()
            } catch (e : Exception) {
                Log.e(TAG, e.toString())
                getTokenResult = repository.getIdToken()
            }
        }
    }
}