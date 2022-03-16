package com.example.favdishnew.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.favdish.adapters.FavouriteDishesAdapter
import com.example.favdishnew.application.FavDishApplication
import com.example.favdishnew.databinding.FragmentFavouriteBinding
import com.example.favdishnew.model.entities.FavDish
import com.example.favdishnew.view.activities.MainActivity
import com.example.favdishnew.viewModel.FavDishViewModel
import com.example.favdishnew.viewModel.FavDishViewModelFactory


class Favourite : Fragment() {

    private val favDishViewModel: FavDishViewModel by viewModels{
        FavDishViewModelFactory((requireActivity().application as FavDishApplication).repository)
    }
    private lateinit var  binding: FragmentFavouriteBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentFavouriteBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        favDishViewModel.favouriteDishesList.observe(viewLifecycleOwner){
            dishes->
            dishes.let {
                if (it.isNotEmpty()){
                    binding.rvFavDishes.visibility=View.VISIBLE
                    binding.nofavDishTV.visibility=View.GONE
                    binding.rvFavDishes.layoutManager= LinearLayoutManager(context)
                    val favouriteDishesAdapter= FavouriteDishesAdapter(it,this)
                    binding.rvFavDishes.adapter=favouriteDishesAdapter

                }
                else{
                    binding.rvFavDishes.visibility=View.GONE
                    binding.nofavDishTV.visibility=View.VISIBLE
                }
            }
        }
    }
    fun dishDetails(favDish: FavDish){
        findNavController().navigate(FavouriteDirections.actionFavouriteToDishDetailsFragment(favDish))
        if(requireActivity() is MainActivity){
            (activity as MainActivity?)!!.hideBottomNavigationView()
        }

    }
    fun removeFromFavourites(favDish: FavDish){
        favDish.favouriteDish=false
        favDishViewModel.update(favDish)
        Toast.makeText(context,"Removed From Favourites", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        if(requireActivity() is MainActivity){
            (activity as MainActivity?)!!.showBottomNavigationView()
        }
    }

}

