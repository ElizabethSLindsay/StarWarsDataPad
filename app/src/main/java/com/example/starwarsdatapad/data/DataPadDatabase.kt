package com.example.starwarsdatapad.data

import android.content.Context
import androidx.constraintlayout.widget.Placeholder
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter


@Database(entities = [User::class], version = 1, exportSchema = false)
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
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}