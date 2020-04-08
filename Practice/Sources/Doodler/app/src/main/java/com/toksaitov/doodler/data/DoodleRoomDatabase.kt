package com.toksaitov.doodler.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Doodle::class), version = 1, exportSchema = false)
public abstract class DoodleRoomDatabase : RoomDatabase() {

    abstract fun doodleDao(): DoodleDao

    companion object {
        @Volatile
        private var INSTANCE: DoodleRoomDatabase? = null

        fun getDatabase(context: Context): DoodleRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DoodleRoomDatabase::class.java,
                    "doodle_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}