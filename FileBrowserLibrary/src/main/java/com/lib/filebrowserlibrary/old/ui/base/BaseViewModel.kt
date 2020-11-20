package com.lib.filebrowserlibrary.old.ui.base

open class BaseViewModel <V : MvvmView> : MvvmViewModel<V>{

    private lateinit var view : V

    override fun onAttach(mvvmView: V) {
        view = mvvmView
    }

    override fun onDetach() {

    }

    fun getView():V{
        return view
    }
}