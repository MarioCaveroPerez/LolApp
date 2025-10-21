package com.example.lolapp.Data.Repository

import android.content.Context
import com.example.lolapp.Data.Champion
import com.example.lolapp.Data.ChampionDetailFull
import com.example.lolapp.Data.ChampionWithSpells
import com.example.lolapp.Data.Local.ChampionDao
import com.example.lolapp.Data.Local.ChampionDetailDao
import com.example.lolapp.Data.Local.ChampionSpellsDao
import com.example.lolapp.Data.Local.DatabaseProvider
import com.example.lolapp.Data.Mappers.toEntity
import com.example.lolapp.Data.Mappers.toEntityFull
import com.example.lolapp.Data.Mappers.toChampionDetailFull
import com.example.lolapp.Data.Mappers.toChampionWithSpells
import com.example.lolapp.Utils.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChampionRepository(
    private val api: ApiService,
    private val championDao: ChampionDao,
    private val championDetailDao: ChampionDetailDao,
    private val championSpellsDao: ChampionSpellsDao
) {

    // Cache en memoria
    private val championDetailCache = mutableMapOf<String, ChampionDetailFull>()
    private val championSpellsCache = mutableMapOf<String, ChampionWithSpells>()

    // --- Obtener lista básica de campeones ---
    suspend fun getChampions(forceRefresh: Boolean = false): List<Champion> {
        return withContext(Dispatchers.IO) {
            val localData = championDao.getAllChampions()
            if (localData.isEmpty() || forceRefresh) {
                val response = api.getChampions()
                val champions = response.data.values.map { it.toEntity() }
                championDao.clearChampions()
                championDao.insertAll(champions)
                response.data.values.toList()
            } else {
                // Mapear Entity -> Champion para UI
                localData.map {
                    Champion(
                        id = it.id,
                        name = it.name,
                        title = it.title,
                        image = com.example.lolapp.Data.ChampionImageResponse(full = it.imageFull)
                    )
                }
            }
        }
    }

    // --- Obtener detalles completos de un campeón ---
    suspend fun getChampionDetail(championId: String, forceRefresh: Boolean = false): ChampionDetailFull {
        return withContext(Dispatchers.IO) {
            // Primero cache en memoria
            championDetailCache[championId]?.let { return@withContext it }

            // Luego DB local
            val local = championDetailDao.getChampionDetail(championId)
            if (local != null && !forceRefresh) {
                val mapped = local.toChampionDetailFull()
                championDetailCache[championId] = mapped
                return@withContext mapped
            }

            // Finalmente API
            val response = api.getChampionDetailsWithStats(championId)
            val champion = response.data[championId]!!

            // Guardar en DB
            championDetailDao.insertChampionDetail(champion.toEntityFull())
            championDetailCache[championId] = champion.copy(skins = champion.skins)


            // Cache en memoria
            championDetailCache[championId] = champion
            champion
        }
    }


    suspend fun getChampionSpells(championId: String, forceRefresh: Boolean = false): ChampionWithSpells {
        return withContext(Dispatchers.IO) {
            championSpellsCache[championId]?.let { return@withContext it }

            val local = championSpellsDao.getChampionSpells(championId)
            if (local != null && !forceRefresh) {
                val mapped = local.toChampionWithSpells()
                championSpellsCache[championId] = mapped
                return@withContext mapped
            }

            val response = api.getChampionDetailsWithSpells(championId)
            val champion = response.data[championId]!!
            championSpellsDao.insertChampionSpells(champion.toEntity())
            championSpellsCache[championId] = champion
            champion
        }
    }

    companion object {
        fun create(context: Context, api: ApiService): ChampionRepository {
            val db = DatabaseProvider.getDatabase(context)
            return ChampionRepository(api, db.championDao(), db.championDetailDao(), db.championSpellsDao())
        }
    }
}
