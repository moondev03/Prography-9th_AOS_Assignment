package com.weave.project.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.weave.project.model.PhotoEntity

class DiffUtilCallback(
    private val oldList: List<PhotoEntity>,
    private val newList: List<PhotoEntity>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}