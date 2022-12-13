package com.example.starwarsdatapad.viewmodel

import android.util.Log
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
    var upgradeString: LiveData<String> = dataPadDao.getDataString(1)

    fun getUpgrades(): List<Upgrades> {
        return dataPadDao.getUpgrades()
    }

    fun getShip(): Ships {
        return dataPadDao.getShip()
    }

    fun buyUpgrade(id: Int) {
        val count = dataPadDao.upgradePresent(id)
        val activeUser: UserData? = userData.value
        if (count !=0) {
            val upgrade:Upgrades = dataPadDao.findUpgrade(id)
            if (!upgrade.owned &&
                (activeUser != null) &&
                (activeUser.credits >= upgrade.cost)
            ) {
                if (id !in listOf(771,817,994)) {
                    val credits: Int = activeUser.credits - upgrade.cost
                    val score: Int = (activeUser.score + (upgrade.cost * 1.25)).toInt()
                    dataPadDao.updateUser(credits, score)
                    dataPadDao.buyUpgrade(id)
                    dataPadDao.updateDataString(1, " ")
                } else {
                    val credits: Int = activeUser.credits - upgrade.cost
                    val score: Int = (activeUser.score + (upgrade.cost * 1.5)).toInt()
                    dataPadDao.updateUser(credits, score)
                    dataPadDao.buyUpgrade(id)
                    dataPadDao.updateDataString(1, " ")
                    dataPadDao.updateShip(id)
                    dataPadDao.resetUpgrades()
                }
            } else if (
                (activeUser != null) &&
                (activeUser.credits < upgrade.cost)) {
                dataPadDao.updateDataString(1, "You cannot afford this upgrade.")
            } else if (upgrade.owned) {
                dataPadDao.updateDataString(1, "You already own this upgrade")
            } else {
                dataPadDao.updateDataString(1, "Please enter a valid upgrade ID.")
            }
        }
    }

    fun searchUpgrade(id:Int) {
        // TRY: IF USER SHOULD NOT BE ABLE TO BUY
        //      CREATE NEW LIVE DATA BOOLEAN AND OBSERVE IT
        //      IN THE OBSERVE CODE, IF BOOLEAN IS FALSE, DEACTIVATE BUTTON
        //      IF BOOLEAN IS TRUE, ACTIVATE THE BUTTON
        //      DEFAULT BOOLEAN TO FALSE WHEN FRAGMENT LOADS

        val count = dataPadDao.upgradePresent(id)
        if (count !=0) {
            val upgrade:Upgrades = dataPadDao.findUpgrade(id)
            if (!upgrade.owned) {
                if (id !in listOf(771,817,994)) {
                    dataPadDao.updateDataString(1, upgrade.label)
                } else {
                    dataPadDao.updateDataString(
                        1, upgrade.label + " WARNING: Buying a new ship will reset all other ship ugrades!")
                }
            } else {
                dataPadDao.updateDataString(1, "You already own this upgrade")
            }
            upgradeString.value
        } else {
            dataPadDao.updateDataString(1, "Please enter a valid upgrade ID.")
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