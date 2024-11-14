package com.example.storyapp.view.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.remote.response.ListStoryItem
import com.example.storyapp.data.repository.ApiRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class DetailViewModel(private val storyRepository: ApiRepository) : ViewModel() {
    private val _storyDetail = MutableLiveData<ListStoryItem>()
    val storyDetail: LiveData<ListStoryItem> get() = _storyDetail

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun getDetailStory(storyID: String) {
        viewModelScope.launch {
            try {
                
                val story = storyRepository.getDetail(storyID)
                _storyDetail.postValue(story)
            } catch (e: Exception) {
                _errorMessage.postValue("Failed to load story: ${e.message}")
            }
        }
    }
}