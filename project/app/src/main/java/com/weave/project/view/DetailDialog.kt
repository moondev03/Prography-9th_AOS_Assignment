package com.weave.project.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.WindowManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.weave.project.R
import com.weave.project.data.local.BookMarkDatabase
import com.weave.project.databinding.DialogDetailBinding
import com.weave.project.model.BookMarkEntity
import com.weave.project.viewmodel.DetailDialogViewModel
import com.weave.project.viewmodel.HouseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DetailDialog(private val id: String, private val vm: HouseViewModel?) : DialogFragment() {
    companion object {
        private var instance: DetailDialog? = null

        fun getInstance(id: String, vm: HouseViewModel?): DetailDialog {
            return instance ?: synchronized(this) {
                instance ?: DetailDialog(id, vm).also { instance = it }
            }
        }
    }

    private var _binding: DialogDetailBinding? = null
    private val binding get() = _binding!!

    private var db: BookMarkDatabase? = null
    private val viewModel by viewModels<DetailDialogViewModel>()

    override fun onStart() {
        super.onStart()

        dialog?.window?.setLayout(MATCH_PARENT, MATCH_PARENT)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.dialog_detail, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = viewModel
        setIsBookMark()

//        vm.setSaveBtn(false)

        binding.ibCancel.setOnClickListener {
            dismiss()
        }

        binding.ibBookmark.setOnClickListener {
            clickBookMark()
        }

        viewModel.url.observe(this){
            Glide.with(binding.ivDetailPhoto)
                .load(it)
                .transform(RoundedCorners(15*4))
                .into(binding.ivDetailPhoto)
        }

        viewModel.setPhotoData(id)

        return binding.root
    }

    private fun setIsBookMark(){
        db = BookMarkDatabase.getInstance(requireContext())

        CoroutineScope(Dispatchers.IO).launch {
            val isBookMark = db!!.bookMarkDao().getBookMark(id)

            launch(Dispatchers.Main) {
                if(isBookMark != 0){
                    viewModel.setBookMark(true)
                } else {
                    viewModel.setBookMark(false)
                }
            }
        }
    }

    private fun clickBookMark(){
        CoroutineScope(Dispatchers.Main).launch {
            val temp: Deferred<Boolean> = async(Dispatchers.IO){
                val isBookMarked = db!!.bookMarkDao().getBookMark(id)
                if(isBookMarked != 0){
                    db!!.bookMarkDao().deleteBookMark(id)
                    false
                } else {
                    db!!.bookMarkDao().saveBookMark(BookMarkEntity(id, viewModel.photoData.value!!.url))
                    true
                }
            }

            val result = temp.await()
            if(result){
                Toast.makeText(requireContext(), "북마크 추가", Toast.LENGTH_SHORT).show()
                vm?.setAddRefresh(BookMarkEntity(
                    id = viewModel.photoData.value!!.id,
                    url = viewModel.photoData.value!!.url
                ))
            } else {
                Toast.makeText(requireContext(), "북마크 삭제", Toast.LENGTH_SHORT).show()
                vm?.setRemoveRefresh(viewModel.photoData.value!!.id)
            }

            viewModel.setBookMark(result)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        instance = null
        Log.i("dialog", "destroy")
    }
}