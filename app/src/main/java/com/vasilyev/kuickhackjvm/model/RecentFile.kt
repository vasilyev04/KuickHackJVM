package com.vasilyev.kuickhackjvm.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recent_files")
data class RecentFile(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val fileName: String,
    var filePreview: String,
    val uploadDate: String
)