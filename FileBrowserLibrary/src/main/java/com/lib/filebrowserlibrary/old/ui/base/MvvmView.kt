package com.lib.filebrowserlibrary.old.ui.base

import android.content.Context
import androidx.annotation.StringRes

interface MvvmView {
    fun showLoading()

    fun hideLoading()

    fun onError(@StringRes resId: Int)

    fun onError(message: String?)

    fun showMessage(message: String?)

    fun showMessage(@StringRes resId: Int)

    fun isNetworkConnected(): Boolean

    fun hideKeyboard()

    fun getmContext(): Context?}
