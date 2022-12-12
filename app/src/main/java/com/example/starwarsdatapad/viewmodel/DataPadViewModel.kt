package com.example.starwarsdatapad.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.starwarsdatapad.data.DataPadDao
import com.example.starwarsdatapad.data.Ships
import com.example.starwarsdatapad.data.Upgrades
import com.example.starwarsdatapad.data.UserData


class DataPadViewModel(
    private val dataPadDao: DataPadDao
) : ViewModel() {
    var userData: LiveData<UserData> = dataPadDao.getUser(1)
    private val _upgradeSearch = MutableLiveData<String>()
    val upgradeSearch: LiveData<String> get() = _upgradeSearch

    init {
        _upgradeSearch.value = "Test"
    }

    fun getUpgrades(): List<Upgrades> {
        return dataPadDao.getUpgrades()
    }

    fun getShip(): Ships {
        return dataPadDao.getShip()
    }

    fun searchUpgrade(id: Int) {
        if (id.toString().length != 3) {
            val count = dataPadDao.upgradePresent(id)
            if (count != 0) {
                val upgrade: Upgrades = dataPadDao.findUpgrade(id)
                _upgradeSearch.value = (
                        upgrade.label + ": " +
                        upgrade.cost.toString())
            } else {
                _upgradeSearch.value = "Please enter a valid upgrade ID."
            }
        } else {
            _upgradeSearch.value = "Please enter a valid upgrade ID."
        }
    }
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