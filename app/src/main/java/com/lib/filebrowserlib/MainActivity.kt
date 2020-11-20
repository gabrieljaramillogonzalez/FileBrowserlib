package com.lib.filebrowserlib

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lib.filebrowserlibrary.data.enums.TypeFileBrowser
import com.lib.filebrowserlibrary.data.model.FileBrowserBundle
import com.lib.filebrowserlibrary.ui.browser.MainBrowserActivity
import com.lib.filebrowserlibrary.utils.CommonUtils

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bundle = Bundle()
        val fileBrowserBundle = FileBrowserBundle()
        fileBrowserBundle.setIsSingle(false)
        fileBrowserBundle.setTypeFile(TypeFileBrowser.IMAGE)
        bundle.putString("fileBrowser",CommonUtils.toJson(fileBrowserBundle))
        val intent = Intent(this , MainBrowserActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
}