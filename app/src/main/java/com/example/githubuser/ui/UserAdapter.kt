package com.example.githubuser.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuser.data.response.Users
import com.example.githubuser.databinding.ItemUserBinding
import com.example.githubuser.ui.fragment.FollowFragment

class UserAdapter : ListAdapter<Users, UserAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)

        holder.itemView.setOnClickListener {
            val username = user.login

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
        fun bind(user: Users) {
            binding.tvUsername.text = user.login
            Glide.with(binding.root)
                .load(user.avatarUrl)
                .into(binding.ivUser)
            binding.tvID.text = user.id.toString()
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Users>() {
            override fun areItemsTheSame(oldItem: Users, newItem: Users): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Users, newItem: Users): Boolean {
                return oldItem == newItem
            }

        }
    }

}


