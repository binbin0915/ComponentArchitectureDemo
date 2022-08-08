package com.model.airpods.ui.widget

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.transition.TransitionManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.library.logcat.AppLog
import com.model.airpods.R
import com.model.airpods.databinding.AirpodsLayoutPopupBinding
import com.model.airpods.model.BatteryState
import com.model.airpods.model.ConnectionState
import com.model.airpods.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


/**
 * MD风格的底部弹窗
 */
class AnPodsDialog constructor(
    context: Context
) : BottomSheetDialog(context), CoroutineScope by MainScope() {

    /**
     * ConstraintLayout（约束布局）是Android Studio 2.2中主要的新增功能之一，
     * Android studio升级到2.3版本之后，不管是新建Activity或fragment，xml默认布局由RelativeLayout更改为ConstraintLayout了。
     * 但是ConstraintLayout远远比想象中的强大，不仅可以解决布局层层嵌套的缺点，还可以实现动画效果。
     *
     * ConstraintLayout 动画要new一个ConstraintSet变量用于存放view的改变状态
     */
    private var applyConstraintSet = ConstraintSet()

    /**
     * viewBinding
     */
    private var airpodsLayoutPopupBinding =
        AirpodsLayoutPopupBinding.inflate(LayoutInflater.from(getContext()))

    init {
        AppLog.log(TAG, "初始化耳机dialog")
        setContentView(airpodsLayoutPopupBinding.root)
        applyConstraintSet.clone(airpodsLayoutPopupBinding.anpodsPanel)
        window?.run {
            setType(
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                } else {
                    WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
                }
            )
            delegate?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                ?.setBackgroundResource(android.R.color.transparent)
        }
        airpodsLayoutPopupBinding.ivCase.tag = MODEL_AIR_PODS_NORMAL
        setOnShowListener {
            updateAirPodsModel("auto")

            val connectionState = airpodsLayoutPopupBinding.anpodsPanel.tag
            if (connectionState != null && (connectionState as ConnectionState).isConnected) {
                airpodsLayoutPopupBinding.ivCase.progress = 1f
                airpodsLayoutPopupBinding.ivCaseLeft.progress = 1f
                airpodsLayoutPopupBinding.ivCaseRight.progress = 1f

                airpodsLayoutPopupBinding.ivLeftPod.progress = 1f
                airpodsLayoutPopupBinding.ivRightPod.progress = 1f
                return@setOnShowListener
            }
            airpodsLayoutPopupBinding.ivCase.playAnimation()
            airpodsLayoutPopupBinding.ivCaseLeft.playAnimation()
            airpodsLayoutPopupBinding.ivCaseRight.playAnimation()

            airpodsLayoutPopupBinding.ivCaseBattery.playAnimation()
            airpodsLayoutPopupBinding.ivLeftPodBattery.playAnimation()
            airpodsLayoutPopupBinding.ivRightPodBattery.playAnimation()
            airpodsLayoutPopupBinding.ivLeftPod.playAnimation()
            airpodsLayoutPopupBinding.ivRightPod.playAnimation()
        }

        setOnDismissListener {
            val connectionState = airpodsLayoutPopupBinding.anpodsPanel.tag
            if (connectionState != null && (connectionState as ConnectionState).isConnected) {
                return@setOnDismissListener
            }
            airpodsLayoutPopupBinding.ivCase.progress = 0f
            airpodsLayoutPopupBinding.ivCaseLeft.progress = 0f
            airpodsLayoutPopupBinding.ivCaseRight.progress = 0f

            airpodsLayoutPopupBinding.ivCaseBattery.progress = 0f
            airpodsLayoutPopupBinding.tvCase.text = "-"
            airpodsLayoutPopupBinding.ivLeftPodBattery.progress = 0f
            airpodsLayoutPopupBinding.tvLeftPod.text = "-"
            airpodsLayoutPopupBinding.ivRightPodBattery.progress = 0f
            airpodsLayoutPopupBinding.tvRightPod.text = "-"
            airpodsLayoutPopupBinding.ivLeftPod.progress = 0f
            airpodsLayoutPopupBinding.ivRightPod.progress = 0f
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        context.displayChange().onEach {
            onBackPressed()
        }.launchIn(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        cancel()
    }

    override fun show() {
        super.show()
        //设置高度
        val bottomSheet =
            findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
        val originLayoutParams =
            (bottomSheet.layoutParams as CoordinatorLayout.LayoutParams).apply {
                height = ViewGroup.LayoutParams.WRAP_CONTENT
                gravity = Gravity.CENTER_HORIZONTAL
            }
        bottomSheet.layoutParams = originLayoutParams
        val bottomSheetBehavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun showCase() {
        TransitionManager.beginDelayedTransition(airpodsLayoutPopupBinding.anpodsPanel)
        applyConstraintSet.setVisibility(R.id.iv_case, ConstraintSet.VISIBLE)
        applyConstraintSet.setVisibility(R.id.iv_case_left, ConstraintSet.VISIBLE)
        applyConstraintSet.setVisibility(R.id.iv_case_right, ConstraintSet.VISIBLE)
        applyConstraintSet.setVisibility(R.id.tv_case, ConstraintSet.VISIBLE)
        applyConstraintSet.setVisibility(R.id.iv_case_battery, ConstraintSet.VISIBLE)

        applyConstraintSet.connect(
            R.id.iv_left_pod,
            ConstraintSet.END,
            R.id.iv_case,
            ConstraintSet.START
        )
        applyConstraintSet.connect(
            R.id.iv_right_pod,
            ConstraintSet.START,
            R.id.iv_case,
            ConstraintSet.END
        )
        applyConstraintSet.applyTo(airpodsLayoutPopupBinding.anpodsPanel)
    }

    private fun hideCase() {
        TransitionManager.beginDelayedTransition(airpodsLayoutPopupBinding.anpodsPanel)
        applyConstraintSet.setVisibility(R.id.iv_case, ConstraintSet.GONE)
        applyConstraintSet.setVisibility(R.id.iv_case_left, ConstraintSet.GONE)
        applyConstraintSet.setVisibility(R.id.iv_case_right, ConstraintSet.GONE)
        applyConstraintSet.setVisibility(R.id.tv_case, ConstraintSet.INVISIBLE)
        applyConstraintSet.setVisibility(R.id.iv_case_battery, ConstraintSet.INVISIBLE)

        applyConstraintSet.connect(
            R.id.iv_left_pod,
            ConstraintSet.END,
            R.id.iv_right_pod,
            ConstraintSet.START
        )
        applyConstraintSet.connect(
            R.id.iv_right_pod,
            ConstraintSet.START,
            R.id.iv_left_pod,
            ConstraintSet.END
        )
        applyConstraintSet.applyTo(airpodsLayoutPopupBinding.anpodsPanel)
    }

    fun updateConnectedDevice(connectionState: ConnectionState) {
        val state = airpodsLayoutPopupBinding.anpodsPanel.tag
        if (state == connectionState) return
        airpodsLayoutPopupBinding.anpodsPanel.tag = connectionState

        if (connectionState.isConnected) {
            airpodsLayoutPopupBinding.tvTitle.text = connectionState.deviceName
            updateAirPodsBattery(true)
        } else {
            airpodsLayoutPopupBinding.tvTitle.text =
                context.getString(R.string.airpods_tile_disconnected_title)
            updateAirPodsBattery(false)
        }
    }

    fun updateUI(it: BatteryState) {
        updateAirPodsBattery(false, isBatteryStateChange = true)

        updateAirPodsModel(it.model)

        if (it.caseBattery <= 10) {
            showCase()
            airpodsLayoutPopupBinding.tvCase.text =
                if (it.caseBattery == 10) "100%" else "${it.caseBattery * 10 + 5}%"
            airpodsLayoutPopupBinding.ivCaseBattery.progress = (it.caseBattery + 0.5f) / 10f

        } else {
            hideCase()
        }
        if (it.leftBattery <= 10) {
            airpodsLayoutPopupBinding.tvLeftPod.text =
                if (it.leftBattery == 10) "100%" else "${it.leftBattery * 10 + 5}%"
            airpodsLayoutPopupBinding.ivLeftPodBattery.progress = (it.leftBattery + 0.5f) / 10f

        } else {
            airpodsLayoutPopupBinding.tvLeftPod.text = "-"
            airpodsLayoutPopupBinding.ivLeftPodBattery.progress = 0f
        }
        if (it.rightBattery <= 10) {
            airpodsLayoutPopupBinding.tvRightPod.text =
                if (it.rightBattery == 10) "100%" else "${it.rightBattery * 10 + 5}%"
            airpodsLayoutPopupBinding.ivRightPodBattery.progress = (it.rightBattery + 0.5f) / 10f
        } else {
            airpodsLayoutPopupBinding.tvRightPod.text = "-"
            airpodsLayoutPopupBinding.ivRightPodBattery.progress = 0f
        }
    }

    private fun updateAirPodsBattery(isLoading: Boolean, isBatteryStateChange: Boolean = false) {
        airpodsLayoutPopupBinding.ivPodsLoading.visibility =
            if (isLoading) View.VISIBLE else View.INVISIBLE
        airpodsLayoutPopupBinding.tvLeftPod.visibility =
            if (isLoading) View.INVISIBLE else View.VISIBLE
        airpodsLayoutPopupBinding.ivLeftPodBattery.visibility =
            if (isLoading) View.INVISIBLE else View.VISIBLE
        airpodsLayoutPopupBinding.tvRightPod.visibility =
            if (isLoading) View.INVISIBLE else View.VISIBLE
        airpodsLayoutPopupBinding.ivRightPodBattery.visibility =
            if (isLoading) View.INVISIBLE else View.VISIBLE
        if (!isBatteryStateChange) {
            airpodsLayoutPopupBinding.tvCase.visibility =
                if (isLoading) View.INVISIBLE else View.VISIBLE
            airpodsLayoutPopupBinding.ivCaseBattery.visibility =
                if (isLoading) View.INVISIBLE else View.VISIBLE
        }
    }

    private fun updateAirPodsModel(model: String) {
        if (airpodsLayoutPopupBinding.ivCase.tag != model) {
            airpodsLayoutPopupBinding.ivCase.tag = model
            airpodsLayoutPopupBinding.ivLeftPod.setAnimation(if (model == MODEL_AIR_PODS_PRO) R.raw.airpod_left_pro else R.raw.airpod_left)
            airpodsLayoutPopupBinding.ivRightPod.setAnimation(if (model == MODEL_AIR_PODS_PRO) R.raw.airpod_right_pro else R.raw.airpod_right)
            airpodsLayoutPopupBinding.ivCase.setAnimation(
                when (model) {
                    MODEL_AIR_PODS_PRO -> R.raw.case_empty_pro
                    MODEL_AIR_PODS_2 -> R.raw.case_empty_gen_two
                    else -> R.raw.case_empty_gen_one
                }
            )
            airpodsLayoutPopupBinding.ivCaseLeft.setAnimation((if (model == MODEL_AIR_PODS_PRO) R.raw.case_left_pro else R.raw.case_left))
            airpodsLayoutPopupBinding.ivCaseRight.setAnimation((if (model == MODEL_AIR_PODS_PRO) R.raw.case_right_pro else R.raw.case_right))
        }
    }

}