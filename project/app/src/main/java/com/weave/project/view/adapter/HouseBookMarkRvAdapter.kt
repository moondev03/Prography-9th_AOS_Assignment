package com.weave.project.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.weave.project.R
import com.weave.project.databinding.ItemPhotoBookmarkBinding
import com.weave.project.model.BookMarkEntity

class HouseBookMarkRvAdapter(private val data: ArrayList<BookMarkEntity>): RecyclerView.Adapter<HouseBookMarkRvAdapter.RecyclerViewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewViewHolder {
        return RecyclerViewViewHolder(ItemPhotoBookmarkBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerViewViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class RecyclerViewViewHolder(private val binding: ItemPhotoBookmarkBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BookMarkEntity) {
            Glide.with(binding.ivItem)
                .load(item.url)
                .placeholder(R.drawable.loading_photo_item)
                .transform(CenterInside(), RoundedCorners(40))
                .into(binding.ivItem)
        }

    }
}