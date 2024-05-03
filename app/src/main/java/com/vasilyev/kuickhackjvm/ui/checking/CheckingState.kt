package com.vasilyev.kuickhackjvm.ui.checking

sealed class CheckingState {
    data object Loading: CheckingState()
    class CheckingCompleted(val id: Int): CheckingState()
}