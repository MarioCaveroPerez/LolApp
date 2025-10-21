package com.example.lolapp.Data.Local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "champion_details")
data class ChampionDetailEntity(
    @PrimaryKey val id: String,
    val name: String,
    val title: String,
    val lore: String,
    val statsHp: Double,
    val statsHpPerLevel: Double,
    val statsMp: Double,
    val statsMpPerLevel: Double,
    val statsMoveSpeed: Double,
    val statsArmor: Double,
    val statsArmorPerLevel: Double,
    val statsSpellBlock: Double,
    val statsSpellBlockPerLevel: Double,
    val statsAttackRange: Double,
    val statsAttackDamage: Double,
    val statsAttackDamagePerLevel: Double,
    val statsAttackSpeed: Double,
    val statsAttackSpeedPerLevel: Double,
    val infoAttack: Int,
    val infoDefense: Int,
    val infoMagic: Int,
    val infoDifficulty: Int
)
