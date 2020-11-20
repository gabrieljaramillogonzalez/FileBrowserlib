package com.lib.filebrowserlibrary.ui.browser

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.lib.filebrowserlibrary.data.core.ConstantsLib
import com.lib.filebrowserlibrary.data.model.FileBrowserBundle
import com.lib.filebrowserlibrary.ui.base.BaseViewModel
import com.lib.filebrowserlibrary.utils.CommonUtils
import kotlinx.coroutines.launch
import java.io.File

class MainBrowserVieModel : BaseViewModel(){

    val listFile : MutableLiveData<List<File>> = MutableLiveData()
    val isLoading = MutableLiveData<Boolean>()
    val isShowEmpty = MutableLiveData<Boolean>()
    val isBackDirectory = MutableLiveData<Boolean>()
    val directionalityRoot = MutableLiveData<String>()
    private var fileBrowser = FileBrowserBundle()
    private var listOfFileList = ArrayList<ArrayList<File>>()
    private var filesCorrectType = ArrayList<File>()
    private lateinit var filesRoot : Array<File>
    private val filesCorrectAlbum= ArrayList<File>()
    private val listPath = ArrayList<String>()
    private val pathsFile = HashSet<File>()

    override fun onAttach(bundle: Bundle,context: Context) {
        super.onAttach(bundle,context)
        getBundle()
        refresh()
    }

    private fun getBundle() {
        fileBrowser = CommonUtils.toObject<FileBrowserBundle>(getMBundle().getString(ConstantsLib.fileBrowser),FileBrowserBundle::class.java)!!
    }

    private fun refresh(){
        setLoading(true)
        viewModelScope.launch {
            getRootFile()
            addFilesCorrectType(filesRoot)
            setFilesCorrectAlbum()
            listFile.postValue(listOfFileList[listOfFileList.size-1])
            isShowEmpty.value = listOfFileList[listOfFileList.size-1].size==0
        }
        setLoading(false)
    }

    private fun setFilesCorrectAlbum() {
        filesCorrectAlbum.clear()
        for(file in filesRoot){
            for (f in pathsFile){
                if (f.absoluteFile.toString().startsWith(file.absolutePath)){
                    filesCorrectAlbum.add(file)
                    break
                }
            }
        }
        listOfFileList.add(filesCorrectAlbum)
    }

    private fun addFilesCorrectType(listFiles: Array<File>) {
        for (file in listFiles){
            if (file.listFiles()!=null&& file.listFiles().isNotEmpty()){
                addFilesCorrectType(File(file.absolutePath).listFiles())
            }else if (fileBrowser.evaluateFileType(file, getMContext())){
                filesCorrectType.add(file)
                pathsFile.add(file.parentFile)
            }
        }
    }

    private fun getRootFile(){
        val files = Environment.getExternalStorageDirectory().absoluteFile.listFiles()
        val  filesAux = ArrayList<File>()
        if (files!=null){
            for (file in files){
                if (!file.isHidden && !file.name.startsWith("Android") && !file.name.startsWith("data") && !file.name.startsWith("log"))
                    filesAux.add(file)
            }
            this.filesRoot = filesAux.toTypedArray()
            isBackDirectory.value = false
        }
        directionalityRoot.value = filesAux[0].parent
    }

    fun openFolder(file: File){
        val arrayFile = ArrayList(listOf<File>(*file.listFiles()))
        var arrayList = ArrayList<File>()
        for (file in arrayFile){
            if(arrayFile.size>0)
                arrayList.add(file)
            else if (fileBrowser.evaluateFileType(file,getMContext()))
                arrayList.add(file)
        }
        arrayList = setFilesCorrectAlbum(arrayList)
        listOfFileList.add(arrayList)
        listFile.postValue(listOfFileList[listOfFileList.size-1])
        directionalityRoot.value = file.absolutePath
        isBackDirectory.value = listOfFileList.size>1
        isShowEmpty.value = listFile.value?.isEmpty()
    }

    private fun setFilesCorrectAlbum(arrayList: ArrayList<File>) :  ArrayList<File>{
        val listFiles = ArrayList<File>()
        for (file in arrayList){
            for (file1 in filesCorrectType){
                if (file1.absoluteFile.toString().startsWith(file.absolutePath)){
                    listFiles.add(file)
                    break
                }
            }
        }
        return listFiles
    }

    fun back(){
        if (listOfFileList.size>1){
            listOfFileList.removeAt(listOfFileList.size-1)
            listFile.postValue(listOfFileList[listOfFileList.size-1])
            directionalityRoot.value = listOfFileList[listOfFileList.size-1][0].parent
        }
        isBackDirectory.value = listOfFileList.size>1
        isShowEmpty.value = listFile.value?.isEmpty()
    }

    fun getListPath() : Array<Any>? {
        return listPath.toArray()
    }

    fun getSizeListOfFileList():Int{
        return listOfFileList.size
    }

    fun isContentItem(file: File): Boolean {
        return  listPath.contains(file.absolutePath)
    }

    fun removeListItem(file: File) {
        listPath.remove(file.absolutePath)
    }

    fun setListItem(file: File) {
        listPath.add(file.absolutePath)
    }

    private fun setLoading(value : Boolean){
        isLoading.value = value
    }

    fun isSingleImage(): Boolean {
        return fileBrowser.isSingle()
    }
}