package com.example.favdishnew.model.database

import androidx.annotation.WorkerThread
import com.example.favdishnew.model.entities.FavDish
import kotlinx.coroutines.flow.Flow


class FavDishRepository(private val favDishDao: FavDishDao) {

    @WorkerThread
    suspend fun insertFavDishData(favDish: FavDish){
        favDishDao.insertFavDishDetails(favDish)
    }
    @WorkerThread
    suspend fun updateFavDishData(favDish: FavDish){
      favDishDao.upDateFavDishDetails(favDish)
    }
    @WorkerThread
    suspend fun deleteFavDishData(favDish: FavDish){
        favDishDao.deleteFavDishDetails(favDish)
    }

    val favouriteDishesList: Flow<List<FavDish>> =favDishDao.getAllFavouriteDishesList()

    val allDishesList:Flow<List<FavDish>> = favDishDao.getAllDishesList()

    fun getFilteredDishes(item:String):Flow<List<FavDish>> =favDishDao.getAllFilteredDishesList(item)


}