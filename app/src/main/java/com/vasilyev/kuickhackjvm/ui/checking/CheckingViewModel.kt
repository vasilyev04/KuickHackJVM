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
import com.vasilyev.kuickhackjvm.utils.bitmapToBase64
import com.vasilyev.kuickhackjvm.utils.pdfPageToBitmap
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

    fun checkFile(file: File, documentName: String){
        Log.d("LOG_CHECK_RESPONSE", file.toUri().toString())

        _checkingState.value = CheckingState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            val requestFile = RequestBody.create(MediaType.parse("application/pdf"), file)

            val multipartFile = MultipartBody.Part.createFormData("file", file.name, requestFile)
            //val response = apiService.checkIDCard(file = multipartFile)

            val documentPreviewBitmap = pdfPageToBitmap(file, 0)
            val documentPreview = bitmapToBase64(documentPreviewBitmap)

           // val statusText = response.body()!!.status

//            val status = if(statusText == "OK"){
//                CheckStatus.SUCCESS
//            }else if(statusText == "FAKE"){
//                CheckStatus.ERROR
//            }else{
//                CheckStatus.WARNING
//            }

            delay(6000)

            val checkingResult = CheckingResult(
                documentName = documentName,
                documentPreview = documentPreview,
                checkStatus = CheckStatus.SUCCESS,
                uploadDate = "Сегодня"
            )

            val id = database.recentFilesDao().addCheckingResult(checkingResult)

            _checkingState.postValue(CheckingState.CheckingCompleted(id.toInt()))

//            Log.d("LOG_CHECK_RESPONSE", response.body()!!.status)
        }
    }
}