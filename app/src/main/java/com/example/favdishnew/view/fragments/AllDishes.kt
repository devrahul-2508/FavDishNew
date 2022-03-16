package com.example.favdishnew.view.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.favdishnew.adapters.CustomListAdapter
import com.example.favdishnew.adapters.FavDishAdapter
import com.example.favdishnew.application.FavDishApplication
import com.example.favdishnew.databinding.DialogCustomListBinding
import com.example.favdishnew.databinding.FragmentAllDishesBinding
import com.example.favdishnew.model.entities.FavDish
import com.example.favdishnew.utils.Constants
import com.example.favdishnew.view.activities.AddUpdateDishActivity
import com.example.favdishnew.view.activities.MainActivity
import com.example.favdishnew.viewModel.FavDishViewModel
import com.example.favdishnew.viewModel.FavDishViewModelFactory


class AllDishes : Fragment() {
    private var _binding: FragmentAllDishesBinding?=null
    lateinit var customListDialog: Dialog
    lateinit var favDishAdapter: FavDishAdapter
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    private val favDishViewModel: FavDishViewModel by viewModels{
        FavDishViewModelFactory((requireActivity().application as FavDishApplication).repository)
    }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        _binding = FragmentAllDishesBinding.inflate(inflater, container, false)
        val view = binding.root
        return view

        }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addNewDish.setOnClickListener {
            val intent= Intent(activity, AddUpdateDishActivity::class.java)
            startActivity(intent)
        }
        binding.filterDish.setOnClickListener {
            filterDishesListDialog()
        }
       binding.rvDishesList.layoutManager= GridLayoutManager(activity,2)
        favDishAdapter= FavDishAdapter(this@AllDishes)

        binding.rvDishesList.adapter=favDishAdapter

        favDishViewModel.allDishesList.observe(viewLifecycleOwner){
            dishes->
                dishes.let {
                    if(it.isNotEmpty()){
                        binding.noDishesTV.visibility=View.GONE
                        binding.rvDishesList.visibility=View.VISIBLE
                        favDishAdapter.dishesList(it)
                    }else{
                        binding.noDishesTV.visibility=View.VISIBLE
                        binding.rvDishesList.visibility=View.GONE
                    }
                }
        }
        }

    fun dishDetails(favDish: FavDish){

        findNavController().navigate(AllDishesDirections.actionAllDishesToDishDetailsFragment(favDish))
        if(requireActivity() is MainActivity){
            (activity as MainActivity?)!!.hideBottomNavigationView()
        }
    }
    private fun filterDishesListDialog(){
         customListDialog=Dialog(requireActivity())
        val mbinding: DialogCustomListBinding = DialogCustomListBinding.inflate(layoutInflater)

        customListDialog.setContentView(mbinding.root)
        mbinding.listTitle.text="Select item to filter"
        val dishtypes=Constants.dishTypes()
        dishtypes.add(0, Constants.ALL_ITEMS)
        mbinding.rvList.layoutManager= LinearLayoutManager(context)
        val adapter= CustomListAdapter(requireActivity(),this,dishtypes,Constants.FILTER_SELECTION)
        mbinding.rvList.adapter=adapter
        customListDialog.show()

    }
    fun filterDishes(item:String){
        Toast.makeText(context,"Clicked on $item", Toast.LENGTH_SHORT).show()
        customListDialog.dismiss()
        if (item==Constants.ALL_ITEMS){
            favDishViewModel.allDishesList.observe(viewLifecycleOwner){
                    dishes->
                dishes.let {
                    if(it.isNotEmpty()){
                        binding.noDishesTV.visibility=View.GONE
                        binding.rvDishesList.visibility=View.VISIBLE
                        favDishAdapter.dishesList(it)
                    }else{
                        binding.noDishesTV.visibility=View.VISIBLE
                        binding.rvDishesList.visibility=View.GONE
                    }
                }
            }

        }
        else{
            favDishViewModel.getFilteredDish(item).observe(viewLifecycleOwner){
                    dishes->
                dishes.let {
                    if(it.isNotEmpty()){
                        binding.noDishesTV.visibility=View.GONE
                        binding.rvDishesList.visibility=View.VISIBLE
                        favDishAdapter.dishesList(it)
                    }else{
                        binding.noDishesTV.visibility=View.VISIBLE
                        binding.rvDishesList.visibility=View.GONE
                    }
                }
            }
        }



    }
    fun deleteDishDetails(favDish: FavDish){
        val builder = AlertDialog.Builder(context)
        //set title for alert dialog
        builder.setTitle("Delete Dish")
        //set message for alert dialog
        builder.setMessage("Are You Sure you want to delete this dish")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("Yes"){dialogInterface, which ->
            Toast.makeText(context,"Dish Deleted",Toast.LENGTH_LONG).show()
            favDishViewModel.delete(favDish)
        }

        //performing negative action
        builder.setNegativeButton("No"){dialogInterface, which ->
            Toast.makeText(context,"clicked No",Toast.LENGTH_LONG).show()
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }


    override fun onResume() {
        super.onResume()
        if(requireActivity() is MainActivity){
            (activity as MainActivity?)!!.showBottomNavigationView()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    }








