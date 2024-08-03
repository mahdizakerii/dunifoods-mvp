package com.example.dunifoods.utils

interface BasePresenter<T> {
    fun onAttach(view: T)
    fun onDetach()
}

interface BaseView {

}