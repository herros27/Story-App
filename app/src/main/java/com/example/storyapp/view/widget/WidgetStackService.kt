package com.example.storyapp.view.widget

import android.content.Intent
import android.widget.RemoteViewsService
import com.example.storyapp.di.Injection

class WidgetStackService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory {
        val apiRepository = Injection.provideApiRepository(this)
        return StoryWidgetFactory(applicationContext,apiRepository)
    }
}