package com.vasilyev.kuickhackjvm.ui.documents

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vasilyev.kuickhackjvm.local.RoomInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DocumentsViewModel(
    application: Application
): ViewModel() {
    private val _documentsState = MutableLiveData<DocumentsState>()
    val documentsState: LiveData<DocumentsState> get() = _documentsState

    private val database = RoomInstance.getInstance(application)

    val resultsList = database.recentFilesDao().getResults()

    fun deleteRecentCheckingResult(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            database.recentFilesDao().deleteCheckingResult(id)
        }
    }
}