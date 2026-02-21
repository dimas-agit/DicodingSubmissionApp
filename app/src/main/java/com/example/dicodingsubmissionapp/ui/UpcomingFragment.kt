package com.example.dicodingsubmissionapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingsubmissionapp.databinding.FragmentUpcomingBinding
import com.example.dicodingsubmissionapp.ui.adapter.ListEventAdapter
import com.example.dicodingsubmissionapp.ui.viewmodel.MainViewModel

class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainViewModel = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory())[MainViewModel::class.java]

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvUpcomingEvents.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvUpcomingEvents.addItemDecoration(itemDecoration)

        mainViewModel.listActiveEvent.observe(viewLifecycleOwner) { listEvent ->
            val adapter = ListEventAdapter()
            adapter.submitList(listEvent)
            binding.rvUpcomingEvents.adapter = adapter
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