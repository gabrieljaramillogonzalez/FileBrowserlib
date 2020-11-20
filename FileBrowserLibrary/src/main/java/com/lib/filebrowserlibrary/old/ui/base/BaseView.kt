package com.lib.filebrowserlibrary.old.ui.base

import android.annotation.TargetApi
import android.app.ProgressDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.view.MenuItem
import android.view.View
import android.view.accessibility.AccessibilityManager
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.lib.filebrowserlibrary.R
import com.lib.filebrowserlibrary.old.ui.utils.CommonUtils
import com.lib.filebrowserlibrary.old.ui.utils.NetworkUtils

abstract class BaseView<T : MvvmViewModel<MvvmView>> :AppCompatActivity(),MvvmView {

    private lateinit var progressDialog : ProgressDialog
    lateinit var viewModel : T
    private var hasInputErrorFocused = true

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        setup()
    }

    override fun onOptionsItemSelected(itemMenu: MenuItem): Boolean {
        when (itemMenu.itemId){
            android.R.id.home -> finishActivity()
        }
        return super.onOptionsItemSelected(itemMenu)
    }

    @TargetApi(Build.VERSION_CODES.M)
    open fun requestPermissionsSafely(permissions: Array<String?>?,requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions!!, requestCode)
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    open fun hasPermission(permission: String?): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||checkSelfPermission(permission!!) == PackageManager.PERMISSION_GRANTED
    }


    private fun showSnackBar(message : String){
        val snackBar = Snackbar.make(findViewById(android.R.id.content), message,Snackbar.LENGTH_LONG)
        val sbView = snackBar.view
        val textView = sbView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.setTextColor(ContextCompat.getColor(this,R.color.white))
        val accManagerField = BaseTransientBottomBar::class.java.getDeclaredField("mAccessibilityManager")
        accManagerField.isAccessible = true
        val accManager = accManagerField.get(snackBar)
        val isEnabledField = AccessibilityManager::class.java.getDeclaredField("mIsEnabled")
        isEnabledField.isAccessible = true
        isEnabledField.setBoolean(accManager,false)
        accManagerField.set(snackBar,accManager)
        snackBar.show()
    }

    override fun onError(message : String?){
        if (message != "")
            showSnackBar(message!!)
        else
            showSnackBar(getString(R.string.empty_images_list))
    }

    override fun onError(@StringRes resId : Int){
        onError(getString(resId))
    }

    override fun showMessage(message : String?  ){
        if (message != "")
            Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(this,getString(R.string.some_error),Toast.LENGTH_SHORT).show()
    }

    override fun showMessage(@StringRes resId : Int){
        showMessage(getString(resId))
    }

    override fun isNetworkConnected(): Boolean {
        return NetworkUtils.isNetworkConnected(applicationContext)
    }

    protected fun setInputError(view: View,@StringRes resString: Int){
        if (view is TextInputLayout)
            setTextInputError(view,resString)
        else if (view is TextView)
            setTextViewError(view, resString)
    }

    private fun setTextViewError(view: TextView, resString: Int) {
        view.setText(resString)
        view.requestFocus()
        view.visibility = View.VISIBLE
    }

    private fun setTextInputError(view: TextInputLayout, resString: Int) {
        if (!hasInputErrorFocused) {
            hasInputErrorFocused = true
            view.requestFocus()
        }
        view.isErrorEnabled = true
        view.error = getString(resString)
    }

    protected fun disableInputError(view: View){
        if (view is TextInputLayout)
            disableTextInputError(view)
        else if (view is TextView)
            disableTextViewError(view)
    }

    private fun disableTextViewError(view: View) {
        view.visibility = View.GONE
    }

    private fun disableTextInputError(view: TextInputLayout) {
        view.error = null
        view.isErrorEnabled = false
    }

    override fun hideKeyboard(){
        val view = this.currentFocus
        if (view?.id!=0){
            val imm : InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view?.windowToken, 0)
        }
    }

    fun hideAppBar(){
        supportActionBar?.hide()
    }

    fun activeHomeBackButton(){
        if (supportActionBar!!.isShowing)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun showLoading(){
        hideLoading()
        progressDialog = CommonUtils.showLoadingDialog(this)
    }
    
    override fun hideLoading(){
        if (progressDialog.isShowing)
            progressDialog.cancel()
    }

    private fun finishActivity() : Boolean{
        finish()
        return true
    }

    override fun onDestroy() {
        if (progressDialog.isShowing)
            progressDialog.dismiss()
        viewModel.onDetach()
        super.onDestroy()
    }

    override fun getmContext(): Context? {
        return applicationContext
    }

    protected abstract fun setup()
}