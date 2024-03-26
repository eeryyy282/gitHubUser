package com.example.githubuser.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.example.githubuser.data.response.Users
import com.example.githubuser.databinding.FragmentHomeBinding
import com.example.githubuser.ui.UserAdapter
import com.google.android.material.snackbar.Snackbar

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
        val homeViewModel = ViewModelProvider(
            requireActivity(), ViewModelProvider.NewInstanceFactory()
        )[HomeViewModel::class.java]

        homeViewModel.userResponse.observe(viewLifecycleOwner) { userData ->
            setUserData(userData)
        }

        if (homeViewModel.userResponse.value.isNullOrEmpty()) {
            homeViewModel.findUser("Airi")
        }

        val layoutManager = GridLayoutManager(requireActivity(), 2)
        binding.rvReview.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvReview.addItemDecoration(itemDecoration)

        homeViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        homeViewModel.snackbarText.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { snackBarText ->
                Snackbar.make(
                    requireView(), snackBarText, Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { _, _, _ ->
                searchBar.setText(searchView.text)
                searchView.hide()
                val username = searchBar.text.toString()
                if (username.isEmpty()) {
                    homeViewModel.snackBar("Maaf, Username tidak boleh kosong :(")
                } else {
                    homeViewModel.findUser(username)
                }
                false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setUserData(userData: List<Users>) {
        val adapter = UserAdapter()
        adapter.submitList(userData)
        binding.rvReview.adapter = adapter
    }
}