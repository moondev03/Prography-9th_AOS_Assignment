package com.weave.project.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weave.project.data.remote.Result
import com.weave.project.data.remote.dto.Tags
import com.weave.project.model.PhotoDetailEntity
import com.weave.project.repository.UnSplashRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.StringBuilder


class DetailDialogViewModel: ViewModel() {
    private val repository = UnSplashRepositoryImpl()

    private var _photoData = MutableLiveData<PhotoDetailEntity>()
    val photoData: LiveData<PhotoDetailEntity>
        get() = _photoData


    private val _url = MutableLiveData<String>()
    val url: LiveData<String>
        get() = _url

    fun setPhotoData(id: String){
        viewModelScope.launch(Dispatchers.IO){
            when (val res = repository.getPhotoInfo(id)) {
                is Result.Success -> {
                    launch(Dispatchers.Main){
                        _photoData.value = res.data
                        _url.value = res.data.url
                        setTags(res.data.tags)
                    }
                    Log.i("load", "load")
                }
                is Result.Error -> {
                    Log.e("load", res.message.toString())
                }
                is Result.Exception -> {
                }
            }
        }
    }

    private val _isBookMark = MutableLiveData(false)
    val isBookMark: LiveData<Boolean>
        get() = _isBookMark

    fun setBookMark(p: Boolean){
        _isBookMark.value = p
    }

    private val _tags = MutableLiveData<String>("")
    val tags: LiveData<String>
        get() = _tags

    private fun setTags(tags: List<Tags>){
        if(tags.isNotEmpty()){
            val temp = tags.listIterator()
            val result = StringBuilder()
            while(temp.hasNext()){
                result.append("#${temp.next().title} ")
            }


            _tags.value = result.toString()
        }
    }
}