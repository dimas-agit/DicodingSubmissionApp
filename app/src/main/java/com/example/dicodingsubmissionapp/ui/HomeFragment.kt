package com.example.dicodingsubmissionapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingsubmissionapp.databinding.FragmentHomeBinding
import com.example.dicodingsubmissionapp.ui.adapter.ListEventAdapter
import com.example.dicodingsubmissionapp.ui.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainViewModel = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory())[MainViewModel::class.java]

        // Setup Upcoming Events (Horizontal)
        val upcomingAdapter = ListEventAdapter()
        binding.rvUpcomingEvents.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = upcomingAdapter
        }

        mainViewModel.listActiveEvent.observe(viewLifecycleOwner) { listEvent ->
            upcomingAdapter.submitList(listEvent?.take(5))
        }

        // Setup Finished Events (Vertical)
        val finishedAdapter = ListEventAdapter()
        binding.rvFinishedEvents.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = finishedAdapter
        }

        mainViewModel.listFinishedEvent.observe(viewLifecycleOwner) { listEvent ->
            finishedAdapter.submitList(listEvent?.take(5))
        }

        // Setup Search Results
        val searchAdapter = ListEventAdapter()
        binding.rvSearchResults.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = searchAdapter
        }

        mainViewModel.searchResult.observe(viewLifecycleOwner) { listEvent ->
            searchAdapter.submitList(listEvent)
        }

        // Observe Error Messages
        mainViewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            if (message != null) {
                Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
                    .setAction("Retry") {
                        mainViewModel.getActiveEvents()
                        mainViewModel.getFinishedEvents()
                    }
                    .show()
                mainViewModel.clearErrorMessage()
            }
        }

        // Search logic
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            
            // Live Search when typing
            searchView.editText.addTextChangedListener { editable ->
                val query = editable.toString()
                if (query.isNotEmpty()) {
                    mainViewModel.searchEvents(query)
                }
            }

            searchView.editText.setOnEditorActionListener { textView, _, _ ->
                searchBar.setText(searchView.text)
                searchView.hide()
                mainViewModel.searchEvents(textView.text.toString())
                false
            }
        }

        mainViewModel.isLoading.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}