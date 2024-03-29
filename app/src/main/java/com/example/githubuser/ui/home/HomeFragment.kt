package com.example.githubuser.ui.home

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
import com.example.githubuser.databinding.FragmentHomeBinding
import com.example.githubuser.ui.UserAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory: HomeViewModelFactory = HomeViewModelFactory.getInstance()
        val homeViewModel: HomeViewModel by viewModels {
            factory
        }

        val usersAdapter = UserAdapter()

        if (homeViewModel.users.value == null) {
            homeViewModel.findUsers("Airi")
        }

        homeViewModel.users.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val usersData = result.data
                        usersAdapter.submitList(usersData)
                    }

                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        homeViewModel.findUsers("Airi")
                        Toast.makeText(
                            context,
                            "Terjadi kesalahan menemukan user " + result.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        binding.rvUserHome.apply {
            layoutManager = GridLayoutManager(requireActivity(), 2)
            adapter = usersAdapter
            addItemDecoration(
                DividerItemDecoration(
                    requireActivity(),
                    GridLayoutManager(requireActivity(), 2).orientation
                )
            )
        }

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { _, _, _ ->
                searchBar.setText(searchView.text)
                searchView.hide()
                val username = searchBar.text.toString()
                if (username.isEmpty()) {
                    Toast.makeText(
                        context,
                        "Maaf, Username tidak boleh kosong :(",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    homeViewModel.findUsers(username)
                }
                false
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}