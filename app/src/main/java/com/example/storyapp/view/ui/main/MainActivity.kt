package com.example.storyapp.view.ui.main

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.utils.ResultStories
import com.example.storyapp.view.adapter.StoryAdapter
import com.example.storyapp.view.ui.ViewModelFactory
import com.example.storyapp.view.ui.cameraGalery.PhotoActivity
import com.example.storyapp.view.ui.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var adapter: StoryAdapter
    private lateinit var rvStory: RecyclerView
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }

        binding.btnSettings.setOnClickListener {
            val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(intent)
        }

        binding.btnUpStory.setOnClickListener {
            val intent = Intent(this, PhotoActivity::class.java)
            startActivity(intent)
        }

        rvStory = binding.recyclerView
        adapter = StoryAdapter()

        setupView()
        playAnimation()

    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        setupRecyclerView()
        observeUserEmail()
        observeStories()
        setSupportActionBar(binding.toolbar)
        supportActionBar?.show()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getStories()
    }

    private fun observeUserEmail() {
        viewModel.userEmail.observe(this) { name ->
            
            binding.nameTextView.text = name
        }
    }

    private fun setupRecyclerView() {

        rvStory.layoutManager = LinearLayoutManager(this@MainActivity)
        adapter = StoryAdapter()
        rvStory.adapter = adapter
    }

    private fun observeStories() {
        
        viewModel.stories.observe(this) { result ->
            when (result) {
                is ResultStories.Loading -> {
                    
                    showLoading(true)
                }
                is ResultStories.Success -> {
                    
                    result.data?.listStory?.let { stories ->
                        showLoading(false)
                        showNoInternet(false)
                        showEmptyStories(false)
                        adapter.submitList(stories)
                    }?:{
                        showEmptyStories(true)
                    }
                }
                is ResultStories.Error -> {
                    if (isNetworkError(result.error)){
                        showNoInternet(true)
                        return@observe
                    }else{
                        showToast(result.error)
                    }
                }
            }
        }

        viewModel.getStories()
    }

    private fun isNetworkError(error: String): Boolean {
        return error.contains(getString(R.string.unable_to_resolve_host), ignoreCase = true) ||
                error.contains(getString(R.string.network_error), ignoreCase = true)
    }

    private fun showNoInternet(isNoInternet: Boolean) {
        binding.noInternet.visibility = if (isNoInternet) View.VISIBLE else View.GONE
    }

    private fun showEmptyStories(isEmpty: Boolean) {
        binding.tvEmpty.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun playAnimation() {
        
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -200f, 200f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        
        val randomRotationAnimator = ObjectAnimator.ofFloat(binding.imageView, View.ROTATION, 0f, 360f).apply {
            duration = 3000 
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }

        
        val name = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(100)
        val message = ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(name, message)
            playTogether(randomRotationAnimator)
            startDelay = 100
        }.start()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                viewModel.logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}