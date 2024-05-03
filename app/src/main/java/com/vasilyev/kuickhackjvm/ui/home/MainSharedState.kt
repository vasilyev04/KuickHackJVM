package com.vasilyev.kuickhackjvm.ui.home

import com.vasilyev.kuickhackjvm.model.Document
import org.w3c.dom.DocumentType

sealed class MainSharedState {
    data object Loading: MainSharedState()
}