package com.example.jetpackdemo.paged

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.jetpackdemo.R
import com.example.jetpackdemo.room.User

class UserViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
) {

    private val nameView = itemView.findViewById<TextView>(R.id.name)
    var user: User? = null

    fun bindTo(user: User?) {
        this.user = user
        nameView.text = user?.name
    }
}

class UserAdapter : PagedListAdapter<User, UserViewHolder>(diffCallback) {

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    override fun onCreateViewHolder(p: ViewGroup, type: Int): UserViewHolder = UserViewHolder(p)

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<User>() {
            /**
             * 是否为同一个Item
             */
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean =
                oldItem.id == newItem.id

            /**
             * 数据内容是否发生变化
             */
            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean =
                oldItem == newItem
        }
    }
}