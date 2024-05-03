package com.vasilyev.kuickhackjvm.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vasilyev.kuickhackjvm.ui.checking.CheckingViewModel
import com.vasilyev.kuickhackjvm.ui.documents.DocumentsViewModel
import com.vasilyev.kuickhackjvm.ui.main.MainSharedViewModel
import com.vasilyev.kuickhackjvm.ui.result.ActivityResultViewModel

class ViewModelFactory(
    private val application: Application
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(CheckingViewModel::class.java)){
            return CheckingViewModel(application) as T
        }else if(modelClass.isAssignableFrom(MainSharedViewModel::class.java)){
            return MainSharedViewModel(application) as T
        }else if(modelClass.isAssignableFrom(ActivityResultViewModel::class.java)){
            return ActivityResultViewModel(application) as T
        }else if(modelClass.isAssignableFrom(DocumentsViewModel::class.java)){
            return DocumentsViewModel(application) as T
        }

        throw RuntimeException("Unknown ViewModel")
    }
}