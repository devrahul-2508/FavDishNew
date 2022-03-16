package com.example.favdishnew.view.activities

import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import com.bumptech.glide.request.target.Target
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.example.favdishnew.R
import com.example.favdishnew.adapters.CustomListAdapter
import com.example.favdishnew.application.FavDishApplication
import com.example.favdishnew.databinding.ActivityAddUpdateDishBinding
import com.example.favdishnew.databinding.DialogCustomImageSelectionBinding
import com.example.favdishnew.databinding.DialogCustomListBinding
import com.example.favdishnew.model.entities.FavDish
import com.example.favdishnew.utils.Constants
import com.example.favdishnew.viewModel.FavDishViewModel
import com.example.favdishnew.viewModel.FavDishViewModelFactory
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*


class AddUpdateDishActivity : AppCompatActivity(), View.OnClickListener{
    private lateinit var binding: ActivityAddUpdateDishBinding
    private lateinit var customListDialog: Dialog
    private  var imagePath:String=""
    private var favDishDetails: FavDish?=null
    private val favDishViewModel: FavDishViewModel by viewModels{
        FavDishViewModelFactory((application as FavDishApplication).repository)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAddUpdateDishBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        binding.edtType.setOnClickListener(this)

        binding.edtcategory.setOnClickListener(this)

        binding.edtCookingTime.setOnClickListener(this)

        binding.addImage.setOnClickListener(this)

        binding.btnAddDish.setOnClickListener(this)


   if (intent.hasExtra(Constants.EXTRA_DISH_DETAILS)) {
       favDishDetails = intent.getParcelableExtra(Constants.EXTRA_DISH_DETAILS)
   }
        setUpActionBar()

        favDishDetails?.let {
            if (it.id != 0) {
                imagePath = it.image
                Glide.with(this).load(imagePath).centerCrop().into(binding.displayImage)
                binding.edtTitle.setText(it.title)
                binding.edtType.setText(it.type)
                binding.edtcategory.setText(it.category)
                binding.edtIngredients.setText(it.ingredients)
                binding.edtCookingTime.setText(it.cookingTime)
                binding.edtDirections.setText(it.directionToCook)

                binding.btnAddDish.text = "Update Dish"

            }
        }



    }
    private fun setUpActionBar(){
        setSupportActionBar(binding.toolbar)
        if (favDishDetails!=null && favDishDetails!!.id!=0){

            supportActionBar?.setTitle("Update Dish")
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle("Add Dish")
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.addImage->{
                customImageSelectorDialog()
                return
            }
            R.id.edtType->{
                displayListType()
                return
            }
            R.id.edtcategory->{
                displayCategoryType()
                return
            }
            R.id.edtCookingTime->{
                displayCookingTime()
                return
            }
            R.id.btnAddDish->{
                val title=binding.edtTitle.text.toString().trim{it<= ' '}
                val type=binding.edtType.text.toString().trim{it<= ' '}
                val category=binding.edtcategory.text.toString().trim { it<= ' ' }
                val ingredients=binding.edtIngredients.text.toString().trim { it<= ' ' }
                val cookingTimeInMinutes=binding.edtCookingTime.text.toString().trim {it<= ' '  }
                val cookingDirection=binding.edtDirections.text.toString().trim { it<= ' ' }

                when{
                    TextUtils.isEmpty(imagePath)->{
                        Toast.makeText(this,"Pls add image",Toast.LENGTH_SHORT).show()
                    }
                    TextUtils.isEmpty(title)->{
                        Toast.makeText(this,"Pls add title",Toast.LENGTH_SHORT).show()

                    }
                    TextUtils.isEmpty(type)->{
                        Toast.makeText(this,"Pls add Type",Toast.LENGTH_SHORT).show()

                    }
                    TextUtils.isEmpty(category)->{
                        Toast.makeText(this,"Pls add category",Toast.LENGTH_SHORT).show()

                    }
                    TextUtils.isEmpty(ingredients)->{
                        Toast.makeText(this,"Pls add ingredients",Toast.LENGTH_SHORT).show()

                    }
                    TextUtils.isEmpty(cookingTimeInMinutes)->{
                        Toast.makeText(this,"Pls add CookingTime",Toast.LENGTH_SHORT).show()

                    }
                    TextUtils.isEmpty(cookingDirection)->{
                        Toast.makeText(this,"Pls add Cooking Directions",Toast.LENGTH_SHORT).show()

                    }
                    else->{
                        var dishID=0
                        var imageSource=Constants.DISH_IMAGE_SOURCE_LOCAL
                        var favouriteDish=false

                        favDishDetails?.let {
                            if(it.id!=0){
                                dishID=it.id
                                imageSource=it.imageSource
                                favouriteDish=it.favouriteDish
                            }
                        }
                        val favDishDetails:FavDish= FavDish(
                            title,
                            type,
                            category,
                            ingredients,
                            cookingTimeInMinutes,
                            cookingDirection,
                            favouriteDish,
                            imagePath,
                            imageSource,
                        dishID)

                        if(dishID == 0){
                            favDishViewModel.insert(favDishDetails)
                            Toast.makeText(this@AddUpdateDishActivity,"Your FavDish Has been Added Successfully",Toast.LENGTH_SHORT).show()
                            Log.i("New dish","${favDishDetails.id}")
                            finish()
                        }
                        else{
                            favDishViewModel.update(favDishDetails)
                            Toast.makeText(this@AddUpdateDishActivity,"Successfully Updated",Toast.LENGTH_SHORT).show()
                            Log.i("Updated Dish","${favDishDetails.id}")

                            finish()

                        }



                    }

                }
            }


        }

    }




