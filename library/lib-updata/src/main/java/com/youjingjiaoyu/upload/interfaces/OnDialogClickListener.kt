package com.youjingjiaoyu.upload.interfaces

import android.content.DialogInterface

/**
 * @author wangkai
 */
interface OnDialogClickListener {
    fun onOkClick(dialog: DialogInterface?)
    fun onCancelClick(dialog: DialogInterface?)
}