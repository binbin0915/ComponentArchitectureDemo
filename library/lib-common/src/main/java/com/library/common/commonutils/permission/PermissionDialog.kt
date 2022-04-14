package com.library.common.commonutils.permission

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.viewbinding.ViewBinding
import com.library.common.R
import com.library.common.databinding.CustomDialogLayoutBinding
import com.permissionx.guolindev.dialog.RationaleDialog

/**
 * 自定义权限弹框
 */
class PermissionDialog(
    context: Context, private val message: String, private val permissions: List<String>
) : RationaleDialog(context, R.style.CustomDialog) {
    lateinit var viewBinding: ViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = CustomDialogLayoutBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        (viewBinding as CustomDialogLayoutBinding).messageText.text = message
        (viewBinding as CustomDialogLayoutBinding).bodyItem.text = "测试"
        window?.let {
            val param = it.attributes
            val width = (context.resources.displayMetrics.widthPixels * 0.8).toInt()
            val height = param.height
            it.setLayout(width, height)
        }
    }

    override fun getNegativeButton(): View {
        return (viewBinding as CustomDialogLayoutBinding).negativeBtn
    }

    override fun getPositiveButton(): View {
        return (viewBinding as CustomDialogLayoutBinding).positiveBtn
    }

    override fun getPermissionsToRequest(): List<String> {
        return permissions;
    }
}
