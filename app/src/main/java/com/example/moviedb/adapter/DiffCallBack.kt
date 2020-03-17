package com.example.moviedb.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.moviedb.data.Movie

class DiffCallBack: DiffUtil.ItemCallback<Movie>() {

    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }
}