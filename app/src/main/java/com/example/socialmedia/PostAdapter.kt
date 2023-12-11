package com.example.socialmedia

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.socialmedia.databinding.PostViewBinding

class PostAdapter (var postList: List<PostWithUser>) :RecyclerView.Adapter<PostAdapter.postViewHolder> (){

    class  postViewHolder(val binding: PostViewBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): postViewHolder {
        return postViewHolder(PostViewBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: postViewHolder, position: Int) {
        val post= postList[position]

        holder.binding.postTitle.text= post.post.postContent
        holder.binding.myPost.load(post.post.postImageLink)

        // profile
        holder.binding.profileImage.load(post.users.profileImage)
        holder.binding.userName.text = post.users.name

    }
}