package com.example.githubuser.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuser.data.local.entity.FavoriteUser
import com.example.githubuser.databinding.ItemUserBinding
import com.example.githubuser.ui.activities.detail.DetailActivity


class FavoriteUserAdapter(private val listUser : List<FavoriteUser>) : RecyclerView.Adapter<FavoriteUserAdapter.UserViewHolder>() {

    inner class UserViewHolder(var binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val (username, avatarUrl ) = listUser[position]
        holder.binding.tvUser.text = username
        Glide.with(holder.itemView.context)
            .load(avatarUrl)
            .into(holder.binding.ivUser)

        holder.itemView.setOnClickListener{
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_USERNAME, username)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = listUser.size


}