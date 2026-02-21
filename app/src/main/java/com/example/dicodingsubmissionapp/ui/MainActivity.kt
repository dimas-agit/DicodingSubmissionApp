package com.example.dicodingsubmissionapp.ui

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingsubmissionapp.R
import com.example.dicodingsubmissionapp.databinding.ActivityMainBinding
import com.example.dicodingsubmissionapp.ui.adapter.ListEventAdapter
import com.example.dicodingsubmissionapp.ui.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[MainViewModel::class.java]

        mainViewModel.listActiveEvent.observe(this){ listEvent ->
            val adapter = ListEventAdapter()
            adapter.submitList(listEvent)
            binding.rvListEvent.adapter = adapter

        }

        mainViewModel.isLoading.observe(this){
            showLoading(it)
        }
        val layoutManager = LinearLayoutManager(this)
        binding.rvListEvent.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvListEvent.addItemDecoration(itemDecoration)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }



     private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE

    }
}
