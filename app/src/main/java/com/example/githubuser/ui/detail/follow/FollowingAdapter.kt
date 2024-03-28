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

class FollowingAdapter :
    ListAdapter<FollowUserResponseItem, FollowingAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val following = getItem(position)
        holder.bind(following)

        holder.itemView.setOnClickListener {
            val username = following.login
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
        fun bind(following: FollowUserResponseItem) {
            binding.tvUsername.text = following.login
            Glide.with(binding.root)
                .load(following.avatarUrl)
                .into(binding.ivUser)
            binding.tvID.text = following.id.toString()
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

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(
                    oldItem: FollowUserResponseItem,
                    newItem: FollowUserResponseItem
                ): Boolean {
                    return oldItem == newItem
                }

            }
    }
}