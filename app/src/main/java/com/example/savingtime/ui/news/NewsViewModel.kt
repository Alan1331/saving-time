package com.example.savingtime.ui.news

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.savingtime.model.EkonomiApiResponse
import com.example.savingtime.model.News
import com.example.savingtime.network.ApiStatus
import com.example.savingtime.network.NewsApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewsViewModel : ViewModel() {

    private val data = MutableLiveData<List<News>>()
    private val status = MutableLiveData<ApiStatus>()

    init {
        retrieveData()
    }

    private fun retrieveData() {
        viewModelScope.launch (Dispatchers.IO) {
            status.postValue(ApiStatus.LOADING)
            try {
                val response = NewsApi.service.getNews()
                data.postValue(response.data)
                status.postValue(ApiStatus.SUCCESS)
            } catch (e: Exception) {
                Log.d("NewsViewModel", "Failure: ${e.message}")
                status.postValue(ApiStatus.FAILED)
            }
        }
    }

    fun getData(): LiveData<List<News>> = data

    fun getStatus(): LiveData<ApiStatus> = status
}