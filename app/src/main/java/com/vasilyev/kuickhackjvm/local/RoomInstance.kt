package com.vasilyev.kuickhackjvm.local

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vasilyev.kuickhackjvm.model.CheckingResult
import com.vasilyev.kuickhackjvm.model.RecentFile

@Database(entities = [RecentFile::class, CheckingResult::class], version = 1, exportSchema = false)
abstract class RoomInstance: RoomDatabase() {

    abstract fun recentFilesDao(): RecentFilesDao

    companion object{
        private var INSTANCE: RoomInstance? = null
        private val LOCK = Any()
        private const val DB_NAME = "kuick_hack.db"

        fun getInstance(application: Application): RoomInstance {
            INSTANCE?.let { return it }

            synchronized(LOCK) {
                INSTANCE?.let { return it }

                val db = Room.databaseBuilder(
                    application,
                    RoomInstance::class.java,
                    DB_NAME
                ).build()

                INSTANCE = db
                return db
            }
        }
    }
}