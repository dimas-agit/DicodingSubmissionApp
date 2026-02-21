package com.example.dicodingsubmissionapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.dicodingsubmissionapp.databinding.FragmentFinishedBinding
import com.example.dicodingsubmissionapp.ui.adapter.ListEventAdapter
import com.example.dicodingsubmissionapp.ui.viewmodel.MainViewModel

class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mainViewModel = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory())[MainViewModel::class.java]

        // Using StaggeredGridLayoutManager for the "Pinterest" look
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.rvFinishedEvents.layoutManager = layoutManager

        mainViewModel.listFinishedEvent.observe(viewLifecycleOwner) { listEvent ->
            val adapter = ListEventAdapter()
            adapter.submitList(listEvent)
            binding.rvFinishedEvents.adapter = adapter
        }

        mainViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}