package com.example.storyapp.view.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.data.repository.ApiRepository
import com.example.storyapp.data.repository.UserRepository
import com.example.storyapp.di.Injection
import com.example.storyapp.view.ui.detail.DetailViewModel
import com.example.storyapp.view.ui.login.LoginViewModel
import com.example.storyapp.view.ui.main.MainViewModel
import com.example.storyapp.view.ui.signup.RegisterViewModel
import com.example.storyapp.view.ui.upload.UploadViewModel

class ViewModelFactory(private val repository: UserRepository, private val apiRepository: ApiRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository, apiRepository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository, apiRepository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(apiRepository) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(apiRepository) as T
            }
            modelClass.isAssignableFrom(UploadViewModel::class.java) -> {
                UploadViewModel(apiRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(Injection.provideRepository(context),Injection.provideApiRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}