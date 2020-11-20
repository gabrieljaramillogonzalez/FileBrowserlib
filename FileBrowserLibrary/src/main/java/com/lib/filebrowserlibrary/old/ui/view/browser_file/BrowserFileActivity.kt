package com.lib.filebrowserlibrary.old.ui.view.browser_file

import android.os.Bundle
import com.lib.filebrowserlibrary.R
import com.lib.filebrowserlibrary.old.ui.base.BaseView
import com.lib.filebrowserlibrary.old.ui.base.MvvmView
import com.lib.filebrowserlibrary.old.ui.view_model.browser_file.BrowserFileVM

open class BrowserFileActivity : BaseView<BrowserFileVM<MvvmView>>(), BrowserFileV {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browser_file)
        activeHomeBackButton()
        setup()
//        viewModel = BrowserFileViewModel<BrowserFileV>()
        viewModel.onAttach(this)
    }
    override fun setup() {
    }
}