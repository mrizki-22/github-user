package com.example.githubuser.ui.activities.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.data.response.UserItems
import com.example.githubuser.databinding.FragmentFollowBinding
import com.example.githubuser.ui.activities.main.UserAdapter


class FollowFragment : Fragment() {
    private var position: Int? = null
    private var username: String? = null

    private lateinit var binding: FragmentFollowBinding

    companion object {
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME)
        }

        val followViewModelFactory = FollowViewModelFactory(username.toString())

        val followViewModel = ViewModelProvider(
            requireActivity(),
            followViewModelFactory
        )[FollowViewModel::class.java]

        if (position == 1) {
            if (followViewModel.followers.value == null) {
                followViewModel.getFollowers(username.toString())
            }
            followViewModel.followers.observe(viewLifecycleOwner) { users ->
                handleFollowData(users)
            }
        } else {
            if (followViewModel.following.value == null) {
                followViewModel.getFollowing(username.toString())
            }
            followViewModel.following.observe(viewLifecycleOwner) { users ->
                handleFollowData(users)
            }
        }

        followViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

    }

    private fun handleFollowData(users: List<UserItems>?) {
        if (users != null) {
            if (users.isNotEmpty()) {
                setFollowData(users)
            } else {
                setFollowData(emptyList())
                Log.e("FollowFragment", "onCreate: users is null")
            }
        }
    }

    private fun setFollowData(users: List<UserItems>) {
        val adapter = UserAdapter(users)
        binding.rvFollowers.layoutManager = LinearLayoutManager(activity)
        binding.rvFollowers.adapter = adapter
    }

    private fun showLoading(it: Boolean?) {
        if (it == true) {
            binding.progressBarFollow.visibility = View.VISIBLE
        } else {
            binding.progressBarFollow.visibility = View.GONE
        }
    }



}