package com.vasilyev.kuickhackjvm.ui.checking

import android.app.Application
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.RoomDatabase
import com.vasilyev.kuickhackjvm.local.RoomInstance
import com.vasilyev.kuickhackjvm.model.CheckStatus
import com.vasilyev.kuickhackjvm.model.CheckingResult
import com.vasilyev.kuickhackjvm.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class CheckingViewModel(
    application: Application
): ViewModel() {
    private val apiService = RetrofitInstance.apiService

    private val database = RoomInstance.getInstance(application)

    private val _checkingState = MutableLiveData<CheckingState>()
    val checkingState: LiveData<CheckingState> get() = _checkingState

    fun checkFile(file: File){
        Log.d("masdmasdm", file.toUri().toString())

        _checkingState.value = CheckingState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            val requestFile = RequestBody.create(MediaType.parse("application/pdf"), file)

            delay(3000)

            val multipartFile = MultipartBody.Part.createFormData("file", file.name, requestFile)
            val response = apiService.checkIDCard(multipartFile)

            val checkingResult = CheckingResult(documentName = file.name,
                documentPreview = "",
                checkStatus = CheckStatus.SUCCESS,
                uploadDate = "Today"
            )

            database.recentFilesDao().addCheckingResult(checkingResult)

            _checkingState.postValue(CheckingState.CheckingCompleted)

            Log.d("masdmasdm", response.body()!!.string())
        }
    }
}