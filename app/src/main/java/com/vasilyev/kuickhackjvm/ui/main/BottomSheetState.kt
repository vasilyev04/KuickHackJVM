package com.vasilyev.kuickhackjvm.ui.main

sealed class BottomSheetState {
    data object Expanded: BottomSheetState()
    data object Hidden: BottomSheetState()
}