package com.example.githubuser.ui.detail.follow

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuser.data.remote.response.FollowUserResponseItem
import com.example.githubuser.databinding.ItemUserBinding
import com.example.githubuser.ui.detail.DetailUserActivity
import com.example.githubuser.ui.detail.follow.fragment.FollowFragment

class FollowersAdapter :
    ListAdapter<FollowUserResponseItem, FollowersAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val followers = getItem(position)
        holder.bind(followers)

        holder.itemView.setOnClickListener {
            val username = followers.login
            val intentUsername =
                Intent(holder.itemView.context, DetailUserActivity::class.java).apply {
                    putExtra(DetailUserActivity.EXTRA_USERNAME, username)
                    putExtra(FollowFragment.EXTRA_USERNAME, username)
                }
            holder.itemView.context.startActivity(intentUsername)
        }
    }

    class MyViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(followers: FollowUserResponseItem) {
            binding.tvUsername.text = followers.login
            Glide.with(binding.root)
                .load(followers.avatarUrl)
                .into(binding.ivUser)
            binding.tvID.text = followers.id.toString()
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<FollowUserResponseItem> =
            object : DiffUtil.ItemCallback<FollowUserResponseItem>() {
                override fun areItemsTheSame(
                    oldItem: FollowUserResponseItem,
                    newItem: FollowUserResponseItem
                ): Boolean {
                    return oldItem == newItem
                }

                @SuppressLint("DIffUtilEquals")
                override fun areContentsTheSame(
                    oldItem: FollowUserResponseItem,
                    newItem: FollowUserResponseItem
                ): Boolean {
                    return oldItem == newItem
                }

            }
    }
}