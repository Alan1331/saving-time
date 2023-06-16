package com.example.savingtime.ui.news

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.savingtime.model.News
import com.example.savingtime.network.ApiStatus
import com.example.savingtime.network.NewsApi
import com.example.savingtime.network.UpdateWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class NewsViewModel : ViewModel() {

    private val data = MutableLiveData<List<News>>()
    private val status = MutableLiveData<ApiStatus>()
    private var application: Application? = null

    init {
        retrieveData()
    }

    fun setContext(application: Application?) {
        this.application = application
    }

    private fun retrieveData() {
        viewModelScope.launch (Dispatchers.IO) {
            status.postValue(ApiStatus.LOADING)
            try {
                val response = NewsApi.service.getNews()
                data.postValue(response.data)
                status.postValue(ApiStatus.SUCCESS)
                application?.let { scheduleUpdater(it) }
            } catch (e: Exception) {
                Log.d("NewsViewModel", "Failure: ${e.message}")
                status.postValue(ApiStatus.FAILED)
            }
        }
    }

    fun getData(): LiveData<List<News>> = data

    fun getStatus(): LiveData<ApiStatus> = status

    fun scheduleUpdater(app: Application) {
        val request = OneTimeWorkRequestBuilder<UpdateWorker>()
            .setInitialDelay(1, TimeUnit.MINUTES)
            .build()
        WorkManager.getInstance(app).enqueueUniqueWork(
            UpdateWorker.WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            request
        )
    }
}