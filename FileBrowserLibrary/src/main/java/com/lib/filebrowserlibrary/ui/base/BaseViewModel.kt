package com.lib.filebrowserlibrary.ui.base

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.ViewModel


open class BaseViewModel : ViewModel(){

    private lateinit var context : Context
    private lateinit var bundle: Bundle

    open fun onAttach(bundle: Bundle,context: Context){
        this.context = context
        this.bundle = bundle
    }

    open fun getMContext():Context{
        return context
    }

    open fun getMBundle():Bundle{
        return bundle
    }
}