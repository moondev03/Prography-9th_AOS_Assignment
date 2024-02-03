package com.weave.project.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.weave.project.R
import com.weave.project.databinding.ItemPhotoBinding
import com.weave.project.model.PhotoEntity

class HouseRvAdapter: RecyclerView.Adapter<HouseRvAdapter.RecyclerViewViewHolder>() {

    private var photoList = mutableListOf<PhotoEntity>()

//    fun rangeInsert(start: Int, count: Int, newList: List<PhotoEntity>) {
//        photoList = newList
//        notifyItemRangeInserted(start, count)
//    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewViewHolder {
        return RecyclerViewViewHolder(ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerViewViewHolder, position: Int) {
        holder.bind(photoList[position])
    }

    override fun getItemCount(): Int {
        return photoList.size
    }

    inner class RecyclerViewViewHolder(private val binding: ItemPhotoBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PhotoEntity) {
            Log.i("POS", adapterPosition.toString())

            binding.ivItem.foreground = AppCompatResources.getDrawable(itemView.context, R.drawable.gradient)

            Glide.with(binding.ivItem)
                .load(item.urls?.small)
                .placeholder(R.drawable.loading_photo_item)
                .transform(CenterInside(), RoundedCorners(40))
                .into(binding.ivItem)

            binding.tvItem.text = item.description
        }

    }

    fun changeList(newItem: List<PhotoEntity>){
        val diffUtilCallback = DiffUtilCallback(this.photoList, newItem)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback)

        this.photoList.apply {
            clear()
            addAll(newItem)
            diffResult.dispatchUpdatesTo(this@HouseRvAdapter)
        }
    }

    interface OnItemClickListener{
        fun onClick(v: View, pos: Int, data: PhotoEntity)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener){
        this.itemClickListener = onItemClickListener
    }

    private lateinit var itemClickListener: OnItemClickListener
}