package com.example.starwarsdatapad.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query


@Dao
interface DataPadDao {

    @Query(
        """
        SELECT credits, score
        FROM user
        WHERE id= :id
    """
    )
    fun getUser(id: Int): LiveData<UserData>

    @Query(
        """
        SELECT CAST (cargo as string)
        FROM ships
        WHERE id = (SELECT ship FROM user WHERE id =1)
    """
    )
    fun getCargo(): LiveData<String>

    @Query("""
        SELECT cargo
        FROM ships
        WHERE id = (SELECT ship FROM user WHERE id=1)
    """)
    fun getCargoSize(): Int

    @Query(
        """
        SELECT *
        FROM upgrades
        WHERE owned = 1
    """
    )
    fun getUpgrades(): List<Upgrades>

    @Query ("""
        SELECT owned
        FROM upgrades
        WHERE id = 919
        """)
    fun hasSmuggle(): Boolean

    @Query("""
        SELECT owned
        FROM upgrades
        WHERE id = 685
    """)
    fun hasHoldingPen(): Boolean

    @Query("""
        SELECT owned
        FROM upgrades
        WHERE id = 719
    """)
    fun hasReinforcedPen(): Boolean

    @Query(
        """
        SELECT COUNT(id)
        FROM upgrades
        WHERE id=:id
    """
    )
    fun upgradePresent(id: Int): Int

    @Query("""
        SELECT COUNT(id)
        FROM trade
        WHERE id=:id
    """)
    fun itemPresent(id:Int): Int

    @Query("""
        SELECT COUNT(id)
        FROM quests
        WHERE id=:id
    """)
    fun questPresent(id:Int): Int

    @Query("""
        SELECT complete
        FROM quests
        WHERE id=:id
    """)
    fun reqMet(id:Int): Boolean

    @Query("""
        SELECT *
        FROM quests
        WHERE id=:id
    """)
    fun getQuest(id:Int): Quests

    @Query("""
        SELECT *
        FROM quest_lines
        WHERE id=:id
    """)
    fun getQuestLine(id:Int): QuestLines

    @Query(
        """
        SELECT *
        FROM upgrades
        WHERE id= :id
    """
    )
    fun findUpgrade(id: Int): Upgrades

    @Query(
        """
        SELECT *
        FROM ships
        WHERE id = (SELECT ship FROM user WHERE id =1) 
    """
    )
    fun getShip(): Ships

    @Query(
        """
        SELECT *
        FROM inventory
    """
    )
    fun getInventory(): List<Inventory>

    @Query(
        """
        SELECT *
        FROM trade
        WHERE id=:id
    """
    )
    fun getInventoryItem(id: Int): Trade

    @Query("""
        SELECT *
        FROM inventory
        WHERE name=:name
    """)
    fun inventoryItem(name:String): Inventory

    @Query("""
        SELECT cargo_bay_size
        FROM trade
        WHERE name=:name
        LIMIT 1
    """)
    fun getItemSize(name:String): Int

    @Query("""
        UPDATE inventory
        SET quantity= :quantity,
            total_price= :value
        WHERE name= :name
    """)
    fun updateInventory(name: String, quantity: Int, value:Int)

    @Query(
        """
        SELECT DISTINCT cargo_bay_size
        FROM trade
        WHERE name=:name
        LIMIT 1
    """
    )
    fun getItemData(name: String): Int

    @Query(
        """
        SELECT text_value
        FROM data_strings
        WHERE id=:id
    """
    )
    fun getDataString(id: Int): LiveData<String>

    @Query(
        """
        UPDATE data_strings
        SET text_value = :value
        WHERE id=:id
    """
    )
    fun updateDataString(id: Int, value: String)

    @Query(
        """
        UPDATE user
        SET credits = :credits,
            score = :score
        WHERE id=1
    """
    )
    fun updateUser(credits: Int, score: Int)

    @Query(
        """
        UPDATE user
        SET ship = :ship
        WHERE id=1
    """
    )
    fun updateShip(ship: Int)

    @Query(
        """
        UPDATE upgrades
        SET owned = 1
        WHERE id=:id
    """
    )
    fun buyUpgrade(id: Int)

    @Query(
        """
        SELECT text_value
        FROM data_strings
        WHERE ID = 2
    """
    )
    fun checkUpdate(): String

    @Query("""
        SELECT ship
        FROM user
    """)
    fun getUserShip(): Int

    @Query("""
        UPDATE quests
        SET complete = 0
    """)
    fun resetQuests()

    @Query("""
        UPDATE quest_lines
        SET active_stage = 0
    """)
    fun resetQuestLines()

    @Query("""
        UPDATE upgrades
        SET owned = 0
        WHERE id not in (817,771,994)
    """)
    fun resetUpgrades()

    @Query("""
        UPDATE upgrades
        set owned = 0
    """)
    fun starterUpgrades()

    @Query("""
        UPDATE upgrades
        SET owned = 1
        WHERE id = 817
    """)
    fun starterShip()

    @Query("""
        UPDATE user
        set credits = 1000,
            score = 0,
            ship = 817,
            upgrades = ""
    """)
    fun resetUser()

    @Query("""
        UPDATE inventory
        set quantity = 0,
            total_price = 0
    """)
    fun resetInventory()

    @Query("""
        UPDATE ships
        SET owned = 0
        WHERE id in (771,994)
    """)
    fun resetShips()

    @Query("""
        UPDATE ships
        set owned = 1
        WHERE id = 817
    """)
    fun addStarter()

    @Query("""
        SELECT owned
        FROM ships
        WHERE id = :id
    """)
    fun shipOwned(id:Int): Boolean

    @Query("""
        SELECT *
        FROM quest_lines
    """)
    fun getQuestLines(): List<QuestLines>

    @Query("""
        SELECT COUNT(id)
        FROM quests
        WHERE quest_line_id = :id
    """)
    fun countQuests(id:Int): Int

    @Query("""
        SELECT COUNT(id)
        FROM quests
        WHERE quest_line_id = :id
            AND complete = 1
    """)
    fun countSeen(id:Int): Int

    @Query("""
        UPDATE quests
        SET complete = 1
        WHERE id =:id
    """)
    fun completeQuest(id:Int)

    @Query("""
        UPDATE quest_lines
        SET active_stage = :questID
        WHERE id= :id
    """)
    fun updateLine(id:Int, questID: Int)

    @Query("""
        SELECT *
        FROM quest_lines
        WHERE active_stage != 0
            AND active_stage != final_stage
    """)
    fun getActiveQuests(): List<QuestLines>
}