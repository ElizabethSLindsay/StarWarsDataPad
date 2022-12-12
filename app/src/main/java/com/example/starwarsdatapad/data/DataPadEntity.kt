package com.example.starwarsdatapad.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User (
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "credits")
    val credits: Int,
    @ColumnInfo(name = "score")
    val score: Int,
    @ColumnInfo(name = "ship")
    val ship: String,
    @ColumnInfo(name = "cargo_spaces")
    val cargoSpaces: Int,
    @ColumnInfo(name = "upgrades")
    val upgrades: String
)

data class QuestName (
    val id: Int, // This is the Quest Line ID
    val name: String
)
