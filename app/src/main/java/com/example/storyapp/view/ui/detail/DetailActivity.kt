package com.example.storyapp.view.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivityDetailBinding
import com.example.storyapp.view.ui.ViewModelFactory
import com.google.android.material.snackbar.Snackbar

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_back)         }

        setupObservers()
        handleIncomingData()
    }


    private fun setupObservers() {
        viewModel.storyDetail.observe(this) { story ->
            showLoading(false)
            Log.d("DetailActivity", "Data story: $story")
            binding.tvDetailName.text = story.name
            binding.tvDetailDescription.text = story.description
            Glide.with(this)
                .load(story.photoUrl)
                .into(binding.ivDetailPhoto)
        }

                viewModel.errorMessage.observe(this){ error ->
            showLoading(false)
            showError(error)
        }
    }

    private fun handleIncomingData() {
        val storyID = intent.getStringExtra(EXTRA_STORY_ID)

        if (storyID != null) {
            showLoading(true)
            viewModel.getDetailStory(storyID)
        } else {
            Toast.makeText(this, "Invalid story ID", Toast.LENGTH_SHORT).show()
            finish()
        }
    }



    private fun showError(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    companion object {
        const val EXTRA_STORY_ID = "EXTRA_STORY_ID"
    }
}
