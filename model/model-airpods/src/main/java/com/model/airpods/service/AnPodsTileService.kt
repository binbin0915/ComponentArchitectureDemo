package com.model.airpods.service

import android.content.Context
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.Icon
import android.os.Build
import android.os.IBinder
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.text.TextPaint
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ServiceLifecycleDispatcher
import com.model.airpods.R
import com.model.airpods.util.ACTION_POPUP
import com.model.airpods.util.airPodsBatteryState
import com.model.airpods.util.airPodsConnectionState
import com.model.airpods.util.dp

/**
 * 磁贴
 */


fun getTitle(level: Int, charge: Boolean = false): String {
    return (if (level == 10) "100%" else if (level < 10) (level * 10 + 5).toString() + "%" else "-") + if (charge && level < 10) "+" else ""
}

fun Context.getTitleBitmap(
    leftBattery: Int,
    leftCharge: Boolean,
    rightBattery: Int,
    rightCharge: Boolean
): Bitmap {
    val strokeSize = 2.dp.toFloat()
    val batteryWidth = 26.dp.toFloat()
    val batteryHeight = 59.dp.toFloat()
    val batteryCapWidth = 12.dp.toFloat()
    val batteryCapHeight = 5.dp.toFloat()

    //L battery rectf
    val leftBatteryRectF = RectF(
        2.dp.toFloat(),
        batteryCapHeight,
        batteryWidth + 2.dp,
        batteryCapHeight + batteryHeight
    )
    //L battery cap rectf
    val leftBatteryCapRectF = RectF(
        2.dp + (batteryWidth - batteryCapWidth) / 2.0f,
        0f,
        2.dp + (batteryWidth - batteryCapWidth) / 2.0f + batteryCapWidth,
        batteryCapHeight
    )
    //L battery power rectf
    var leftPowerRectF = RectF(leftBatteryRectF).apply {
        inset(4.dp.toFloat(), 4.dp.toFloat())

    }
    if (leftBattery < 10) {
        leftPowerRectF = RectF(
            leftPowerRectF.left,
            leftPowerRectF.top + (leftPowerRectF.height() * (10.5f - leftBattery)) / 10f,
            leftPowerRectF.right,
            leftPowerRectF.bottom
        )
    }
    //R battery rectf
    val rightBatteryRectF = RectF(leftBatteryRectF).apply {
        offset(32.dp.toFloat(), 0f)
    }
    //R battery cap rectf
    val rightBatteryCapRectF = RectF(leftBatteryCapRectF).apply {
        offset(32.dp.toFloat(), 0f)
    }
    //R battery power rectf
    var rightBateryPowerRectF = RectF(rightBatteryRectF).apply {
        inset(4.dp.toFloat(), 4.dp.toFloat())
    }
    if (rightBattery < 10) {
        rightBateryPowerRectF = RectF(
            rightBateryPowerRectF.left,
            rightBateryPowerRectF.top + (rightBateryPowerRectF.height() * (10.5f - rightBattery)) / 10f,
            rightBateryPowerRectF.right,
            rightBateryPowerRectF.bottom
        )
    }
    val paint = Paint().apply {
        color = Color.WHITE
        strokeWidth = strokeSize
        style = Paint.Style.STROKE
        isAntiAlias = true
    }
    val powerPaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    val bitmap = Bitmap.createBitmap(64.dp, 64.dp, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    val radius = 4.dp.toFloat()
    canvas.drawRoundRect(leftBatteryRectF, radius, radius, paint)
    canvas.drawRoundRect(leftBatteryCapRectF, strokeSize, strokeSize, powerPaint)
    canvas.drawRoundRect(leftPowerRectF, 1f, 1f, powerPaint)
    canvas.drawRoundRect(rightBatteryRectF, radius, radius, paint)
    canvas.drawRoundRect(rightBatteryCapRectF, strokeSize, strokeSize, powerPaint)
    canvas.drawRoundRect(rightBateryPowerRectF, 1f, 1f, powerPaint)
//    if (leftCharge || rightCharge) {
//        val path = Path().apply {
//            moveTo(dip(38).toFloat(), dip(6).toFloat())
//            lineTo(dip(14).toFloat(), dip(36).toFloat())
//            lineTo(dip(32).toFloat(), dip(36).toFloat())
//            lineTo(dip(26).toFloat(), dip(58).toFloat())
//            lineTo(dip(50).toFloat(), dip(28).toFloat())
//            lineTo(dip(32).toFloat(), dip(28).toFloat())
//        }
//        val matrix = Matrix().apply {
//            postScale(0.5f, 0.5f)
//        }
//        path.transform(matrix)
//        path.offset(dip(-1f).toFloat(), dip(18).toFloat())
//        if (leftCharge) {
//            canvas.drawPath(path, powerPaint)
//        }
//        path.offset(dip(31f).toFloat(), 0f)
//        if (rightCharge) {
//            canvas.drawPath(path, powerPaint)
//        }
//    }
    return bitmap
}

private const val WATER_MARK_SHADOW_Y = 2f
private const val WATER_MARK_SHADOW_Y_COLOR = 0x2E000000

fun Context.getDefaultPaint(textSize: Float): TextPaint {
    return TextPaint(TextPaint.ANTI_ALIAS_FLAG).apply {
        this.textSize = textSize
        isAntiAlias = true
        color = Color.WHITE

        isFakeBoldText = true
        setShadowLayer(
            0.1f,
            0f,
            WATER_MARK_SHADOW_Y,
            WATER_MARK_SHADOW_Y_COLOR
        )
        strokeWidth = 2f
        style = Paint.Style.FILL_AND_STROKE
        this.letterSpacing = 0f
    }
}

@RequiresApi(Build.VERSION_CODES.N)
class AnPodsTileService : TileService(), LifecycleOwner {
    private val mDispatcher = ServiceLifecycleDispatcher(this)
    override fun onCreate() {
        super.onCreate()
        Log.d("AAAAAAAAAAAAAAAAAAA", "AnPodsTileService创建")
        mDispatcher.onServicePreSuperOnCreate()
        airPodsBatteryState.observe({ lifecycle }) {
            val state = airPodsConnectionState.value
            if (state == null || !state.isConnected) return@observe
            val left = getTitle(it.leftBattery, it.isLeftCharge)
            val right = getTitle(it.rightBattery, it.isRightCharge)
//            val bitmap =
//                getTitleBitmap(it.leftBattery, it.isLeftCharge, it.rightBattery, it.isRightCharge)
            updateTile("$left|$right", null, Tile.STATE_ACTIVE)
        }
        airPodsConnectionState.observe({ lifecycle }) {
            updateTile(
                if (!it.isConnected) getString(R.string.airpods_tile_disconnected_title) else it.deviceName,
                if (!it.isConnected) Icon.createWithResource(
                    applicationContext,
                    R.drawable.airpods_ic_airpods
                ) else null,
                if (!it.isConnected) Tile.STATE_INACTIVE else Tile.STATE_ACTIVE
            )
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        mDispatcher.onServicePreSuperOnBind()
        return super.onBind(intent)
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mDispatcher.onServicePreSuperOnStart()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        mDispatcher.onServicePreSuperOnDestroy()
    }

    override fun onStartListening() {
        super.onStartListening()
        val state = airPodsConnectionState.value
        if (state == null || !state.isConnected) {
            updateTile(
                getString(R.string.airpods_tile_disconnected_title),
                Icon.createWithResource(applicationContext, R.drawable.airpods_ic_airpods),
                Tile.STATE_INACTIVE
            )
        }
    }

    override fun onStopListening() {
        super.onStopListening()
        val state = airPodsConnectionState.value
        if (state == null || !state.isConnected) {
            updateTile(
                getString(R.string.airpods_tile_disconnected_title),
                Icon.createWithResource(applicationContext, R.drawable.airpods_ic_airpods),
                Tile.STATE_INACTIVE
            )
        }
    }

    private fun updateTile(title: String, icon: Icon?, state: Int) {
        qsTile.label = title
        icon?.let {
            qsTile.icon = icon
        }
        qsTile.state = state
        qsTile.updateTile()
    }

    override fun onClick() {
        super.onClick()
        startService(Intent(this, AnPodsService::class.java).apply { action = ACTION_POPUP })
    }

    override fun getLifecycle(): Lifecycle {
        return mDispatcher.lifecycle
    }
}