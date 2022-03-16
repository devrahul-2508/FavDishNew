package com.example.favdishnew.view.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.favdishnew.R
import com.example.favdishnew.application.FavDishApplication
import com.example.favdishnew.databinding.FragmentDishDetailsBinding
import com.example.favdishnew.model.entities.FavDish
import com.example.favdishnew.utils.Constants
import com.example.favdishnew.viewModel.FavDishViewModel
import com.example.favdishnew.viewModel.FavDishViewModelFactory


class DishDetailsFragment : Fragment() {

    private lateinit var binding: FragmentDishDetailsBinding

    private var favDishDetails:FavDish?=null
    private val favDishViewModel: FavDishViewModel by viewModels {
        FavDishViewModelFactory((requireActivity().application as FavDishApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentDishDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.toolBar.setNavigationIcon(R.drawable.ic_back)
        binding.toolBar.setNavigationOnClickListener {
            requireActivity().onBackPressed()

        }
        val args:DishDetailsFragmentArgs by navArgs()
        favDishDetails=args.dishDetails

        Glide.with(this).load(args.dishDetails.image).into(binding.ivDishImage)
        binding.tvTitle.text=args.dishDetails.title
        binding.tvType.text=args.dishDetails.type
        binding.tvCategory.text=args.dishDetails.category
        binding.tvIngredients.text=args.dishDetails.ingredients
        binding.tvCookingDirection.text=args.dishDetails.directionToCook
        binding.tvCookingTime.text=("It may take approx "+args.dishDetails.cookingTime+" minutes")
        Log.i("Dish Source", favDishDetails!!.image)
        if (args.dishDetails.favouriteDish){
            binding.ivFavoriteDish.setImageResource(R.drawable.ic_favorite_selected)

        }

        binding.ivFavoriteDish.setOnClickListener {
            if (!args.dishDetails.favouriteDish){
                binding.ivFavoriteDish.setImageResource(R.drawable.ic_favorite_selected)
                args.dishDetails.favouriteDish=true
                favDishViewModel.update(args.dishDetails)
                Toast.makeText(requireContext(),"Added to favourites",Toast.LENGTH_SHORT).show()
            }
            else if (args.dishDetails.favouriteDish){
                binding.ivFavoriteDish.setImageResource(R.drawable.ic_favorite_unselected)
                args.dishDetails.favouriteDish=false
                favDishViewModel.update(args.dishDetails)
                Toast.makeText(requireContext(),"Removed from Favourites",Toast.LENGTH_SHORT).show()

            }
        }
        binding.btnShare.setOnClickListener {
            val type="text/plain"
            val subject="Checkout this dish"
            var extraText=""
            val shareWith="Share with"
            favDishDetails?.let {
                var image=""
                if(it.imageSource==Constants.DISH_IMAGE_SOURCE_ONLINE){
                    image=it.image
                }
                var cookingInstructions=""
                cookingInstructions=it.directionToCook

                extraText =
                    "$image \n" +
                            "\n Title:  ${it.title} \n\n Type: ${it.type} \n\n Category: ${it.category}" +
                            "\n\n Ingredients: \n ${it.ingredients} \n\n Instructions To Cook: \n $cookingInstructions" +
                            "\n\n Time required to cook the dish approx ${it.cookingTime} minutes."
            }
            val intent=Intent(Intent.ACTION_SEND)
            intent.type=type
            intent.putExtra(Intent.EXTRA_SUBJECT,subject)
            intent.putExtra(Intent.EXTRA_TEXT,extraText)
            startActivity(Intent.createChooser(intent,shareWith))
        }
    }



    }


