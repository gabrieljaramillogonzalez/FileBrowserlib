package com.lib.filebrowserlibrary.utils

import android.Manifest
import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import com.lib.filebrowserlibrary.R

class CheckPermissionUtil {
    companion object{
        const val WRITE_SD_REQ_CODE = 201
        const val CAMERA_REQ_CODE = 202
        const val READ_SD_REQ_CODE = 203
        private const val LOCATION_PERMISSION_REQ_CODE = 200
        
        fun checkLocation( activity: Activity, callback : PermissionUtil.Companion.ReqPermissionCallback? ) {
            if (callback != null) {
                PermissionUtil.checkPermission(
                    activity,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    LOCATION_PERMISSION_REQ_CODE,
                    activity.getText(R.string.location_permission_description_message),
                    activity.getText(R.string.location_permission_go_settings_message),
                    callback
                )
            }
        }
    
        fun checkWriteSd( activity: Activity, callback: PermissionUtil.Companion.ReqPermissionCallback?) {
            if (callback != null) {
                PermissionUtil.checkPermission(
                    activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    WRITE_SD_REQ_CODE,
                    activity.getText(R.string.write_es_permission_description_message),
                    activity.getText(R.string.write_es_permission_go_settings_message),
                    callback
                )
            }
        }
    
        fun checkCamera(activity: Activity, callback: PermissionUtil.Companion.ReqPermissionCallback? ) {
            if (callback != null) {
                PermissionUtil.checkPermission(
                    activity,
                    Manifest.permission.CAMERA,
                    CAMERA_REQ_CODE,
                    activity.getText(R.string.message_rationale_camera_permission_get_photo),
                    activity.getText(R.string.camera_permission_not_allowed),
                    callback
                )
            }
        }
    
        fun checkReadSd(activity: Activity, callback: PermissionUtil.Companion.ReqPermissionCallback?) {
            if (callback != null) {
                PermissionUtil.checkPermission(
                    activity,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    READ_SD_REQ_CODE,
                    activity.getText(R.string.read_es_permission_description_message),
                    activity.getText(R.string.read_es_permission_go_settings_message),
                    callback
                )
            }
        }

        @RequiresApi(Build.VERSION_CODES.R)
        fun checkWriteSdR(activity: Activity, callback: PermissionUtil.Companion.ReqPermissionCallback?) {
            if (callback != null) {
                PermissionUtil.checkPermission(
                    activity,
                    Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                    READ_SD_REQ_CODE,
                    activity.getText(R.string.write_es_permission_description_message),
                    activity.getText(R.string.write_es_permission_go_settings_message),
                    callback
                )
            }
        }
    }
}
