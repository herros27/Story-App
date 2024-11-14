package com.example.storyapp.view.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.pref.UserModel
import com.example.storyapp.data.remote.response.StoryResponse
import com.example.storyapp.data.repository.ApiRepository
import com.example.storyapp.data.repository.UserRepository
import com.example.storyapp.utils.ResultStories
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository, private val apiRepository: ApiRepository) : ViewModel() {

    private val _userEmail = MutableLiveData<String>()
    val userEmail: LiveData<String> get() = _userEmail

    private val _stories = MutableLiveData<ResultStories<StoryResponse>>()
    val stories: LiveData<ResultStories<StoryResponse>> get() = _stories

    
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }


    fun getStories() {
        viewModelScope.launch(Dispatchers.IO) {
            _stories.postValue( ResultStories.Loading )
            val result = apiRepository.getStories()
            _stories.postValue(result)
        }
    }

    init {
        
        repository.getSession().asLiveData().observeForever { user ->
            _userEmail.postValue(user.name)
        }

    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}
