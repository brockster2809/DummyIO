package com.example.dummyapp.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dummyapp.databinding.ItemUserBinding
import com.example.dummyapp.db.User
import com.example.libdummyapi.models.Data

class UserAdapter(private val interaction : (User) -> Unit) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private var userList : ArrayList<User> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.UserViewHolder {
        return UserViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: UserAdapter.UserViewHolder, position: Int) {
        val userName = position.toString() + " "  + userList[position].firstName + " " + userList[position].lastName
        Glide.with(holder.userImage).load(userList[position].picture).into(holder.userImage)
        holder.userName.text = userName
        holder.userId.text = userList[position].id
        holder.itemView.setOnClickListener {
            interaction.invoke(userList[position])
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun setUserList(data : List<User>) {
        userList.clear()
        userList.addAll(data)
    }

    class UserViewHolder(binding : ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {

        val userName : TextView = binding.userName
        val userId : TextView = binding.userId
        val userImage : ImageView = binding.userImage

        companion object {
            fun create(parent : ViewGroup) : UserViewHolder {
                val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return UserViewHolder(binding)
            }
        }
    }

}