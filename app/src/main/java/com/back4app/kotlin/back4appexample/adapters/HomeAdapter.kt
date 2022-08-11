package com.back4app.kotlin.back4appexample.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.back4app.kotlin.back4appexample.R
import com.back4app.kotlin.back4appexample.databinding.PostItemBinding
import com.back4app.kotlin.back4appexample.entities.Post
import com.squareup.picasso.Picasso

interface RecycleOnClickListener {
    fun onItemClick(position: Int)
}

class HomeAdapter : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    var postList: List<Post> = ArrayList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false)
        val binding = PostItemBinding.bind(view)
        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(post = postList[position])
    }

    override fun getItemCount(): Int = postList.size

    inner class HomeViewHolder(
        private val binding: PostItemBinding,
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) {
            binding.apply {
                locationTv.text = post.location
                descriptionTv.text = post.description
                titleTv.text = post.title
                Picasso.get().load(post.image?.url).into(imagePost)
            }
            itemView.setOnClickListener {}
        }
    }
}