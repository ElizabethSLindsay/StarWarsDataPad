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

    @Query("""
        SELECT text_value
        FROM data_strings
        WHERE id=:id
    """)
    fun getDataString(id: Int): LiveData<String>

    @Query("""
        UPDATE data_strings
        SET text_value = :value
        WHERE id=:id
    """)
    fun updateDataString(id: Int, value: String)

    @Query("""
        UPDATE user
        SET credits = :credits,
            score = :score
        WHERE id=1
    """)
    fun updateUser(credits:Int, score: Int)

    @Query("""
        UPDATE user
        SET ship = :ship
        WHERE id=1
    """)
    fun updateShip(ship:Int)

    @Query("""
        UPDATE upgrades
        SET owned = 0
        WHERE id not in (771,817,994)
    """)
    fun resetUpgrades()

    @Query("""
        UPDATE upgrades
        SET owned = 1
        WHERE id=:id
    """)
    fun buyUpgrade(id:Int)
}