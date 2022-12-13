package com.example.starwarsdatapad.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quests")
data class Quests (
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "label")
    val label: String,
    @ColumnInfo(name = "quest_line_id")
    val questLineId: Int,
    @ColumnInfo(name = "display_text_value")
    val displayTextVal: String,
    @ColumnInfo(name = "reminder_text")
    val reminderText: String,
    @ColumnInfo(name = "requires")
    val requires: String,
    @ColumnInfo(name = "failure_message")
    val failureMessage: String,
    @ColumnInfo(name = "decision_id")
    val decisionId: Int,
    @ColumnInfo(name = "credit_reward")
    val creditReward: Int,
    @ColumnInfo(name = "score_reward")
    val scoreReward: Int,
    @ColumnInfo(name = "complete")
    val complete: Boolean = false
)

data class QuestName (
    val id: Int, // This is the Quest Line ID
    val name: String
)

@Entity(tableName = "quest_lines")
data class QuestLines (
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "first_stage")
    val firstStage: Int,
    @ColumnInfo(name = "active_stage")
    val activeStage: Int,
    @ColumnInfo(name = "final_stage")
    val finalStage: Int
)

@Entity(tableName = "decision")
data class Decision (
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "required_incoming_id")
    val reqIncomingId: Int,
    @ColumnInfo(name = "required_quest_ids")
    val reqQuestIds: String,
    @ColumnInfo(name = "required_inventory_ids")
    val reqInventoryIds: String,
    @ColumnInfo(name = "required_upgrade_ids")
    val reqUpgradeIds: String,
    @ColumnInfo(name = "required_credits")
    val reqCredits: Int,
    @ColumnInfo(name = "success_id")
    val successId: Int,
    @ColumnInfo(name = "fail_id")
    val failId: Int,
    @ColumnInfo(name = "fail_message")
    val failMessage: String
)

@Entity(tableName = "trade")
data class Trade (
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "planet")
    val planet: String,
    @ColumnInfo(name = "value")
    val value: Int,
    @ColumnInfo(name= "cargo_bay_size")
    val cargoBay: Int
)

@Entity(tableName = "upgrades")
data class Upgrades (
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "label")
    val label: String,
    @ColumnInfo(name = "cost")
    val cost: Int,
    @ColumnInfo(name = "owned")
    val owned: Boolean = false
)

@Entity(tableName = "user")
data class User (
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "credits")
    val credits: Int,
    @ColumnInfo(name = "score")
    val score: Int,
    @ColumnInfo(name = "ship")
    val ship: Int,
    @ColumnInfo(name = "upgrades")
    val upgrades: String
)

data class UserData (
    val credits: Int,
    val score: Int,
)


@Entity(tableName = "inventory")
data class Inventory (
    @PrimaryKey
    val name: String,
    @ColumnInfo(name = "quantity")
    val quantity: Int,
    @ColumnInfo(name = "total_price")
    val totalPrice: Int
)

@Entity(tableName = "Ships")
data class Ships (
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "cargo")
    val cargo: Int,
    @ColumnInfo(name = "smuggle")
    val smuggle: Boolean,
    @ColumnInfo(name = "shields")
    val shields: Boolean,
    @ColumnInfo(name = "laser_battery")
    val laserBattery: Boolean,
    @ColumnInfo(name = "nav_computer")
    val navComputer: Boolean,
    @ColumnInfo(name = "ion_canon")
    val ionCanon: Boolean,
    @ColumnInfo(name = "target_computer")
    val targetComp: Boolean,
    @ColumnInfo(name = "holding_pen")
    val holdingPen: Boolean,
    @ColumnInfo(name = "reinforced_holding_pen")
    val reinforcedHolding: Boolean,
    @ColumnInfo(name = "owned")
    val owned: Boolean = false
)

@Entity(tableName = "data_strings")
data class DataStrings (
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name= "text_value")
    val textValue: String
)