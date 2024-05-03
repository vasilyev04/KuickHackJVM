package com.vasilyev.kuickhackjvm.ui.main

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vasilyev.kuickhackjvm.local.RoomInstance
import com.vasilyev.kuickhackjvm.model.CheckingResult
import com.vasilyev.kuickhackjvm.model.Document
import com.vasilyev.kuickhackjvm.ui.home.MainSharedState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainSharedViewModel(
    private val application: Application
): ViewModel() {
    private val database = RoomInstance.getInstance(application)

    private val _mainSharedState = MutableLiveData<MainSharedState>()
    val mainSharedState: LiveData<MainSharedState> get() = _mainSharedState

    private val _selectedDocument = MutableLiveData<Document>()
    val selectedDocument: LiveData<Document> get() = _selectedDocument

    var recentResults = database.recentFilesDao().getResults()


    init {
        _mainSharedState.value = MainSharedState.Loading
    }

    fun selectDocument(document: Document){
        _selectedDocument.value = document
    }

    fun getUpdatedList(){
        viewModelScope.launch(Dispatchers.IO) {
            recentResults = database.recentFilesDao().getResults()
        }
    }

    fun deleteCheckingResult(checkingResult: CheckingResult){
        viewModelScope.launch(Dispatchers.IO) {
            database.recentFilesDao().deleteCheckingResult(checkingResult.id)
        }
    }
}