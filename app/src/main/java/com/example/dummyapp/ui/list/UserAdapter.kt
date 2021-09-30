package com.example.dummyapp.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dummyapp.R
import com.example.libdummyapi.models.Data

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private var userList : ArrayList<Data> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.UserViewHolder {
        return UserViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: UserAdapter.UserViewHolder, position: Int) {
        val userName = userList[position].firstName + " " + userList[position].lastName
        holder.userName.text = userName
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun setUserList(data : List<Data>) {
        userList.clear()
        userList.addAll(data)
    }

    class UserViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        val userName : TextView = itemView.findViewById(R.id.user_name)

        companion object {
            fun create(parent : ViewGroup) : UserViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
                return UserViewHolder(view)
            }
        }
    }

}