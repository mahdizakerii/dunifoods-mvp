package com.example.dunifoods.mainScreen

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dunifoods.model.Food
import com.example.dunifoods.model.MyDatabase
import com.example.dunifoods.utils.showToast
import example.dunifoods.databinding.ActivityMainBinding
import example.dunifoods.databinding.DialogAddNewItemBinding
import example.dunifoods.databinding.DialogDeleteItemBinding
import example.dunifoods.databinding.DialogUpdateItemBinding

// implement Room :
// 1. Entity
// 2. Dao
// 3. database


class MainActivity : AppCompatActivity(), FoodAdapter.FoodEvents, MainScreenContract.View {
    private lateinit var binding: ActivityMainBinding
    private lateinit var myAdapter: FoodAdapter
    private lateinit var presenter : MainScreenContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        presenter = MainScreenPresenter(MyDatabase.getDatabase(this).foodDao)

        val sharedPreferences = getSharedPreferences("duniFood", MODE_PRIVATE)
        if (sharedPreferences.getBoolean("first_run", true)) {
            presenter.firstRun()
            sharedPreferences.edit().putBoolean("first_run", false).apply()
        }

        presenter.onAttach(this)

        binding.btnRemoveAllFoods.setOnClickListener {
            presenter.onDeleteAllClicked()
        }

        binding.btnAddNewFood.setOnClickListener {
            addNewFood()
        }

        binding.edtSearch.addTextChangedListener { editTextInput ->
            presenter.onSearchFood(editTextInput.toString())
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDetach()
    }
    private fun addNewFood() {

        val dialog = AlertDialog.Builder(this).create()

        val dialogBinding = DialogAddNewItemBinding.inflate(layoutInflater)
        dialog.setView(dialogBinding.root)
        dialog.setCancelable(true)
        dialog.show()

        dialogBinding.dialogBtnDone.setOnClickListener {

            if (
                dialogBinding.dialogEdtNameFood.length() > 0 &&
                dialogBinding.dialogEdtFoodCity.length() > 0 &&
                dialogBinding.dialogEdtFoodPrice.length() > 0 &&
                dialogBinding.dialogEdtFoodDistance.length() > 0
            ) {

                val txtName = dialogBinding.dialogEdtNameFood.text.toString()
                val txtPrice = dialogBinding.dialogEdtFoodPrice.text.toString()
                val txtDistance = dialogBinding.dialogEdtFoodDistance.text.toString()
                val txtCity = dialogBinding.dialogEdtFoodCity.text.toString()
                val txtRatingNumber: Int = (1..150).random()
                val ratingBarStar: Float = (1..5).random().toFloat()

                val randomForUrl = (1 until 12).random()
                val urlPic = "$BASE_URL_IMAGE$randomForUrl.jpg"

                val newFood = Food(
                    txtSubject = txtName,
                    txtPrice = txtPrice,
                    txtDistance = txtDistance,
                    txtCity = txtCity,
                    urlImage = urlPic,
                    numOfRating = txtRatingNumber,
                    rating = ratingBarStar
                )

                presenter.onAddNewFood(newFood)


            } else {
                showToast("لطفا همه مقادیر را وارد کنید :")
            }

        }


    }


    override fun onFoodClicked(food: Food, position: Int) {

        val dialog = AlertDialog.Builder(this).create()
        val updateDialogBinding = DialogUpdateItemBinding.inflate(layoutInflater)

        updateDialogBinding.dialogEdtNameFood.setText(food.txtSubject)
        updateDialogBinding.dialogEdtFoodCity.setText(food.txtCity)
        updateDialogBinding.dialogEdtFoodPrice.setText(food.txtPrice)
        updateDialogBinding.dialogEdtFoodDistance.setText(food.txtDistance)

        dialog.setView(updateDialogBinding.root)
        dialog.setCancelable(true)
        dialog.show()

        updateDialogBinding.dialogUpdateBtnCancel.setOnClickListener {
            dialog.dismiss()
        }

        updateDialogBinding.dialogUpdateBtnDone.setOnClickListener {

            if (
                updateDialogBinding.dialogEdtNameFood.length() > 0 &&
                updateDialogBinding.dialogEdtFoodCity.length() > 0 &&
                updateDialogBinding.dialogEdtFoodPrice.length() > 0 &&
                updateDialogBinding.dialogEdtFoodDistance.length() > 0
            ) {

                val txtName = updateDialogBinding.dialogEdtNameFood.text.toString()
                val txtPrice = updateDialogBinding.dialogEdtFoodPrice.text.toString()
                val txtDistance = updateDialogBinding.dialogEdtFoodDistance.text.toString()
                val txtCity = updateDialogBinding.dialogEdtFoodCity.text.toString()

                // create new food to add to recycler view
                val newFood = Food(
                    id = food.id,
                    txtSubject = txtName,
                    txtPrice = txtPrice,
                    txtDistance = txtDistance,
                    txtCity = txtCity,
                    urlImage = food.urlImage,
                    numOfRating = food.numOfRating,
                    rating = food.rating
                )

                presenter.onUpdateFood(newFood , position)
                dialog.dismiss()

            } else {
                showToast("لطفا همه مقادیر را وارد کن :")
            }

        }

    }
    override fun onFoodLongClicked(food: Food, pos: Int) {

        val dialog = AlertDialog.Builder(this).create()
        val dialogDeleteBinding = DialogDeleteItemBinding.inflate(layoutInflater)
        dialog.setView(dialogDeleteBinding.root)
        dialog.setCancelable(true)
        dialog.show()

        dialogDeleteBinding.dialogBtnDeleteCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialogDeleteBinding.dialogBtnDeleteSure.setOnClickListener {

           presenter.onDeleteFood(food , pos)
            dialog.dismiss()

        }


    }

    override fun showFoods(data: List<Food>) {
         myAdapter = FoodAdapter(ArrayList(data) , this)
        binding.recyclerMain.adapter = myAdapter
        binding.recyclerMain.layoutManager = LinearLayoutManager(this , RecyclerView.VERTICAL , false )
    }
    override fun refreshFood(data: List<Food>) {
        myAdapter.setData(ArrayList(data))
    }
    override fun addNewFood(food: Food) {
        myAdapter.addFood(food)
    }
    override fun deleteFood(oldFood: Food, pos: Int) {
        myAdapter.removeFood(oldFood , pos)
    }
    override fun editingFood(editingFood: Food, pos: Int) {
        myAdapter.updateFood(editingFood , pos)
    }
    override fun deleteAllFood(editingFood: Food, pos: Int) {

    }

}