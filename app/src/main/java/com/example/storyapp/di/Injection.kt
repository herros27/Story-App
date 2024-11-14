package com.example.storyapp.di

import android.content.Context
import com.example.storyapp.data.pref.UserPreference
import com.example.storyapp.data.pref.dataStore
import com.example.storyapp.data.remote.service.ApiConfig
import com.example.storyapp.data.repository.ApiRepository
import com.example.storyapp.data.repository.UserRepository

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }

    fun provideApiRepository(context: Context): ApiRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig(pref,context).apiService
        return ApiRepository(apiService)
    }
}