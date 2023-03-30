package com.example.starwarsdatapad.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [User::class,
    Quests::class,
    QuestLines::class,
    Decision::class,
    Trade::class,
    Upgrades::class,
    Inventory::class,
    Ships::class,
    DataStrings::class
    ], version = 6, exportSchema = false)
abstract class DataPadDatabase: RoomDatabase() {

    abstract fun DataPadDao(): DataPadDao

    companion object {

        @Volatile
        private var INSTANCE: DataPadDatabase? = null

        fun getDatabase(context: Context): DataPadDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DataPadDatabase::class.java,
                    "data_pad_database"
                )
                    //.fallbackToDestructiveMigration()
                    .createFromAsset("database/starwars.db")
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}