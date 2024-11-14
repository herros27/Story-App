package com.example.storyapp.view.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.utils.RegisterResult
import com.example.storyapp.data.repository.ApiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterViewModel(private val apiRepository: ApiRepository): ViewModel() {

    private val _registerResult = MutableLiveData<RegisterResult>()
    val registerResult: LiveData<RegisterResult> get() = _registerResult

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = apiRepository.register(name, email, password)
            _registerResult.postValue(result)
        }
    }
}