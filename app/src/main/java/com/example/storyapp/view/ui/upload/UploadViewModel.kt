package com.example.storyapp.view.ui.upload

import androidx.lifecycle.ViewModel
import com.example.storyapp.data.repository.ApiRepository
import java.io.File

class UploadViewModel(private val apiRepository: ApiRepository) : ViewModel() {
    fun uploadStory(description: String, photoFile: File)  = apiRepository.postStory(description, photoFile )
}