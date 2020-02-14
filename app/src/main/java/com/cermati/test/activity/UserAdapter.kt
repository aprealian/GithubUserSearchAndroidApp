package com.cermati.test.activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cermati.test.R
import com.cermati.test.model.User


class UserAdapter(users:List<User>):RecyclerView.Adapter<UserAdapter.MViewHolder>(){

    private var users:ArrayList<User> = ArrayList()

    init {
        this.users = ArrayList(users)
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return MViewHolder(view)
    }

    override fun onBindViewHolder(vh: MViewHolder, position: Int) {
        val user= users[position]

        //render
        vh.textViewName.text= user.login
        Glide.with(vh.imageView.context).load(user.avatarUrl).into(vh.imageView)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    fun update(users:List<User>){
        this.users = ArrayList(users)
        notifyDataSetChanged()
    }

    fun addAll(users:List<User>){
        this.users.addAll(users)
        notifyDataSetChanged()
    }

    fun clear(){
        this.users = ArrayList()
        notifyDataSetChanged()
    }

    class MViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        val textViewName: TextView = view.findViewById(R.id.textUserName)
        val imageView: ImageView = view.findViewById(R.id.imageUser)
    }
}