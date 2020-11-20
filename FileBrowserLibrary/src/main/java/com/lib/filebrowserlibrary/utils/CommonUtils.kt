package com.lib.filebrowserlibrary.utils

import com.google.gson.GsonBuilder
import java.text.DecimalFormat

class CommonUtils {
    companion object{
        fun sizeFile(size : Long): String {
            val sizeFile : Float
            var textSize : String
            var decimalFormat = DecimalFormat("#.00")

            if (size <= 1024L * 1024L) {
                sizeFile = (size/1024L).toFloat()
                textSize = decimalFormat.format(sizeFile)+"KB"
            } else {
                sizeFile = (size / (1024L * 1024L)).toFloat()
                textSize = decimalFormat.format(sizeFile) + " MB"
            }
            return textSize
        }

        fun <T> toObject(json: String?, clazz: Class<*>?): T? {
            return if (json == null) null else GsonBuilder().create().fromJson(json, clazz) as T
        }

        fun <T : Any> toJson(myObject : T?): String? {
            return if (myObject == null) null else GsonBuilder().create().toJson(myObject, myObject.javaClass)
        }
    }
}