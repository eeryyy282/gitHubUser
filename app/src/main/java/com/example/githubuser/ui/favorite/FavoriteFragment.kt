package com.example.githubuser.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.example.githubuser.data.remote.response.Users
import com.example.githubuser.databinding.FragmentFavoriteBinding
import com.example.githubuser.ui.UserAdapter

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory: FavoriteViewModelFactory =
            FavoriteViewModelFactory.getInstance(requireActivity())
        val favoriteViewModel: FavoriteViewModel by viewModels {
            factory
        }

        val userAdapter = UserAdapter()

        favoriteViewModel.getFavoriteUser().observe(viewLifecycleOwner) { favoriteUsers ->
            binding.progressBar.visibility = View.GONE
            val items = arrayListOf<Users>()
            favoriteUsers.map {
                val item =
                    Users(login = it.username, avatarUrl = it.avatarUrl.toString(), id = it.id!!)
                items.add(item)
            }
            userAdapter.submitList(items)

            if (items.isEmpty()) {
                binding.noFavorite.visibility = View.VISIBLE
                binding.noFavorite.text = "Tidak ada user favorite"
            } else {
                binding.noFavorite.visibility = View.GONE
            }
        }

        binding.rvUserFavorite.apply {
            layoutManager = GridLayoutManager(requireActivity(), 2)
            adapter = userAdapter
            addItemDecoration(
                DividerItemDecoration(
                    requireActivity(),
                    GridLayoutManager(requireActivity(), 2).orientation
                )
            )
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}