package com.lib.filebrowserlibrary.ui.base

import android.content.Context
import android.util.Log
import android.view.accessibility.AccessibilityManager
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.lib.filebrowserlibrary.R

abstract class BaseActivity<T : BaseViewModel> : AppCompatActivity(){

    protected lateinit var vieModel: T

    protected abstract fun setUp()

    fun hideAppBar(){
        if (supportActionBar!=null)
            supportActionBar!!.hide()
    }
    open fun activeHomeBackButton() {
        if (supportActionBar != null && supportActionBar!!.isShowing) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun showSnackBar(message: String) {
        val snackbar = Snackbar.make(
            findViewById(R.id.content),
            message, Snackbar.LENGTH_LONG
        )
        val sbView = snackbar.view
        val textView = sbView
            .findViewById<TextView>(R.id.snackbar_text)
        textView.setTextColor(ContextCompat.getColor(this, R.color.white))
        try {
            val accManagerField =
                BaseTransientBottomBar::class.java.getDeclaredField("mAccessibilityManager")
            accManagerField.isAccessible = true
            val accManager =
                accManagerField[snackbar] as AccessibilityManager
            val isEnabledField =
                AccessibilityManager::class.java.getDeclaredField("mIsEnabled")
            isEnabledField.isAccessible = true
            isEnabledField.setBoolean(accManager, false)
            accManagerField[snackbar] = accManager
        } catch (e: Exception) {
            Log.d("Snackbar", "Reflection error: $e")
        }
        snackbar.show()
    }

    open fun onError(message: String?) {
        if (message != null) {
            showSnackBar(message)
        } else {
            showSnackBar(getString(R.string.some_error))
        }
    }

    open fun onError(@StringRes resId: Int) {
        onError(getString(resId))
    }


    open fun getMContext(): Context? {
        return applicationContext
    }
}