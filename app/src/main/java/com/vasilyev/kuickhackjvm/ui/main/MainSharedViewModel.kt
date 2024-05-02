package com.vasilyev.kuickhackjvm.ui.main

import android.app.Application
import android.net.Uri
import androidx.core.net.toFile
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vasilyev.kuickhackjvm.local.RoomInstance
import com.vasilyev.kuickhackjvm.model.Document
import com.vasilyev.kuickhackjvm.model.RecentFile
import com.vasilyev.kuickhackjvm.ui.home.MainSharedState
import com.vasilyev.kuickhackjvm.utils.uriToFile
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

    val recentFiles: LiveData<List<RecentFile>> = database.recentFilesDao().getRecentFiles()

    init {
        _mainSharedState.value = MainSharedState.Loading
    }

    fun createRecentFile(uri: Uri, documentName: String) {
        val file = uriToFile(application, uri, documentName)

        val recentFile = RecentFile(
            fileName = file.name,
            filePreview = "",
            uploadDate = "Today"
        )

        viewModelScope.launch(Dispatchers.IO) {
            database.recentFilesDao().addRecentFile(recentFile)
        }
    }

    fun selectDocument(document: Document){
        _selectedDocument.value = document
    }

    fun expandBottomSheet(){

    }

    fun hideBottomSheet(){

    }
}