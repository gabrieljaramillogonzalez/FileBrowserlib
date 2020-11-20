package com.lib.filebrowserlibrary.old.ui.base

interface MvvmViewModel <V : MvvmView>{

    fun onAttach(mvpView: V)

    fun onDetach()
}
