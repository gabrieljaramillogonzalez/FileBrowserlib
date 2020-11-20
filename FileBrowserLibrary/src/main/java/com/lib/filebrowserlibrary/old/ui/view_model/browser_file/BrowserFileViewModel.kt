package com.lib.filebrowserlibrary.old.ui.view_model.browser_file

import com.lib.filebrowserlibrary.old.ui.base.BaseViewModel
import com.lib.filebrowserlibrary.old.ui.view.browser_file.BrowserFileV

open class BrowserFileViewModel <V : BrowserFileV>: BaseViewModel<V>() ,BrowserFileVM<V>{

    override fun onAttach(mvvmView: V) {
        super.onAttach(mvvmView)
    }
}