    private fun customImageSelectorDialog(){
        val dialog= Dialog(this)
        val binding: DialogCustomImageSelectionBinding = DialogCustomImageSelectionBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)
        binding.tvCamera.setOnClickListener {

            Dexter.withContext(this)
                .withPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                // android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                 android.Manifest.permission.CAMERA).withListener(object:
                    MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        report?.let {
                            if (report!!.areAllPermissionsGranted()){
                                val intent= Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                                startActivityForResult(intent,CAMERA)
                            }
                        }

                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: MutableList<PermissionRequest>?,
                        token: PermissionToken?
                    ) {
                        //show alert dialog
                          showRationDialogPermissions()

                    }


                }).onSameThread().check()

            dialog.dismiss()

        }

        binding.tvGallery.setOnClickListener {
            Dexter.withContext(this)
                .withPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(object: PermissionListener {
                    override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                        val galleryIntent=Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        startActivityForResult(galleryIntent, GALLERY)
                    }

                    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                        Toast.makeText(this@AddUpdateDishActivity,"Permission Denied",Toast.LENGTH_SHORT).show()
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        p0: PermissionRequest?,
                        p1: PermissionToken?
                    ) {
                        showRationDialogPermissions()
                    }


                }).onSameThread().check()

            dialog.dismiss()


        }

        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode== Activity.RESULT_OK){
            data?.extras?.let {
                if(requestCode== CAMERA){
                    val thumbnail: Bitmap =data.extras!!.get("data") as Bitmap
                   // binding.displayImage.setImageBitmap(thumbnail)
                    Glide.with(this).load(thumbnail).centerCrop().into(binding.displayImage)
                    imagePath=saveImagetoInternalStorage(thumbnail)
                    Log.i("imagepath",imagePath)
                }
                if(requestCode== GALLERY){
                    val selectedPhotoUri=data.data
                    binding.displayImage.setImageURI(selectedPhotoUri)
                    Glide.with(this)
                        .load(selectedPhotoUri)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                Log.e("TAG","error loading image")
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target:Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                val bitmap:Bitmap=resource!!.toBitmap()
                                imagePath=saveImagetoInternalStorage(bitmap)
                                return false
                            }

                        })
                        .into(binding.displayImage)

                }

            }
            binding.addImage.setImageResource(R.drawable.ic_edit)

        }else if(resultCode==Activity.RESULT_CANCELED){
            Log.e("cancelled","User Cancelled Image Selection")
        }
    }
    private fun showRationDialogPermissions(){
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setMessage("It looks like you have turned off permissions required for this feature.It cam be enabled under Application settings")
            .setPositiveButton("Go to SETTINGS"){
                _,_->
                try {

                    val intent=Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri= Uri.fromParts("package",packageName,null)
                    intent.data=uri
                    startActivity(intent)
                }catch (e: ActivityNotFoundException){
                    e.printStackTrace()
                }
            }
            .setNegativeButton("Cancel"){dialog,_->
                 dialog.dismiss()
            }.show()
    }
    private fun saveImagetoInternalStorage(bitmap:Bitmap) : String{
        val wrapper = ContextWrapper(applicationContext)
        var file=wrapper.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE)
        file= File(file,"${UUID.randomUUID()}.jpg")

        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream)
            stream.flush()
            stream.close()
        }catch (e: IOException){
            e.printStackTrace()
        }
        return file.absolutePath
    }
    private fun customItemsDialog(title:String,itemsList:List<String>,selection:String){
        customListDialog=Dialog(this)
        val binding: DialogCustomListBinding = DialogCustomListBinding.inflate(layoutInflater)
        customListDialog.setContentView(binding.root)
        binding.listTitle.text=title
        binding.rvList.layoutManager= LinearLayoutManager(this)

        val adapter= CustomListAdapter(this,null,itemsList,selection)
        binding.rvList.adapter=adapter
        customListDialog.show()


    }
    private fun displayListType(){
        customItemsDialog("Type",Constants.dishTypes(),Constants.DISH_TYPE)
    }
    private fun displayCategoryType(){
        customItemsDialog("Categories",Constants.dishCategories(),Constants.DISH_CATEGORY)
    }
    private fun displayCookingTime(){
        customItemsDialog("CookingTime",Constants.dishCookingTime(),Constants.DISH_COOKING_TIME)
    }
    fun selectedListItem(item:String,selection: String){
        when(selection){
                Constants.DISH_TYPE->{
                    binding.edtType.setText(item)
                    customListDialog.dismiss()
                }
                Constants.DISH_COOKING_TIME->{
                    binding.edtCookingTime.setText(item)
                    customListDialog.dismiss()
                }
                Constants.DISH_CATEGORY->{
                    binding.edtcategory.setText(item)
                    customListDialog.dismiss()
                }
        }
    }
    companion object{
        private const val CAMERA=1
        private const val GALLERY=2
        private const val IMAGE_DIRECTORY="FAVDishImages"
    }



}