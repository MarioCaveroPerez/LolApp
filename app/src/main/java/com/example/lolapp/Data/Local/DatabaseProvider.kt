package com.example.lolapp.Data.Local

import android.content.Context
import androidx.room.Room

object DatabaseProvider {
    private var INSTANCE: LoLDatabase? = null

    fun getDatabase(context: Context): LoLDatabase {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                LoLDatabase::class.java,
                "lol_database"
            )
                .fallbackToDestructiveMigration() // <- Esto fuerza a recrear la DB
                .build()
        }
        return INSTANCE!!
    }
}

