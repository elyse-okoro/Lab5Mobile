package com.example.lab5mobile.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [AirportEntity::class, FavoriteEntity::class], version = 1, exportSchema = false)
abstract class FlightDatabase : RoomDatabase() {

    abstract fun airportDao(): AirportDao

    abstract fun favoriteDao(): FavoriteDao

    companion object {
        private const val DATABASE_NAME = "flight_search"
        private const val ASSET_PATH = "database/flight_search.db"

        @Volatile
        private var INSTANCE: FlightDatabase? = null

        /**
         * Одна база на всё приложение. Double-checked locking: без блокировки, если экземпляр
         * уже есть, а создание — внутри synchronized, чтобы два потока не открыли базу дважды.
         */
        fun getDatabase(context: Context): FlightDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    FlightDatabase::class.java,
                    DATABASE_NAME
                )
                    .createFromAsset(ASSET_PATH)
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
