package com.starcarrlane.coroutines.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.starcarrlane.coroutines.network.Post
import coroutines.R

/**
 * Sample adapter, shows the posts and notifies when an item is clicked
 * Created by macastiblancot on 2/13/17.
 */
class PostsAdapter(var posts : List<Post> = ArrayList<Post>(),
                   val listener: PostClickListener) : RecyclerView.Adapter<PostViewHolder>(){

    fun setElements(elements : List<Post>){
        posts = elements
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        holder.title.text = post.title
        holder.body.text = post.body
        holder.view.setOnClickListener { listener.onPostClicked(post) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false)
        return PostViewHolder(view, view.findViewById(R.id.title) as TextView,
                view.findViewById(R.id.body) as TextView)
    }

    override fun getItemCount(): Int {
        return posts.size
    }


}

interface PostClickListener{
    fun onPostClicked(post : Post)
}

class PostViewHolder(val view: View, val title : TextView, val body: TextView) : RecyclerView.ViewHolder(view)
