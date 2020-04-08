package com.toksaitov.doodler.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DoodleDao {
    @Query("SELECT * from doodle_table")
    fun getDoodles(): LiveData<List<Doodle>>

    @Insert()
    suspend fun insert(doodle: Doodle)

    @Delete()
    suspend fun delete(doodle: Doodle)
}
