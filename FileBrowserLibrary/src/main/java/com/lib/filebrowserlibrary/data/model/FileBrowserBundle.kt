package com.lib.filebrowserlibrary.data.model

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import com.lib.filebrowserlibrary.R
import com.lib.filebrowserlibrary.data.enums.TypeFileBrowser
import java.io.File
import java.io.FileNotFoundException

class FileBrowserBundle {
    private var typeFile = TypeFileBrowser.ALL
    private var isSingle : Boolean = false
    private var width : Double = 0.0
    private var height : Double = 0.0
    private var maxWidth : Double = 0.0
    private var maxHeight : Double = 0.0
    private var minWidth : Double = 0.0
    private var minHeight : Double = 0.0
    private var maxSize : Double = 0.0
    private var minSize : Double = 0.0

    fun getTypeFile() = typeFile
    fun setTypeFile(typeFile : TypeFileBrowser){
        this.typeFile= typeFile
    }
    fun isSingle() = isSingle
    fun setIsSingle(isSingle : Boolean){
        this.isSingle= isSingle
    }
    fun getWidth() = width
    fun setWidth(width : Double){
        this.width = width
    }
    fun getHeight() = height
    fun setHeight(height : Double){
        this.height = height
    }
    fun getMaxWidth() = maxWidth
    fun getMaxWidth(maxWidth : Double){
        this.maxWidth = maxWidth
    }
    fun getMaxHeight() = maxHeight
    fun getMaxHeight(maxHeight : Double){
        this.maxHeight = maxHeight
    }
    fun getMinWidth() = minWidth
    fun getMinWidth(minWidth : Double){
        this.minWidth = minWidth
    }
    fun getMinHeight() = minHeight
    fun getMinHeight(minHeight : Double){
        this.minHeight = minHeight
    }
    fun getMaxSize() = maxSize
    fun getMaxSize(maxSize : Double){
        this.maxSize = maxSize
    }
    fun getMinSize() = minSize
    fun getMinSize(minSize : Double){
        this.minSize = minSize
    }

    fun evaluateFileType(file : File , context: Context): Boolean{
        var result = false
        when(typeFile){
            TypeFileBrowser.ALL -> result = true
            TypeFileBrowser.IMAGE -> result = (file.name.toLowerCase().endsWith(".jpg") || file.name.toLowerCase().endsWith(".png") || file.name.toLowerCase().endsWith(".jpeg"))
            TypeFileBrowser.JPEG -> result = (file.name.toLowerCase().endsWith(".jpeg"))
            TypeFileBrowser.JPG -> result = (file.name.toLowerCase().endsWith(".jpg"))
            TypeFileBrowser.PNG -> result = (file.name.toLowerCase().endsWith(".png"))
            TypeFileBrowser.PDF -> result = (file.name.toLowerCase().endsWith(".pdf"))
            TypeFileBrowser.SPHERICAL ->{
                if ((file.name.toLowerCase().endsWith(".jpg") || file.name.toLowerCase().endsWith(".png") || file.name.toLowerCase().endsWith(".jpeg"))){
                    var imageHeight = 0.0
                    var imageWidth = 0.0
                    try {
                        val uri = Uri.fromFile(file)
                        val options = BitmapFactory.Options()
                        options.inJustDecodeBounds = true
                        BitmapFactory.decodeStream(
                            context.contentResolver.openInputStream(uri),
                            null,
                            options
                        )
                        imageHeight = options.outHeight.toDouble()
                        imageWidth = options.outWidth.toDouble()
                        result = imageWidth / imageHeight == (width / height)
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    }
                }
            }
            else -> result = false
        }
        return result
    }

    fun typeString(context: Context):String{
        var type = String()
        when(typeFile){
            TypeFileBrowser.ALL -> type = context.getString(R.string.empty_file_list)
            TypeFileBrowser.IMAGE -> type = context.getString(R.string.empty_images_list)
            TypeFileBrowser.JPEG -> type = context.getString(R.string.empty_JPEG_list)
            TypeFileBrowser.JPG -> type = context.getString(R.string.empty_JPG_list)
            TypeFileBrowser.PNG -> type = context.getString(R.string.empty_PNG_list)
            TypeFileBrowser.PDF -> type = context.getString(R.string.empty_documents_list)
            TypeFileBrowser.SPHERICAL -> type = context.getString(R.string.empty_SPHERICAL_list)
            else -> context.getString(R.string.empty_images_list)
        }
        return type
    }
}