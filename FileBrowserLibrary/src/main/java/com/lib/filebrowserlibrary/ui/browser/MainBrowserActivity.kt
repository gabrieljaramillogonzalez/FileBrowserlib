package com.lib.filebrowserlibrary.ui.browser

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.lib.filebrowserlibrary.R
import com.lib.filebrowserlibrary.data.core.ConstantsLib
import com.lib.filebrowserlibrary.databinding.ActivityBrowserFileBinding
import com.lib.filebrowserlibrary.ui.base.BaseActivity
import com.lib.filebrowserlibrary.utils.CheckPermissionUtil
import com.lib.filebrowserlibrary.utils.CommonUtils
import com.lib.filebrowserlibrary.utils.PermissionUtil
import kotlinx.android.synthetic.main.activity_browser_file.*
import java.io.File

class MainBrowserActivity : BaseActivity<MainBrowserVieModel>() , FileAdapter.FileListener{

    private lateinit var viewModel : MainBrowserVieModel
    private val fileAdapter = FileAdapter(this)
    private lateinit var mGallery: Menu

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (!viewModel.isSingleImage())
            menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val bundle = Bundle()
        val listString = CommonUtils.toJson(viewModel.getListPath())
        bundle.putString(ConstantsLib.FilePathKey, listString)
        backToLastActivity(bundle)
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityBrowserFileBinding>(this,R.layout.activity_browser_file)
        viewModel = ViewModelProviders.of(this).get(MainBrowserVieModel::class.java)
        binding.viewModel = viewModel
        binding.LibFileBrowserIvBack.setOnClickListener { viewModel.back() }
        binding.executePendingBindings()
        checkReadPermission()
    }

    private fun checkReadPermission(){
        CheckPermissionUtil.checkReadSd( this, object : PermissionUtil.Companion.ReqPermissionCallback {
            override fun onResult(success: Boolean) {
                if (success)
                    configAttach()
                else
                    finish()
            }
        })
    }

    override fun onRequestPermissionsResult( requestCode: Int, permissions: Array<String>, grantResults: IntArray ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionUtil.onRequestPermissionResult( this, requestCode, permissions, grantResults )
    }

    fun configAttach(){
        viewModel.onAttach(intent.extras!!,this.baseContext)
        setUp()
    }

    override fun setUp() {
        LibFileBrowser_rvItems.apply {
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
            adapter = fileAdapter
        }
        viewModel.listFile.observe(this, Observer <List<File>>{
                list->fileAdapter.setList(list)
        })
        viewModel.isShowEmpty.observe(this, Observer<Boolean>{
            LibFileBrowser_tvEmptyList.visibility = if (it) View.VISIBLE else View.GONE
        })
        viewModel.isBackDirectory.observe(this, Observer<Boolean>{
            LibFileBrowser_ivBack.visibility = if (it) View.VISIBLE else View.INVISIBLE
        })
        viewModel.isLoading.observe(this, Observer<Boolean>{
            LibFileBrowser_rvProgress.visibility = if (it) View.VISIBLE else View.GONE
        })
        viewModel.directionalityRoot.observe(this, Observer<String> {
            LibFileBrowser_tvSourceDirectory.text = it
        })
    }

    override fun onBackPressed() {
        if (viewModel.getSizeListOfFileList() > 1)
            viewModel.back()
        else
            super.onBackPressed()
    }

    override fun isSingleImage(): Boolean {
        return viewModel.isSingleImage()
    }

    override fun openFolder(file: File) {
        viewModel.openFolder(file)
    }

    override fun isContentItem(file: File): Boolean {
        return viewModel.isContentItem(file)
    }

    override fun removeListItem(file: File) {
        viewModel.removeListItem(file)
    }

    override fun setListItem(file: File) {
        viewModel.setListItem(file)
    }

    override fun addFileAlbums(file: File) {
        val bundle = Bundle()
        bundle.putString(ConstantsLib.FilePathKey, file.absolutePath)
        backToLastActivity(bundle)
    }

    fun backToLastActivity(bundle: Bundle) {
        val intent = Intent()
        intent.putExtras(bundle)
        this.setResult(RESULT_OK, intent)
        this.finish()
    }
}