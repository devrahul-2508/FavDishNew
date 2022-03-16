package com.example.favdishnew.viewModel

import androidx.lifecycle.*
import com.example.favdishnew.model.database.FavDishRepository
import com.example.favdishnew.model.entities.FavDish
import kotlinx.coroutines.launch


class FavDishViewModel(private val repository: FavDishRepository): ViewModel() {

   val allDishesList: LiveData<List<FavDish>> =repository.allDishesList.asLiveData()

    val favouriteDishesList:LiveData<List<FavDish>> =repository.favouriteDishesList.asLiveData()

    fun getFilteredDish(item:String):LiveData<List<FavDish>> =repository.getFilteredDishes(item).asLiveData()

    fun insert(dish:FavDish)=viewModelScope.launch {
       repository.insertFavDishData(dish)
   }
    fun update(dish: FavDish)=viewModelScope.launch {
        repository.updateFavDishData(dish)
    }
    fun delete(dish: FavDish)=viewModelScope.launch {
        repository.deleteFavDishData(dish)
    }
}
class FavDishViewModelFactory(private val repository: FavDishRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavDishViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return FavDishViewModel(repository) as T
    }
    throw IllegalArgumentException("Unknown ViewModel class")
}
}