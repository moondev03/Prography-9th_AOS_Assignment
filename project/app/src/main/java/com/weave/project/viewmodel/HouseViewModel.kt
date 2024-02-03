package com.weave.project.viewmodel

import androidx.lifecycle.LiveData
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

    private var _photoItems = MutableLiveData<MutableList<PhotoEntity>>(mutableListOf())
    val photoItems: LiveData<MutableList<PhotoEntity>>
        get() = _photoItems


    private var page = 1


    fun getPhotos() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val res = repository.getPhotos(page, 10)) {
                is Result.Success -> {
                    val photoList = res.data
                    _photoItems.postValue((photoItems.value!! + photoList).toMutableList())
                    page++
                }
                is Result.Error -> {
                }
                is Result.Exception -> {
                }
            }
        }
    }
}
