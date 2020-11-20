package com.lib.filebrowserlibrary.utils

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.text.TextUtils
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.lib.filebrowserlibrary.R
import java.util.*

class PermissionUtil {
    companion object{

        private val permissionReqList: MutableList<PermissionReq> = ArrayList()

        fun hasPermission(activity: Activity?, permission: String?): Boolean {
            return (ActivityCompat.checkSelfPermission(activity!!, permission!!) == PackageManager.PERMISSION_GRANTED)
        }

        fun checkPermission( activity: Activity, permission: String, reqCode: Int, reqReason: CharSequence?, rejectedMsg: CharSequence?,callback: ReqPermissionCallback) {
            if (hasPermission(activity, permission)) {
                activity.window.decorView.post { callback.onResult(true) }
            } else {
                val shouldShowReqReason = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
                val req = PermissionReq(activity, permission, reqCode, reqReason, rejectedMsg, callback)
                if (shouldShowReqReason) {
                    showReqReason(req)
                } else {
                    reqPermission(req)
                }
            }
        }

        private fun showReqReason(req: PermissionReq) {
            AlertDialog.Builder(req.activity, R.style.CustomAlertDialog)
                .setCancelable(false)
                .setMessage(req.reqReason)
                .setPositiveButton(R.string.grant,
                    DialogInterface.OnClickListener { dialog, which -> reqPermission(req) })
                .setNegativeButton(R.string.cancel,
                    DialogInterface.OnClickListener { dialog, which -> req.callback.onResult(false) })
                .show()
        }

        private fun reqPermission(req: PermissionReq) {
            permissionReqList.add(req)
            ActivityCompat.requestPermissions(req.activity, arrayOf(req.permission),req.reqCode)
        }

        private fun showRejectedMsg(req: PermissionReq) {
            AlertDialog.Builder(req.activity, R.style.CustomAlertDialog)
                .setCancelable(false)
                .setMessage(req.rejectedMsg)
                .setPositiveButton(R.string.yes,
                    DialogInterface.OnClickListener { dialog, which -> openAppDetailSetting(req) })
                .setNegativeButton(R.string.no,
                    DialogInterface.OnClickListener { dialog, which ->
                        req.callback.onResult(false)
                        permissionReqList.remove(req)
                    })
                .show()
        }

        private fun openAppDetailSetting(req: PermissionReq) {
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            val uri =
                Uri.fromParts("package", req.activity.packageName, null)
            intent.data = uri
            req.activity.startActivityForResult(intent, req.reqCode)
        }

        fun onRequestPermissionResult( activity: Activity, requestCode: Int, permissions: Array<String>, grantResults: IntArray ) {
            var targetReq: PermissionReq? = null
            for (req in permissionReqList) {
                if (req.activity == activity && req.reqCode == requestCode && req.permission == permissions[0]
                ) {
                    targetReq = req
                    break
                }
            }
            if (targetReq != null) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    targetReq.callback.onResult(true)
                    permissionReqList.remove(targetReq)
                } else {
                    if (TextUtils.isEmpty(targetReq.rejectedMsg)) {
                        targetReq.callback.onResult(false)
                        permissionReqList.remove(targetReq)
                    } else {
                        showRejectedMsg(targetReq)
                    }
                }
            }
        }

        fun onActivityResult( activity: Activity, reqCode: Int) {
            var targetReq: PermissionReq? = null
            for (req in permissionReqList) {
                if (req.activity == activity && req.reqCode == reqCode) {
                    targetReq = req
                    break
                }
            }
            if (targetReq != null) {
                targetReq.callback.onResult(hasPermission(targetReq.activity, targetReq.permission))
                permissionReqList.remove(targetReq)
            }
        }

        interface ReqPermissionCallback {
            fun onResult(success: Boolean)
        }

        private class PermissionReq internal constructor(
            val activity: Activity,
            val permission: String,
            val reqCode: Int,
            val reqReason: CharSequence?,
            val rejectedMsg: CharSequence?,
            val callback: ReqPermissionCallback
        )
    }
}