package com.vasilyev.kuickhackjvm.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vasilyev.kuickhackjvm.model.CheckingResult
import com.vasilyev.kuickhackjvm.model.RecentFile


@Dao
interface RecentFilesDao {
    @Query("SELECT * FROM recent_files")
    fun getRecentFiles(): LiveData<List<RecentFile>>

    @Insert
    fun addRecentFile(recentFile: RecentFile)

    @Query("DELETE FROM recent_files WHERE id=:id")
    fun deleteRecentFile(id: Int)

    @Query("SELECT * FROM checking_results")
    fun getResults(): List<CheckingResult>

    @Insert
    fun addCheckingResult(checkingResult: CheckingResult)

    @Query("DELETE FROM checking_results WHERE id=:id")
    fun deleteCheckingResult(id: Int)
}