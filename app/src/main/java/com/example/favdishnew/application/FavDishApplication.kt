package com.example.favdishnew.application

import android.app.Application
import com.example.favdishnew.model.database.FavDishRepository
import com.example.favdishnew.model.database.FavDishRoomDatabase


class FavDishApplication : Application(){
    private val database by lazy { FavDishRoomDatabase.getDatabase(this@FavDishApplication) }

     val repository by lazy { FavDishRepository(database.favDishDao()) }
}