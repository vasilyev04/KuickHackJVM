package com.vasilyev.kuickhackjvm.ui.documents

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class DocumentsViewModel: ViewModel() {
    private val _documentsState = MutableLiveData<DocumentsState>()
    val documentsState: LiveData<DocumentsState> get() = _documentsState

    fun getRecentDocuments(){
        viewModelScope.launch {

        }
    }
}