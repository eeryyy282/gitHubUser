package com.example.githubuser.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.example.githubuser.R
import com.example.githubuser.data.response.FollowUserResponseItem
import com.example.githubuser.databinding.FragmentFollowBinding
import com.example.githubuser.ui.FollowersAdapter
import com.example.githubuser.ui.FollowersViewModel
import com.example.githubuser.ui.FollowingAdapter
import com.example.githubuser.ui.FollowingViewModel
import com.google.android.material.snackbar.Snackbar

class FollowFragment : Fragment() {

    private lateinit var binding: FragmentFollowBinding

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val ARG_POSITION = "position"
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_follow, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFollowBinding.bind(view)

        val username = arguments?.getString(EXTRA_USERNAME)
        val position = arguments?.getInt(ARG_POSITION)

        val layoutManager = GridLayoutManager(requireActivity(), 2)
        binding.rvFollowUser.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvFollowUser.addItemDecoration(itemDecoration)

        val followersViewModel = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory())[FollowersViewModel::class.java]
        val followingViewModel = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory())[FollowingViewModel::class.java]

        if (position == 1) {
            if (username != null) {
                if (followersViewModel.followerResponse.value == null) {
                    followersViewModel.getFollowers(username)
                }
            }
            followersViewModel.followerResponse.observe(viewLifecycleOwner) { userFollowers ->
                setFollowersData(userFollowers)
            }
            followersViewModel.isLoading.observe(viewLifecycleOwner) {
                showLoading(it)
            }

        } else {
            if (username != null) {
                if (followingViewModel.followingResponse.value == null) {
                    followingViewModel.getFollowing(username)
                }
            }
            followingViewModel.followingResponse.observe(viewLifecycleOwner) { userFollowing ->
                setFollowingData(userFollowing)
            }
            followingViewModel.isLoading.observe(viewLifecycleOwner) {
                showLoading(it)
            }

        }


    }

    private fun setFollowingData(followData: List<FollowUserResponseItem>) {
        val adapter = FollowingAdapter()
        adapter.submitList(followData)
        binding.rvFollowUser.adapter = adapter
    }

    private fun setFollowersData(followData: List<FollowUserResponseItem>) {
        val adapter = FollowersAdapter()
        adapter.submitList(followData)
        binding.rvFollowUser.adapter = adapter
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}