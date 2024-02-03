package com.weave.project.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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
                .transform(RoundedCorners(40))
                .into(binding.ivItem)

            itemView.setOnClickListener {
                itemClickListener.onClick(item.id)
            }
        }
    }

    fun changeList(newItem: List<BookMarkEntity>){
        val diffUtilCallback = BookMarkDiffUtilCallback(this.data, newItem)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback)

        this.data.apply {
            clear()
            addAll(newItem)
            diffResult.dispatchUpdatesTo(this@HouseBookMarkRvAdapter)
        }
    }

    interface OnItemClickListener{
        fun onClick(id: String)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener){
        this.itemClickListener = onItemClickListener
    }

    private lateinit var itemClickListener: OnItemClickListener
}