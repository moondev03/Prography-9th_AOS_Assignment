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

    // 사진의 상세 정보를 호출하고각
    // 결과를 databinding을 사용해 view에 값이 보이도록 함
    fun setPhotoData(id: String){
        viewModelScope.launch(Dispatchers.IO){
            when (val res = repository.getPhotoInfo(id)) {
                is Result.Success -> {
                    launch(Dispatchers.Main){
                        _photoData.value = res.data
                        _url.value = res.data.url
                        _title.value = res.data.title ?: "No Title"
                        _desc.value = res.data.desc ?: "No Description"
                        setTags(res.data.tags)
                    }
                }
                is Result.Error -> {
                    Log.e("getPhotoInfo", res.message.toString())
                }
                is Result.Exception -> {
                    Log.e("getPhotoInfo", res.e.message.toString())
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

    // 태그는 리스트 타입이기 때문에 하나의 문자열로 붙여 저장
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

    private val _title = MutableLiveData<String>("")
    val title: LiveData<String>
        get() = _title

    private val _desc = MutableLiveData<String>("")
    val desc: LiveData<String>
        get() = _desc
}