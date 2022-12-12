package com.example.starwarsdatapad.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface DataPadDao {


    @Query("""
        SELECT *
        FROM user
    """)
    fun getAll(): List<User>

}