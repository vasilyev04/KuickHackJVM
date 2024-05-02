package com.vasilyev.kuickhackjvm.ui.home


sealed class MainSharedState {
    data object Loading: MainSharedState()
}