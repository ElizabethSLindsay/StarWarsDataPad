package com.example.starwarsdatapad.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.starwarsdatapad.data.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class DataPadViewModel(
    private val dataPadDao: DataPadDao
) : ViewModel() {
    // Upgrades Window
    var userData: LiveData<UserData> = dataPadDao.getUser(1)
    var upgradeString: LiveData<String> = dataPadDao.getDataString(1)
    // Trade Window
    var totalCargoSpace: LiveData<String> = dataPadDao.getCargo() // Buy Upgrade Text
    var usedCargoSpace: LiveData<String> = dataPadDao.getDataString(2) // Cargo Space Used
    var availableCargoSpace: LiveData<String> = dataPadDao.getDataString(3) // Cargo Space Available
    var purchaseItem: LiveData<String> = dataPadDao.getDataString(4) // Buy Item Name
    var inventoryString: LiveData<String> = dataPadDao.getDataString(5) // Inventory display box
    var itemTotal: LiveData<String> = dataPadDao.getDataString(6) // Count of currently searched inventory item
    var sellItem: LiveData<String> = dataPadDao.getDataString(7) // Label of item to be sold
    var sellValue: LiveData<String> = dataPadDao.getDataString(8) // Value of a single item of the searched type
    var questText: LiveData<String> = dataPadDao.getDataString(9) // Quest text from database
    var questLines: LiveData<String> = dataPadDao.getDataString(10)

    private val smugglingQuests = listOf(261638,141940,508967,798535,685860,178767,203697,753231,878948,
        730642,741467,235563,595451,439688,249550,369444,594393,989994,639089,718960,152752,306697,
        285692,609080,198813,372620)
    private val holdingPenQuests = listOf(157228,383394,993904,776731,701197,726403,729117)
    private val reinforcedPenQuests = listOf(982736,314177,371283,756778,139305,702415,321136,
        594766,531354,415028,818295)
    private val lockedPlanets = listOf(387613,390580,180479,101769,294780,827579,400251,
        157228,729117,139305,757884,942574,952971,174042,957421,162394,256639,903548)

    private val weirdQuests = listOf(614104,141940,100306,781761,594393, 269107,989994)

    private lateinit var inventory: List<Inventory>
    var stop = false
    var hasItem = false

    fun getUpgrades(): List<Upgrades> {
        return dataPadDao.getUpgrades()
    }

    fun updateCargoSpace(id: Int) {
        if (!stop && totalCargoSpace.value != null &&
                usedCargoSpace.value != null &&
                availableCargoSpace.value != null) {
            stop = true
            var spaceUsed = 0
            inventory = dataPadDao.getInventory()
            for (item: Inventory in inventory) {
                val cargoSize = dataPadDao.getItemData(item.name)
                val used = cargoSize * item.quantity
                spaceUsed += used
            }
            var available = totalCargoSpace.value.toString().toInt() - spaceUsed

            if (hasItem) {
                val count = dataPadDao.itemPresent(id)
                if (count !=0) {
                    val item = dataPadDao.getInventoryItem(id)
                    val maxPurchase: Int = (
                            userData.value?.credits.toString().toInt() / item.value
                            )
                    val bySize = available / dataPadDao.getItemSize(item.name)

                    if (maxPurchase < available || bySize < available) {
                        if (bySize < maxPurchase) {
                            available = bySize
                        } else {
                            available = maxPurchase
                        }
                    }
                }
            }
            if (spaceUsed != usedCargoSpace.value.toString().toInt()) {
                dataPadDao.updateDataString(2,spaceUsed.toString())
            }
            if (available != availableCargoSpace.value.toString().toInt()) {
                dataPadDao.updateDataString(3, available.toString())
            }
            stop = false
        }
    }

    fun getShip(): Ships {
        return dataPadDao.getShip()
    }

    fun buyCargo(id: Int, count: Int) {
        val ctPresent = dataPadDao.itemPresent(id)
        val activeUser: UserData? = userData.value
        val item = dataPadDao.getInventoryItem(id)
        Log.d("item",item.toString())
        val inventoryItem: Inventory = dataPadDao.inventoryItem(item.name)
        Log.d("Test", inventoryItem.toString())

        if (ctPresent != 0 && activeUser != null) {
            val cost = count * item.value
            val credits: Int = (activeUser.credits) - cost
            val score: Int = (activeUser.score + (cost*1.25/5).toInt())
            val newQuantity = inventoryItem.quantity + count
            val newValue = inventoryItem.totalPrice + cost

            dataPadDao.updateUser(credits, score)
            dataPadDao.updateDataString(3, "0")
            dataPadDao.updateDataString(4, " ")
            dataPadDao.updateInventory(item.name,newQuantity,newValue)
            updateInventory()
        }
    }

    fun buyUpgrade(id: Int) {
        val count = dataPadDao.upgradePresent(id)
        val activeUser: UserData? = userData.value
        val ship = dataPadDao.getShip()
        if (count !=0) {
            val upgrade:Upgrades = dataPadDao.findUpgrade(id)
            if (!upgrade.owned &&
                (activeUser != null) &&
                (activeUser.credits >= upgrade.cost) &&
                (id in listOf(771,817,994) ||
                id == 919 && ship.smuggle ||
                id == 200 && ship.shields ||
                id == 111 && ship.laserBattery ||
                id == 705 && ship.navComputer ||
                id == 979 && ship.ionCanon ||
                id == 402 && ship.targetComp ||
                id == 685 && ship.holdingPen ||
                id == 719 && ship.reinforcedHolding
                )
            ) {
                val credits: Int = activeUser.credits - upgrade.cost
                val score: Int = (activeUser.score + (upgrade.cost * 1.25)).toInt()
                dataPadDao.updateUser(credits, score)
                dataPadDao.updateDataString(1, " ")
                if (id !in listOf(771,817,994)) {
                    dataPadDao.buyUpgrade(id)
                } else {
                    dataPadDao.updateShip(id)
                    dataPadDao.resetUpgrades()
                    dataPadDao.buyUpgrade(id)
                }
            } else if (
                (activeUser != null) &&
                (activeUser.credits < upgrade.cost)) {
                dataPadDao.updateDataString(1, "You cannot afford this upgrade.")
            } else if (upgrade.owned) {
                dataPadDao.updateDataString(1, "You already own this upgrade")
            } else if (
                id == 919 && !ship.smuggle ||
                id == 200 && !ship.shields ||
                id == 111 && !ship.laserBattery ||
                id == 705 && !ship.navComputer ||
                id == 979 && !ship.ionCanon ||
                id == 402 && !ship.targetComp ||
                id == 685 && !ship.holdingPen ||
                id == 719 && !ship.reinforcedHolding
            ) {
                dataPadDao.updateDataString(1, "Your current ship cannot use this upgrade")
            } else {
                dataPadDao.updateDataString(1, "Please enter a valid upgrade ID.")
            }
        } else {
            dataPadDao.updateDataString(1, "Please enter a valid upgrade ID.")
        }
    }

    private fun completeQuest(id:Int) {
        val activeUser: UserData? = userData.value
        if (activeUser != null) {
            val quest = dataPadDao.getQuest(id)
            if (!quest.complete) {
                dataPadDao.completeQuest(quest.id)
                dataPadDao.updateLine(quest.questLineId, quest.id)
                val credits = activeUser.credits + quest.creditReward
                val score = activeUser.score + quest.scoreReward
                dataPadDao.updateUser(credits, score)
                Log.d("Test", quest.displayTextVal)
                dataPadDao.updateDataString(9, quest.displayTextVal)
            } else {
                dataPadDao.updateDataString(9, "You have already completed this stage:\n" + quest.displayTextVal)
            }
            val questLines = dataPadDao.getActiveQuests()
            Log.d("Quest Lines",questLines.toString())
            var printString = ""
            for (questLine in questLines) {
                val activeQuest = dataPadDao.getQuest(questLine.activeStage)
                printString += (questLine.name + " - " + activeQuest.label + ": (" +
                        activeQuest.id.toString() + ") " + activeQuest.reminderText + "\n\n")
            }
            dataPadDao.updateDataString(10,printString)

            Log.d("Top String",dataPadDao.getDataString(9).toString())
            Log.d("Bottom String",dataPadDao.getDataString(10).toString())
        }
    }

    private fun resetUser() {
        dataPadDao.resetQuests()
        dataPadDao.resetQuestLines()
        dataPadDao.starterUpgrades()
        dataPadDao.starterShip()
        dataPadDao.resetUser()
        dataPadDao.resetInventory()
        dataPadDao.resetShips()
        dataPadDao.addStarter()
        dataPadDao.updateDataString(1,"")
        dataPadDao.updateDataString(2,"0")
        dataPadDao.updateDataString(3,"10")
        dataPadDao.updateDataString(4,"")
        dataPadDao.updateDataString(5,"")
        dataPadDao.updateDataString(6,"")
        dataPadDao.updateDataString(7,"")
        dataPadDao.updateDataString(8,"")
        dataPadDao.updateDataString(9,"")
        dataPadDao.updateDataString(10,"")
    }

    fun searchInventory(id:Int) {
        val count = dataPadDao.itemPresent(id)
        if (count !=0) {
            hasItem = true
            val item = dataPadDao.getInventoryItem(id)
            dataPadDao.updateDataString(4,item.name)
            var maxPurchase: Int = 0
            Log.d("Test",dataPadDao.getCargoSize().toString())
            var availableSpace:Int = dataPadDao.getCargoSize()
            val inventory = dataPadDao.getInventory()
            for (newItem in inventory) {
                val space = newItem.quantity * dataPadDao.getItemSize(newItem.name)
                availableSpace -= space
            }
            if (item.value != 0) {
                maxPurchase = (
                        userData.value?.credits.toString().toInt() / item.value
                        )
            }
            val bySize = availableSpace / dataPadDao.getItemSize(item.name)

            if (bySize < availableSpace || maxPurchase < availableSpace) {
                if (bySize < maxPurchase) {
                    dataPadDao.updateDataString(3, bySize.toString())
                } else {
                    dataPadDao.updateDataString(3, maxPurchase.toString())
                }
            } else {
                dataPadDao.updateDataString(3, availableSpace.toString())
            }
        } else {
            hasItem = false
            dataPadDao.updateDataString(3, "0")
            dataPadDao.updateDataString(4, "Please enter a valid inventory item ID")
        }
    }

    fun searchQuest(id: String) {
        // Quest ID Exists
        val count = dataPadDao.questPresent(id.toInt())
        val activeUser: UserData? = userData.value
        if (count != 0 && activeUser != null) {
            val quest = dataPadDao.getQuest(id.toInt())
            val questLine = dataPadDao.getQuestLine(quest.questLineId)
        // Quest is already complete
            if (quest.complete && questLine.activeStage == questLine.finalStage) {
                dataPadDao.updateDataString(9,"You have already completed this quest.")
                return
        // Quest belongs to Quest Line that isn't active and is not first quest in quest line
            } else if (questLine.activeStage == 0 && questLine.firstStage != quest.id) {
                dataPadDao.updateDataString(9,"This quest item is not part of an active quest.")
                return
        // If quest costs credits - check to make sure they have enough credits
            } else if (quest.creditReward < 0 && activeUser.credits + quest.creditReward < 0) {
                dataPadDao.updateDataString(9, "You do not have enough credits to complete the next stage of this quest.")
                return
        // If quest requires smuggling compartment, holding pen or reinforced holding pen, make sure they have one
            } else if (quest.id in smugglingQuests && !dataPadDao.hasSmuggle()) {
                dataPadDao.updateDataString(9, "This quest stage requires a smuggling compartment. Upgrade your ship to continue.")
                return
            } else if (quest.id in holdingPenQuests && !dataPadDao.hasHoldingPen()) {
                dataPadDao.updateDataString(9, "This quest stage requires a holding pen. Upgrade your ship to continue.")
                return
            } else if (quest.id in reinforcedPenQuests && !dataPadDao.hasReinforcedPen()) {
                dataPadDao.updateDataString(9, "This quest stage requires a reinforced holding pen. Upgrade your ship to continue.")
                return
        // If this is a weird quest
            } else {
                if (id.toInt() in weirdQuests) {
                    if (id.toInt() == 614104 && !dataPadDao.reqMet(548189)) {
                        dataPadDao.updateDataString(9, "You do as much digging as you can on Kessel, but are unable to turn up any new leads on the mysterious Sun Crusher. If only you had some friends on the world who might be willing to help you.")
                        return
                    } else if (id.toInt() == 141940 && !dataPadDao.reqMet(508559)) {
                        dataPadDao.updateDataString(9,"You arrive on Borleais but are unable to gain access to the Alderaan Biotics facility. Maybe if you knew someone involved in the production of Ryllca they could help you get in.")
                        return
                    } else if (id.toInt() == 100306 && !dataPadDao.reqMet(372620)) {
                        dataPadDao.updateDataString(9, "You attempt to meet with Admiral Daala, but are turned away at the door. It looks like you need someway to prove you support the empire to meet with her.")
                        return
                    } else if (id.toInt() == 781761 && dataPadDao.getUserShip() == 817) {
                        dataPadDao.updateDataString(9, "We're going to need a bigger ship.")
                        return
                    } else if (id.toInt() == 269107 && (
                        dataPadDao.reqMet(594393) || dataPadDao.reqMet(989994))) {
                            dataPadDao.updateDataString(9, "You have chosen to get a mineral fish. You cannot take this action.")
                            return
                    } else if (id.toInt() in listOf<Int>(594393,989994) && dataPadDao.reqMet(269107)) {
                        dataPadDao.updateDataString(9, "You have already purchased a wurol. You can no longer take this action.")
                        return
                    }
                }
        // Nothing unique applies, check to see if all requirements are met
                if (id.toInt() == 61540) {
                    if (!(dataPadDao.reqMet(269107) || dataPadDao.reqMet(989994))) {
                        return
                    }
                } else if (quest.requires.contains("|")) {
                    val requirements = quest.requires.split("|")
                    for (req in requirements) {
                        Log.d("Requirement",req.toString())
                        if (!dataPadDao.reqMet(req.toInt())) {
                            Log.d("Fail",req.toString())
                            if (req.toInt() in lockedPlanets) {
                                val planet: String = when (req.toInt()) {
                                    387613 -> "Obroa-skai"
                                    390580 -> "Wayland"
                                    180479 -> "Ossus"
                                    101769 -> "Malachor"
                                    294780 -> "Korriban"
                                    740708 -> "Nal Hutta"
                                    400251 -> "Mustafar"
                                    157228 -> "Cantonica"
                                    729117 -> "Dathomir"
                                    139305 -> "Iego"
                                    757884 -> "Myrkr"
                                    942574 -> "Felucia"
                                    952971 -> "Honoghr"
                                    174042 -> "Byss"
                                    957421 -> "Alzoc III"
                                    162394 -> "Kamino"
                                    256639 -> "Kothlis"
                                    903548 -> "Vassek"
                                    else -> "Unknown"
                                }
                                dataPadDao.updateDataString(9, planet + "is locked")
                            } else {
                                dataPadDao.updateDataString(9, "You have not completed all the requirements to see this event.")
                            }
                            return
                        }
                    }
                } else if (quest.requires != "0") {
                    if (!dataPadDao.reqMet(quest.requires.toInt())) {
                        if (quest.requires.toInt() in lockedPlanets) {
                            val planet: String = when (quest.requires.toInt()) {
                                387613 -> "Obroa-skai"
                                390580 -> "Wayland"
                                180479 -> "Ossus"
                                101769 -> "Malachor"
                                294780 -> "Korriban"
                                827579 -> "Nal Hutta"
                                400251 -> "Mustafar"
                                157228 -> "Cantonica"
                                729117 -> "Dathomir"
                                139305 -> "Iego"
                                757884 -> "Myrkr"
                                942574 -> "Felucia"
                                952971 -> "Honoghr"
                                174042 -> "Byss"
                                957421 -> "Alzoc III"
                                162394 -> "Kamino"
                                256639 -> "Kothlis"
                                903548 -> "Vassek"
                                else -> "Unknown"
                            }
                            dataPadDao.updateDataString(9, planet + "is locked")
                        } else {
                            dataPadDao.updateDataString(9, "You have not completed all the requirements to see this event.")
                        }
                        return
                    }
                }
                // All requirements are met, they can complete this quest stage
                Log.d("Go to Complete", "Complete")
                completeQuest(id.toInt())
            }
        // Entered the code to reset the database
        } else if (id == "0000000000") {
            resetUser()
        // Entered the code to show final score details
        } else if (id == "999999999") {
            showScore()
            }
        }

    fun showScore() {
        var printString = "Ships Owned \n"
        if (dataPadDao.shipOwned(817)) {
            printString += "Corellian Light Freighter\n"
        }
        if (dataPadDao.shipOwned(771)) {
            printString += "Medium Transport\n"
        }
        if (dataPadDao.shipOwned(994)) {
            printString += "Corellian Corvette\n"
        }
        printString += "\nUpgrades\n"
        val upgrades = dataPadDao.getUpgrades()
        for (upgrade in upgrades) {
            if (upgrade.owned) {
                printString += upgrade.label +"\n"
            }
        }
        printString += "\nQuests\n"
        val questLines = dataPadDao.getQuestLines()
        for (line in questLines) {
            var total = dataPadDao.countQuests(line.id)
            var seen = dataPadDao.countSeen(line.id)
            printString += line.name + " (" + seen.toString() + "/" + total.toString() + ")\n"
        }
    }


    fun searchSell(id:Int) {
        val count = dataPadDao.itemPresent(id)
        if (count != 0) {
            val item = dataPadDao.getInventoryItem(id)
            dataPadDao.updateDataString(7, item.name)
            if (item.value != 0) {
                val inventory = dataPadDao.inventoryItem(item.name)
                dataPadDao.updateDataString(6, inventory.quantity.toString())
                dataPadDao.updateDataString(8, item.value.toString())
            } else {
                dataPadDao.updateDataString(6,"0")
                dataPadDao.updateDataString(8,"0")
            }
        } else {
            dataPadDao.updateDataString(6, "0")
            dataPadDao.updateDataString(7, "Please enter a valid inventory item ID")
            dataPadDao.updateDataString(8, "0")
        }
    }

    fun searchUpgrade(id:Int) {
        val count = dataPadDao.upgradePresent(id)
        if (count !=0) {
            val upgrade:Upgrades = dataPadDao.findUpgrade(id)
            if (!upgrade.owned) {
                if (id !in listOf(771,817,994)) {
                    val ship = dataPadDao.getShip()
                    if (
                        id == 919 && !ship.smuggle ||
                        id == 200 && !ship.shields ||
                        id == 111 && !ship.laserBattery ||
                        id == 705 && !ship.navComputer ||
                        id == 979 && !ship.ionCanon ||
                        id == 402 && !ship.targetComp ||
                        id == 685 && !ship.holdingPen ||
                        id == 719 && !ship.reinforcedHolding
                    ) {
                        dataPadDao.updateDataString(1, "Your current ship cannot use this upgrade")
                    } else {
                        dataPadDao.updateDataString(1, upgrade.label)
                    }
                } else {
                    dataPadDao.updateDataString(
                        1, upgrade.label + " WARNING: Buying a new ship will reset all other ship ugrades!")
                }
            } else {
                dataPadDao.updateDataString(1, "You already own this upgrade")
            }
        } else {
            dataPadDao.updateDataString(1, "Please enter a valid upgrade ID.")
        }
    }

    fun sellItem(id:Int, count: Int) {
        val valid = dataPadDao.itemPresent(id)
        Log.d("Valid", valid.toString())
        val activeUser: UserData? = userData.value
        if (valid != 0 && activeUser != null) {
            val item = dataPadDao.getInventoryItem(id)
            val inventory = dataPadDao.inventoryItem(item.name)
            val newCount = inventory.quantity - count
            val credits: Int = activeUser.credits + (count * item.value)
            var newTotal = 0

            if (newCount != 0) {
                newTotal = inventory.totalPrice - (count * item.value)
            }

            Log.d("Sell", item.name + " " + newTotal + " " + newCount + " " + credits)
            dataPadDao.updateInventory(item.name,newCount,newTotal)
            dataPadDao.updateUser(credits, activeUser.score)
            updateInventory()

            dataPadDao.updateDataString(6, "")
            dataPadDao.updateDataString(8, "0")
        }
    }

    fun updateInventory() {
        var inventoryString: String = ""
        val inventory = dataPadDao.getInventory()
        for (item in inventory) {
            if (item.quantity != 0) {
                inventoryString += item.name + ": " + item.quantity + "\n"
            }
        }
        dataPadDao.updateDataString(5, inventoryString)
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