package com.example.favdishnew.model.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "fav_dishes_table")
data class FavDish(
    @ColumnInfo(name="title")
    var title:String,
    @ColumnInfo(name="type")
    var type:String,
    @ColumnInfo(name = "category")
    var category:String,
    @ColumnInfo(name = "ingredients")
    var ingredients:String,
    @ColumnInfo(name = "cooking_time")
    var cookingTime:String,
    @ColumnInfo(name ="instructions")
    var directionToCook:String,
    @ColumnInfo(name = "favourite_dish")
    var favouriteDish:Boolean=false,
    @ColumnInfo(name="image")
    var image:String,
    @ColumnInfo(name="image_source")
    var imageSource:String,
    @PrimaryKey(autoGenerate = true)
    var id:Int=0
): Parcelable