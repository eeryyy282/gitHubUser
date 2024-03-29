package com.example.githubuser.ui.detail.follow.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.example.githubuser.data.Result
import com.example.githubuser.databinding.FragmentFollowBinding
import com.example.githubuser.ui.detail.follow.FollowersAdapter
import com.example.githubuser.ui.detail.follow.FollowersViewModel
import com.example.githubuser.ui.detail.follow.FollowersViewModelFactory
import com.example.githubuser.ui.detail.follow.FollowingAdapter
import com.example.githubuser.ui.detail.follow.FollowingViewModel
import com.example.githubuser.ui.detail.follow.FollowingViewModelFactory

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

        val factoryFollowers: FollowersViewModelFactory =
            FollowersViewModelFactory.getInstance(requireActivity())
        val followersViewModel: FollowersViewModel by viewModels {
            factoryFollowers
        }
        val followersAdapter = FollowersAdapter()

        val factoryFollowing: FollowingViewModelFactory =
            FollowingViewModelFactory.getInstance(requireActivity())
        val followingViewModel: FollowingViewModel by viewModels {
            factoryFollowing
        }
        val followingAdapter = FollowingAdapter()



        if (position == 1) {
            if (username != null) {
                if (followersViewModel.followers.value == null) {
                    followersViewModel.getFollowers(username)
                }
            }

            followersViewModel.followers.observe(viewLifecycleOwner) { result ->

                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.noFollow.visibility = View.GONE
                        val followersData = result.data
                        followersAdapter.submitList(followersData)

                    }

                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            context,
                            result.error,
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.noFollow.visibility = View.VISIBLE
                        binding.noFollow.text = "Tidak ada followers"
                    }
                }
            }

            binding.rvFollowUser.apply {
                adapter = followersAdapter
            }


        } else {
            if (username != null) {
                if (followingViewModel.following.value == null) {
                    followingViewModel.getFollowing(username)
                }
            }

            followingViewModel.following.observe(viewLifecycleOwner) { result ->

                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.noFollow.visibility = View.GONE
                        val followingData = result.data
                        followingAdapter.submitList(followingData)
                    }

                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            context,
                            result.error,
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.noFollow.visibility = View.VISIBLE
                        binding.noFollow.text = "Tidak ada following"
                    }
                }
            }

            binding.rvFollowUser.apply {
                adapter = followingAdapter
            }


        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val ARG_POSITION = "position"
    }

}