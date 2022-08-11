package com.back4app.kotlin.back4appexample.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.back4app.kotlin.back4appexample.activities.api.RetrofitBuilder
import com.back4app.kotlin.back4appexample.adapters.HomeAdapter
import com.back4app.kotlin.back4appexample.adapters.RecycleOnClickListener
import com.back4app.kotlin.back4appexample.databinding.FragmentHomeBinding
import com.back4app.kotlin.back4appexample.entities.Post
import com.back4app.kotlin.back4appexample.entities.PostResponse
import com.back4app.kotlin.back4appexample.utils.toImage
import com.back4app.kotlin.back4appexample.utils.toast
import com.parse.ParseObject
import com.parse.ParseQuery
import retrofit2.Call
import retrofit2.Response

class HomeFragment : Fragment(), RecycleOnClickListener {

    private val binding: FragmentHomeBinding by lazy {
        FragmentHomeBinding.inflate(layoutInflater)
    }
    private var adapter: HomeAdapter? = null
    private var postList: ArrayList<Post>? = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        getPosts()
        toast("Resumed!")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            postList?.clear()
            postRV.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            adapter = HomeAdapter()

            getPosts()

            swipeLayout.setOnRefreshListener {
                getPosts()
                swipeLayout.isRefreshing = false
            }
        }
    }

    private fun getPosts() {
        binding.apply {
            val query = ParseQuery.getQuery<ParseObject>("Post")
            query.findInBackground { objects, e ->
                if (e == null) {
                    postList?.clear()
                    for (postObject in objects) {
                        val post = Post()
                        post.description = postObject.getString("description")
                        post.title = postObject.getString("title")
                        post.location = postObject.getString("location")
                        post.image = postObject.getParseFile("image").toImage()
                        postList?.add(post)
                    }
                    adapter?.postList = postList!!
                    postRV.adapter = adapter
                    adapter?.notifyDataSetChanged()
                } else {
                    toast("Post downloads is failed!")
                }
            }
        }
    }

    override fun onItemClick(position: Int) {}

    private fun getRetrofitPosts() {
        val response = RetrofitBuilder.api.getAllPosts()
        response.enqueue(object : retrofit2.Callback<PostResponse> {
            override fun onResponse(
                call: Call<PostResponse>, response: Response<PostResponse>,
            ) {
                if (response.isSuccessful) {
                    postList = response.body()?.results!! as ArrayList<Post>
                    adapter!!.postList = postList!!
                    binding.postRV.adapter = adapter
                    adapter?.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
        binding.swipeLayout.isRefreshing = false
    }
}