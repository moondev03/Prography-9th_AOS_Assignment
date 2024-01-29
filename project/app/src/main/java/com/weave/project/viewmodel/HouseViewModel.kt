package com.weave.project.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weave.project.data.remote.Result
import com.weave.project.model.PhotoEntity
import com.weave.project.repository.UnSplashRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HouseViewModel : ViewModel() {
    private val repository = UnSplashRepositoryImpl()

    val bookMarkItems = MutableLiveData<List<PhotoEntity>>()
    val photoItems = MutableLiveData<List<PhotoEntity>>()

    private var page = 1

    fun init() {
        getBookMarkPhotos()
        getPhotos()
    }

    fun getBookMarkPhotos() {
        val dummyBookMarkList = listOf<PhotoEntity>()
        bookMarkItems.value = dummyBookMarkList
    }

    fun getPhotos() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val res = repository.getPhotos(page, 10)) {
                is Result.Success -> {
                    val photoList = res.data
                    val currentList = photoItems.value ?: emptyList()
                    photoItems.postValue(currentList + photoList)
                    page++
                }
                is Result.Error -> {
                    // 에러 처리
                }
                is Result.Exception -> {
                    // 예외 처리
                }
            }
        }
    }
}
