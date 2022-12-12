package com.example.starwarsdatapad.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.starwarsdatapad.data.DataPadDao


class DataPadViewModel(
    private val dataPadDao: DataPadDao
) : ViewModel() {

    fun tryDatabase() {
        val myDb = dataPadDao.getAll()
        Log.d("Try Database", myDb.toString())
    }
    /*var userData: LiveData<UserData> = dataPadDao.getUser(1)*/

}

class DataPadViewModelFactory(private val dataPadDao: DataPadDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DataPadViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DataPadViewModel(dataPadDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}