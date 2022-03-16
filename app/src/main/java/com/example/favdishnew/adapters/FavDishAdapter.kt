package com.example.favdishnew.adapters

import android.content.Intent
import android.provider.SyncStateContract
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.favdishnew.R
import com.example.favdishnew.databinding.ItemDishLayoutBinding
import com.example.favdishnew.model.entities.FavDish
import com.example.favdishnew.utils.Constants
import com.example.favdishnew.utils.Constants.EXTRA_DISH_DETAILS
import com.example.favdishnew.view.activities.AddUpdateDishActivity
import com.example.favdishnew.view.fragments.AllDishes


class  FavDishAdapter(private val fragment: Fragment

): RecyclerView.Adapter<FavDishAdapter.ViewHolder>() {

    private var dishes:List<FavDish> = listOf()

        class ViewHolder(view: ItemDishLayoutBinding):RecyclerView.ViewHolder(view.root){
            val ivDishImage=view.ivDishImage
            val tvTitle=view.tvDishTitle
            val btnMore=view.btnMore
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding:ItemDishLayoutBinding= ItemDishLayoutBinding.inflate(LayoutInflater.from(fragment.context),parent,false)
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item=dishes[position]
        holder.tvTitle.text=item.title
        Glide.with(fragment).load(item.image).into(holder.ivDishImage)
        holder.itemView.setOnClickListener {
            if (fragment is AllDishes) {
                fragment.dishDetails(item)
            }
        }
        holder.btnMore.setOnClickListener {
            val popupMenu= PopupMenu(fragment.requireContext(),holder.btnMore)
            popupMenu.menuInflater.inflate(R.menu.dish_menu,popupMenu.menu)
            popupMenu.setOnMenuItemClickListener {
                if (it.itemId==R.id.delete){
                    if(fragment is AllDishes){
                        fragment.deleteDishDetails(item)
                    }
                }
                else if(it.itemId==R.id.update){
                    val intent= Intent(fragment.activity, AddUpdateDishActivity::class.java)
                     intent.putExtra(Constants.EXTRA_DISH_DETAILS,item)
                     fragment.requireActivity().startActivity(intent)
                }
                true
            }
            popupMenu.show()
        }

      //  holder.itemView.setOnLongClickListener(object : View.OnLongClickListener {
     //       override fun onLongClick(p0: View?): Boolean {
     //           onClickListener.onClick(item)
     //           return true
     //       }

    //    })


    }

    override fun getItemCount(): Int {
        return dishes.size
    }

    fun dishesList(list:List<FavDish>){
        dishes=list
        notifyDataSetChanged()
    }

}