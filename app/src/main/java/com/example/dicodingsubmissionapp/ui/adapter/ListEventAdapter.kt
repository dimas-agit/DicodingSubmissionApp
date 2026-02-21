package com.example.dicodingsubmissionapp.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.example.dicodingsubmissionapp.data.response.ListEventsItem
import com.example.dicodingsubmissionapp.databinding.ItemEventsBinding
import com.example.dicodingsubmissionapp.ui.DetailEventActivity

class ListEventAdapter : ListAdapter<ListEventsItem, ListEventAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemEventsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class MyViewHolder(val binding: ItemEventsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem) {
            val context = binding.root.context
            val layoutParams = binding.ivEvent.layoutParams

            // Adjust height based on LayoutManager type
            if (itemView.layoutParams is StaggeredGridLayoutManager.LayoutParams) {
                binding.ivEvent.minimumHeight = dpToPx(100, context)
            } else {
                binding.ivEvent.minimumHeight = dpToPx(200, context)
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            }
            binding.ivEvent.layoutParams = layoutParams

            Glide.with(context)
                .load(event.mediaCover)
                .into(binding.ivEvent)
            binding.tvEventTitle.text = event.name

            // Set intent on click
            itemView.setOnClickListener {
                val intent = Intent(context, DetailEventActivity::class.java)
                intent.putExtra(DetailEventActivity.EXTRA_EVENT_ID, event.id)
                context.startActivity(intent)
            }
        }

        private fun dpToPx(dp: Int, context: android.content.Context): Int {
            return (dp * context.resources.displayMetrics.density).toInt()
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}