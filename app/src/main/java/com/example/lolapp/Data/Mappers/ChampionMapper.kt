package com.example.lolapp.Data.Mappers

import com.example.lolapp.Data.Champion
import com.example.lolapp.Data.ChampionDetailFull
import com.example.lolapp.Data.ChampionWithSpells
import com.example.lolapp.Data.Local.ChampionDetailEntity
import com.example.lolapp.Data.Local.ChampionEntity
import com.example.lolapp.Data.Local.ChampionSpellsEntity
import com.example.lolapp.Data.Skins

fun Champion.toEntity(): ChampionEntity {
    return ChampionEntity(
        id = this.id,
        name = this.name,
        title = this.title,
        imageFull = this.image.full
    )
}
// API -> DB
fun ChampionDetailFull.toEntityFull(): ChampionDetailEntity {
    return ChampionDetailEntity(
        id = this.id,
        name = this.name,
        title = this.title,
        lore = this.lore,
        statsHp = this.stats.hp,
        statsHpPerLevel = this.stats.hpperlevel,
        statsMp = this.stats.mp,
        statsMpPerLevel = this.stats.mpperlevel,
        statsMoveSpeed = this.stats.movespeed,
        statsArmor = this.stats.armor,
        statsArmorPerLevel = this.stats.armorperlevel,
        statsSpellBlock = this.stats.spellblock,
        statsSpellBlockPerLevel = this.stats.spellblockperlevel,
        statsAttackRange = this.stats.attackrange,
        statsAttackDamage = this.stats.attackdamage,
        statsAttackDamagePerLevel = this.stats.attackdamageperlevel,
        statsAttackSpeed = this.stats.attackspeed,
        statsAttackSpeedPerLevel = this.stats.attackspeedperlevel,
        infoAttack = this.info.attack,
        infoDefense = this.info.defense,
        infoMagic = this.info.magic,
        infoDifficulty = this.info.difficulty
    )
}

// DB -> UI/API
fun ChampionDetailEntity.toChampionDetailFull(skins: List<Skins> = listOf()): ChampionDetailFull {
    return ChampionDetailFull(
        id = this.id,
        name = this.name,
        title = this.title,
        lore = this.lore,
        stats = com.example.lolapp.Data.Stats(
            hp = statsHp,
            hpperlevel = statsHpPerLevel,
            mp = statsMp,
            mpperlevel = statsMpPerLevel,
            movespeed = statsMoveSpeed,
            armor = statsArmor,
            armorperlevel = statsArmorPerLevel,
            spellblock = statsSpellBlock,
            spellblockperlevel = statsSpellBlockPerLevel,
            attackrange = statsAttackRange,
            attackdamage = statsAttackDamage,
            attackdamageperlevel = statsAttackDamagePerLevel,
            attackspeed = statsAttackSpeed,
            attackspeedperlevel = statsAttackSpeedPerLevel
        ),
        info = com.example.lolapp.Data.Info(
            attack = infoAttack,
            defense = infoDefense,
            magic = infoMagic,
            difficulty = infoDifficulty
        ),
        skins = skins
    )
}

fun ChampionWithSpells.toEntity(): ChampionSpellsEntity {
    val spellsJson = com.google.gson.Gson().toJson(this.spells)
    return ChampionSpellsEntity(
        id = this.id,
        passiveName = this.passive.name,
        passiveDescription = this.passive.description,
        passiveImage = this.passive.image.full,
        spellsJson = spellsJson
    )
}

// DB -> UI/API
fun ChampionSpellsEntity.toChampionWithSpells(): ChampionWithSpells {
    val spells: List<com.example.lolapp.Data.SpellApi> =
        com.google.gson.Gson().fromJson(this.spellsJson, Array<com.example.lolapp.Data.SpellApi>::class.java).toList()
    return ChampionWithSpells(
        id = this.id,
        name = "", // Opcional: si quieres usar name, guárdalo en DB
        passive = com.example.lolapp.Data.SpellApi(
            name = this.passiveName,
            description = this.passiveDescription,
            image = com.example.lolapp.Data.SpellImage(full = this.passiveImage)
        ),
        spells = spells
    )
}