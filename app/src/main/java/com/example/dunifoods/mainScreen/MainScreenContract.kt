package com.example.dunifoods.mainScreen

import com.example.dunifoods.model.Food
import com.example.dunifoods.utils.BasePresenter
import com.example.dunifoods.utils.BaseView

interface MainScreenContract {


    interface Presenter : BasePresenter<View> {

        fun firstRun ()
        fun onSearchFood(filter :String)
        fun onAddNewFood( newFood: Food)
        fun onDeleteAllClicked()
        fun onUpdateFood(food: Food, pos :Int)
        fun onDeleteFood(food: Food , pos :Int)

    }


    interface View : BaseView {

        fun showFoods (data :List<Food>)
        fun refreshFood (data: List<Food>)
        fun addNewFood(food: Food)
        fun deleteFood(oldFood: Food, pos: Int)
        fun editingFood(editingFood: Food , pos :Int)
        fun deleteAllFood(editingFood: Food , pos :Int)
    }

}