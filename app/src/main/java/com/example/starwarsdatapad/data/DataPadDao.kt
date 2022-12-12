package com.example.starwarsdatapad.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query


@Dao
interface DataPadDao {

    @Query("""
        SELECT credits, score
        FROM user
        WHERE id= :id
    """)
    fun getUser(id: Int): LiveData<UserData>

    @Query("""
        SELECT *
        FROM upgrades
        WHERE owned = 1
    """)
    fun getUpgrades(): List<Upgrades>

    @Query("""
        SELECT COUNT(id)
        FROM upgrades
        WHERE id=:id
    """)
    fun upgradePresent(id: Int): Int
    
    @Query("""
        SELECT *
        FROM upgrades
        WHERE id= :id
    """)
    fun findUpgrade(id: Int): Upgrades

    @Query("""
        SELECT *
        FROM ships
        WHERE owned = 1
        LIMIT 1
    """)
    fun getShip():Ships
}