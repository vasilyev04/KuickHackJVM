package com.vasilyev.kuickhackjvm.ui.checking

sealed class CheckingState {
    data object Loading: CheckingState()
    data object CheckingCompleted: CheckingState()
}