package com.example.favdishnew.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.favdishnew.model.entities.FavDish


@Database(entities = [FavDish::class],version = 1)
abstract class FavDishRoomDatabase : RoomDatabase() {

 abstract fun favDishDao(): FavDishDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: FavDishRoomDatabase? = null

        fun getDatabase(context: Context): FavDishRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FavDishRoomDatabase::class.java,
                    "fav_dish_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

}