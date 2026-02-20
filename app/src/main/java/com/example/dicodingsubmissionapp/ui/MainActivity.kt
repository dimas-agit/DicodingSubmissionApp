package com.example.dicodingsubmissionapp.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingsubmissionapp.R
import com.example.dicodingsubmissionapp.data.response.EventResponse
import com.example.dicodingsubmissionapp.data.service.ApiConfig
import com.example.dicodingsubmissionapp.databinding.ActivityMainBinding
import com.example.dicodingsubmissionapp.ui.adapter.ListEventAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object{
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()


        val layoutManager = LinearLayoutManager(this)
        binding.rvListEvent.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvListEvent.addItemDecoration(itemDecoration)


        getEvents()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun getEvents(){
        showLoading(true)
        val client = ApiConfig.getApiService().getEvents(1)
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(
                call: Call<EventResponse>,
                response: Response<EventResponse>
            ) {
                showLoading(false)
               if(response.isSuccessful){
                   val responseBody = response.body()
                   if(responseBody != null){
                       val listEvents = responseBody.listEvents
                       val adapter = ListEventAdapter()
                       adapter.submitList(listEvents)
                       binding.rvListEvent.adapter = adapter

                   }
                   else{
                       Log.e(TAG, "onFailure: ${response.message()}")
                   }
               }
            }

            override fun onFailure(
                call: Call<EventResponse?>?,
                t: Throwable?
            ) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t?.message}")
            }
        })
    }


     private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE

    }
}
