package com.example.favdish.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.favdishnew.databinding.ItemFavouriteDishBinding
import com.example.favdishnew.model.entities.FavDish
import com.example.favdishnew.view.fragments.Favourite


class FavouriteDishesAdapter(private val dishes:List<FavDish>, private val fragment: Fragment):
    RecyclerView.Adapter<FavouriteDishesAdapter.ViewHolder> (){

    class ViewHolder(view: ItemFavouriteDishBinding):RecyclerView.ViewHolder(view.root){
        val favDishTitle=view.favDishTitle
        val favDishType=view.favDishType
        val favDishImage=view.ivFavDish
        val favIcon=view.favIcon
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding=ItemFavouriteDishBinding.inflate(LayoutInflater.from(fragment.requireContext()),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dish=dishes[position]
        holder.favDishTitle.text=dish.title
        holder.favDishType.text=dish.type
        Glide.with(fragment).load(dish.image).circleCrop().into(holder.favDishImage)
        holder.itemView.setOnClickListener {
            if(fragment is Favourite){
                fragment.dishDetails(dish)
            }
        }
        holder.favIcon.setOnClickListener {
            if(fragment is Favourite){
                fragment.removeFromFavourites(dish)
            }
        }

    }

    override fun getItemCount(): Int {
        return dishes.size
    }

}