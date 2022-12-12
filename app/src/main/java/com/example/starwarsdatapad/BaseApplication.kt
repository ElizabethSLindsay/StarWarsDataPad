package com.example.starwarsdatapad

import android.app.Application
import com.example.starwarsdatapad.data.DataPadDatabase

class BaseApplication : Application() {
    val database: DataPadDatabase by lazy { DataPadDatabase.getDatabase(this) }
}