package com.example.favdishnew.model.database

import androidx.room.*
import com.example.favdishnew.model.entities.FavDish
import kotlinx.coroutines.flow.Flow


@Dao
interface FavDishDao {

    @Insert
    suspend fun insertFavDishDetails(favDish: FavDish)

    @Update
    suspend fun upDateFavDishDetails(favDish: FavDish)

    @Delete
    suspend fun deleteFavDishDetails(favDish: FavDish)

    @Query("SELECT * FROM  fav_dishes_table ORDER BY ID")
    fun getAllDishesList(): Flow<List<FavDish>>

    @Query("SELECT * FROM fav_dishes_table WHERE favourite_dish=1")
    fun getAllFavouriteDishesList(): Flow<List<FavDish>>

    @Query("SELECT * FROM fav_dishes_table WHERE type= :filterType")
    fun getAllFilteredDishesList(filterType:String):Flow<List<FavDish>>
}