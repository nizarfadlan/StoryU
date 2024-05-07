package com.nizarfadlan.storyu.presentation.ui.main.listStory.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nizarfadlan.storyu.databinding.ItemStoryBinding
import com.nizarfadlan.storyu.domain.model.Story
import com.nizarfadlan.storyu.utils.helpers.getTimeAgo
import com.nizarfadlan.storyu.utils.helpers.setImageUrl

class StoryListAdapter(
    private val onItemClickCallback: (String) -> Unit
) : PagingDataAdapter<Story, StoryListAdapter.ListViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    inner class ListViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: Story) {
            with(binding) {
                tvItemName.text = story.name
                tvItemDate.text = story.createdAt.getTimeAgo(root.context)
                ivItemPhoto.setImageUrl(story.photoUrl)

                root.setOnClickListener {
                    onItemClickCallback(story.id)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(
                oldItem: Story,
                newItem: Story
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: Story,
                newItem: Story
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}