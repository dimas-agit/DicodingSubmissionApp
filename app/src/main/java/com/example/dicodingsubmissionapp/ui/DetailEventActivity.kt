package com.example.dicodingsubmissionapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.dicodingsubmissionapp.data.response.Event
import com.example.dicodingsubmissionapp.databinding.ActivityDetailEventBinding
import com.example.dicodingsubmissionapp.ui.viewmodel.DetailViewModel
import androidx.core.net.toUri

class DetailEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailEventBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        val viewModel = ViewModelProvider(this)[DetailViewModel::class.java]

        val eventId = intent.getIntExtra(EXTRA_EVENT_ID, -1)
        if (eventId != -1) {
            viewModel.getEventDetail(eventId)
        }

        viewModel.event.observe(this) { event ->
            if (event != null) {
                populateDetail(event)
            }
        }

        viewModel.isLoading.observe(this) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }

        // Handle back press
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.webDescription.canGoBack()) {
                    binding.webDescription.goBack()
                } else {
                    finish()
                }
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun populateDetail(event: Event) {
        binding.tvEventName.text = event.name
        binding.tvOwnerName.text = event.ownerName
        
        binding.tvEventTime.text = "${event.beginTime} - ${event.endTime}"
        val sisaKuota = (event.quota ?: 0) - (event.registrants ?: 0)
        binding.tvQuota.text = "Sisa Kuota: $sisaKuota"
        val cleanHtml = event.description
            ?.replaceFirst(Regex("<img[^>]*>"), "")
            ?: ""

        binding.webDescription.loadDataWithBaseURL(
            null,
            cleanHtml,
            "text/html",
            "UTF-8",
            null
        )


        Glide.with(this)
            .load(event.mediaCover)
            .into(binding.ivEventLogo)

        binding.btnOpenLink.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, event.link!!.toUri())
            startActivity(intent)
        }

        // Update toolbar title when data is loaded (optional, or keep it empty as requested)
        // supportActionBar?.title = event.name
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_EVENT_ID = "extra_event_id"
    }
}