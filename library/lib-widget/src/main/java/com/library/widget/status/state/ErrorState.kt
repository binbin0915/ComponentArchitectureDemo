package com.library.widget.status.state

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import com.library.widget.R
import com.library.widget.status.MultiState
import com.library.widget.status.MultiStateContainer
import com.library.widget.status.MultiStatePage

/**
 * 作用描述：错误状态
 *
 * 创建时间：2022/03/18
 * @author：WangKai
 */
class ErrorState : MultiState() {

    private lateinit var tvErrorMsg: TextView
    private lateinit var imgError: ImageView

    override fun onCreateMultiStateView(
        context: Context,
        inflater: LayoutInflater,
        container: MultiStateContainer
    ): View {
        return inflater.inflate(R.layout.widget_mult_state_error, container, false)
    }

    override fun onMultiStateViewCreate(view: View) {
        tvErrorMsg = view.findViewById(R.id.tv_error_msg)
        imgError = view.findViewById(R.id.img_error)

        setErrorMsg(MultiStatePage.config.errorMsg)
        setErrorIcon(MultiStatePage.config.errorIcon)
    }


    fun setErrorMsg(errorMsg: String) {
        tvErrorMsg.text = errorMsg
    }

    fun setErrorIcon(@DrawableRes errorIcon: Int) {
        imgError.setImageResource(errorIcon)
    }
}