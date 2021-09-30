package com.example.dummyapp.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dummyapp.databinding.ItemUserBinding
import com.example.libdummyapi.models.Data

class UserAdapter(private val interaction : (Data) -> Unit) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private var userList : ArrayList<Data> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.UserViewHolder {
        return UserViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: UserAdapter.UserViewHolder, position: Int) {
        val userName = userList[position].firstName + " " + userList[position].lastName
        holder.userName.text = userName
        holder.itemView.setOnClickListener {
            interaction.invoke(userList[position])
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun setUserList(data : List<Data>) {
        userList.clear()
        userList.addAll(data)
    }

    class UserViewHolder(binding : ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {

        val userName : TextView = binding.userName

        companion object {
            fun create(parent : ViewGroup) : UserViewHolder {
                val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return UserViewHolder(binding)
            }
        }
    }

}