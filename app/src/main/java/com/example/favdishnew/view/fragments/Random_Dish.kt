package com.example.favdishnew.view.fragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.favdishnew.R
import com.example.favdishnew.application.FavDishApplication
import com.example.favdishnew.databinding.FragmentRandomDishBinding
import com.example.favdishnew.model.entities.FavDish
import com.example.favdishnew.model.entities.RandomDish
import com.example.favdishnew.utils.Constants
import com.example.favdishnew.viewModel.FavDishViewModel
import com.example.favdishnew.viewModel.FavDishViewModelFactory
import com.example.favdishnew.viewModel.RandomDishViewModel
import kotlin.random.Random


class Random_Dish : Fragment() {

    private var binding: FragmentRandomDishBinding?=null

    private lateinit var randomDishViewModel:RandomDishViewModel

    lateinit var progressBar:Dialog

    private val favDishViewModel: FavDishViewModel by viewModels{
        FavDishViewModelFactory((requireActivity().application as FavDishApplication).repository)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentRandomDishBinding.inflate(layoutInflater,container,false)
        return binding!!.root
    }

    override fun onDestroy() {
        super.onDestroy()
        binding=null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        randomDishViewModel=ViewModelProvider(this).get(RandomDishViewModel::class.java)
        randomDishViewModel.getRandomRecipeFromApi()
        randomDishViewModelObserver()
        binding!!.swipeLayout.setOnRefreshListener {
            Toast.makeText(context,"Refreshing",Toast.LENGTH_SHORT).show()
            randomDishViewModel.getRandomRecipeFromApi()
        }
    }
    private fun showProgressBarDialog(){
        progressBar= Dialog(requireActivity())
        progressBar?.let {
            it.setContentView(R.layout.dialog_custom_progress)
            it.show()
        }

    }
    private fun hideProgressBarDialog(){
        progressBar.dismiss()
    }


private fun randomDishViewModelObserver(){
    randomDishViewModel.randomDishResponse.observe(viewLifecycleOwner){
        randomDishResponse->
                randomDishResponse.let {
                    Log.i("Random Dish Response","${randomDishResponse.recipes[0]}")

                    setRandomDishResponseInUi(randomDishResponse.recipes[0])
                }
    }
    randomDishViewModel.randomDishLoadingError.observe(viewLifecycleOwner){
        dataError->
            dataError.let {
                Log.i("Data Error","$dataError")
            }
    }
    randomDishViewModel.loadRandomDish.observe(viewLifecycleOwner){
        loadRandomDish->
                loadRandomDish.let {
                    Log.i("Random Dish Loading","$loadRandomDish")
                }
        if (loadRandomDish && !binding!!.swipeLayout.isRefreshing){
            showProgressBarDialog()
        }
        else{
            hideProgressBarDialog()
        }
    }
}
    private fun setRandomDishResponseInUi(recipe: RandomDish.Recipe){
        Glide.with(requireActivity()).load(recipe.image).into(binding!!.ivDishImage)
        binding!!.tvTitle.text=recipe.title
        binding!!.tvType.text=recipe.dishTypes[0]
        binding!!.tvCategory.text="Other"
        binding!!.tvIngredients.text=recipe.extendedIngredients[0].original
        binding!!.tvCookingDirection.text=recipe.instructions
        binding!!.tvCookingTime.text="Your Dish will be ready approximately in ${recipe.readyInMinutes} minutes"
        binding!!.swipeLayout.isRefreshing=false

        var addedToFavourites:Boolean=false
        binding!!.ivFavoriteDish.setImageResource(R.drawable.ic_favorite_unselected)


        binding!!.ivFavoriteDish.setOnClickListener {
            if(!addedToFavourites){
                val favDishDetails:FavDish= FavDish(recipe.title,
                    recipe.dishTypes[0],
                    "Other",
                    recipe.extendedIngredients[0].original,
                    recipe.readyInMinutes.toString(),
                    recipe.instructions,
                    true,recipe.image,
                    Constants.DISH_IMAGE_SOURCE_ONLINE)
                binding!!.ivFavoriteDish.setImageResource(R.drawable.ic_favorite_selected)
                favDishViewModel.insert(favDishDetails)
                addedToFavourites=true
            }
            else{
                binding!!.ivFavoriteDish.setImageResource(R.drawable.ic_favorite_unselected)
            }
        }

    }


}