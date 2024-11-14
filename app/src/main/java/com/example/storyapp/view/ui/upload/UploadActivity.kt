package com.example.storyapp.view.ui.upload

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivityUploadBinding
import com.example.storyapp.utils.ResultStories
import com.example.storyapp.utils.reduceFileImage
import com.example.storyapp.utils.uriToFile
import com.example.storyapp.view.ui.ViewModelFactory
import com.example.storyapp.view.ui.main.MainActivity

class UploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding
    private val viewModel by viewModels<UploadViewModel>{
        ViewModelFactory.getInstance(this)
    }

    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("UploadActivity", "onCreate called uri : ${intent.getStringExtra(EXTRA_IMAGE_URI)}")
        currentImageUri = Uri.parse((intent.getStringExtra(EXTRA_IMAGE_URI)))

        binding.ivContent.setImageURI(currentImageUri)

        setupAction()
    }

    private fun uploadImage() {
        currentImageUri?.let {
            showLoading(true)
            val imageFile = uriToFile(it, this).reduceFileImage()
            val description = binding.edAddDescription.text.toString()

            if (description.isEmpty()) {
                showToast(getString(R.string.description))
            }else{
                viewModel.uploadStory(description, imageFile).observe(this) { response ->
                    if (response != null) {
                        when(response) {
                            is ResultStories.Loading -> {
                                showLoading(true)
                            }
                            is ResultStories.Success -> {
                                showLoading(false)
                                showToast(getString(R.string.succes_upload))
                                val intent = Intent(this, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                                finish()                         }
                            is ResultStories.Error -> {
                                showLoading(false)
                                showToast(response.error)
                            }
                        }
                    }
                }
            }

        } ?: showToast(getString(R.string.nothing_image_selected))
    }

    private fun setupAction(){
        binding.btnUpStory.setOnClickListener {
            uploadImage()
            binding.btnUpStory.isEnabled = false
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    companion object {
        private const val EXTRA_IMAGE_URI = "imageUri"

                fun createIntent(context: Context, imageUri: Uri): Intent {
            return Intent(context, UploadActivity::class.java).apply {
                putExtra(EXTRA_IMAGE_URI, imageUri.toString())
            }
        }
    }

}