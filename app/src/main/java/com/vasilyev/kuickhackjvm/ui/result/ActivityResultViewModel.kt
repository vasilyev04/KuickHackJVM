package com.vasilyev.kuickhackjvm.ui.result

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vasilyev.kuickhackjvm.local.RoomInstance
import com.vasilyev.kuickhackjvm.model.CheckingResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActivityResultViewModel(
    application: Application
): ViewModel() {
    private val _result = MutableLiveData<CheckingResult>()
    val result: LiveData<CheckingResult> get() = _result

    private val database = RoomInstance.getInstance(application)

    fun getResult(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            val result = database.recentFilesDao().getResult(id)
            _result.postValue(result)
        }
    }
}