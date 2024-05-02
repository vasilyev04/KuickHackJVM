package com.vasilyev.kuickhackjvm.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "checking_results")
data class CheckingResult(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val documentName: String,
    val documentPreview: String,
    val checkStatus: CheckStatus,
    val uploadDate: String
)