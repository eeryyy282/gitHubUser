package com.example.githubuser.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.githubuser.R
import com.example.githubuser.databinding.FragmentFollowBinding

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

        if (position == 1) {
            binding.testTextView.text = "Get Follower $username"
        } else {
            binding.testTextView.text = "Get Following $username"
        }


    }
}