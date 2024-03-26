package com.example.githubuser.ui.follow.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.example.githubuser.data.response.FollowUserResponseItem
import com.example.githubuser.databinding.FragmentFollowBinding
import com.example.githubuser.ui.follow.FollowersAdapter
import com.example.githubuser.ui.follow.FollowersViewModel
import com.example.githubuser.ui.follow.FollowingAdapter
import com.example.githubuser.ui.follow.FollowingViewModel

class FollowFragment : Fragment() {

    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val username = arguments?.getString(EXTRA_USERNAME)
        val position = arguments?.getInt(ARG_POSITION)

        val layoutManager = GridLayoutManager(requireActivity(), 2)
        binding.rvFollowUser.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvFollowUser.addItemDecoration(itemDecoration)

        val followersViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.NewInstanceFactory()
        )[FollowersViewModel::class.java]
        val followingViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.NewInstanceFactory()
        )[FollowingViewModel::class.java]

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val ARG_POSITION = "position"
    }

}