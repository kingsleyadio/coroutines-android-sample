package com.starcarrlane.coroutines.ui.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.starcarrlane.coroutines.com.starcarrlane.experimental.Android
import com.starcarrlane.coroutines.network.SampleClient
import coroutines.R
import kotlinx.coroutines.experimental.*
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.starcarrlane.coroutines.network.Post
import com.starcarrlane.coroutines.ui.adapter.PostClickListener
import com.starcarrlane.coroutines.ui.adapter.PostsAdapter
import java.io.IOException


/**
 * When resumed, will call the service to get the Posts and display them
 * Created by macastiblancot on 2/13/17.
 */
class MainActivity : AppCompatActivity(), PostClickListener {

    private lateinit var posts: RecyclerView
    private lateinit var postsAdapter: PostsAdapter
    private lateinit var postsLayoutManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        posts = findViewById(R.id.posts_list) as RecyclerView
        postsAdapter = PostsAdapter(listener = this)
        postsLayoutManager = LinearLayoutManager(this)

        posts.apply {
            setHasFixedSize(true)
            layoutManager = postsLayoutManager
            adapter = postsAdapter
        }
    }

    override fun onPostClicked(post: Post) {
        Toast.makeText(this, "Clicked ${post.id}", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()

        launch(Android) {
            try {
                val result = SampleClient.fetchPosts()

                postsAdapter.setElements(result.await()) // will suspend until the call is finished
                postsAdapter.notifyDataSetChanged()
            } catch (exception: IOException){
                Toast.makeText(this@MainActivity, "Phone not connected or service down", Toast.LENGTH_SHORT).show()
            }

        }
    }
}
