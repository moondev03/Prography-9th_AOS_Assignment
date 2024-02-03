package com.weave.project.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weave.project.data.remote.Result
import com.weave.project.model.PhotoEntity
import com.weave.project.repository.UnSplashRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CardViewModel : ViewModel() {
    private val repository = UnSplashRepositoryImpl()
    private var _photoItems = MutableLiveData<MutableList<PhotoEntity>>(mutableListOf())
    val photoItems: LiveData<MutableList<PhotoEntity>>
        get() = _photoItems


    // 랜덤 이미지를 size만큼 로드
    fun getRandomPhoto(size: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val res = repository.getRandomPhotos(size)) {
                is Result.Success -> {
                    val photoList = res.data
                    val currentList = _photoItems.value ?: emptyList()
                    _photoItems.postValue((currentList + photoList).toMutableList())
                    Log.e("VM", photoItems.value?.size.toString())
                }
                is Result.Error -> {
                    Log.e("getRandomPhotos", res.message.toString())
                }
                is Result.Exception -> {
                    Log.e("getRandomPhotos", res.e.message.toString())
                }
            }
        }
    }
}
