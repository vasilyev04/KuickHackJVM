package com.vasilyev.kuickhackjvm.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vasilyev.kuickhackjvm.model.CheckingResult


@Dao
interface RecentFilesDao {
    @Query("SELECT * FROM checking_results")
    fun getResults(): LiveData<List<CheckingResult>>

    @Query("SELECT * FROM checking_results WHERE id=:id")
    fun getResult(id: Int): CheckingResult

    @Insert
    fun addCheckingResult(checkingResult: CheckingResult): Long

    @Query("DELETE FROM checking_results WHERE id=:id")
    fun deleteCheckingResult(id: Int)
}