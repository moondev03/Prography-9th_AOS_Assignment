package com.weave.project.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.weave.project.databinding.ItemCardPhotoBinding
import com.weave.project.model.PhotoEntity
import com.weave.project.view.DetailDialog

class CardStackAdapter(private val activity: FragmentActivity, private val items: MutableList<PhotoEntity>): RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardStackAdapter.ViewHolder {
        return ViewHolder(ItemCardPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: CardStackAdapter.ViewHolder, position: Int) {
        holder.bindItems(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(val binding: ItemCardPhotoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(item : PhotoEntity) {
            Glide.with(binding.ivPhoto)
                .load(item.urls.regular)
                .transform(CenterCrop(), RoundedCorners(15*4))
                .into(binding.ivPhoto)

            binding.ibBookmark.setOnClickListener {
                itemClickListener.onClick(it, adapterPosition, item)
            }

            binding.ibInfo.setOnClickListener {
                DetailDialog.getInstance(item.id, null).show(activity.supportFragmentManager, "")
            }
        }
    }

    fun changeList(newItem: List<PhotoEntity>){
        val diffUtilCallback = DiffUtilCallback(this.items, newItem)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback)

        this.items.apply {
            clear()
            addAll(newItem)
            diffResult.dispatchUpdatesTo(this@CardStackAdapter)
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