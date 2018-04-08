package com.wanxiang.work.exhibitioner.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.wanxiang.work.exhibitioner.R
import java.util.*

class DeviceIdManagerService : Service() {

    private val TAG = "设备ID管理Service"

    private val DEVICE_INFO_KEY = "CLOUD_SHOW_DEVICE_INFO_KEY"
    private val DEVICE_ID_LOAD_KEY = "DEVICE_ID_KEY"

    // 设备ID属性
    private var deviceId: UUID? = null

    fun fetchDeviceId() : UUID {
        return deviceId as UUID
    }

    fun getDeviceIdString() : String = fetchDeviceId().toString()

    private fun loadDeviceId(): UUID {
        // 从配置中读取，如果不存在使用随机数创建一个
        val settings = getSharedPreferences(DEVICE_INFO_KEY, 0)
        val deviceIdStr = settings.getString(DEVICE_ID_LOAD_KEY, UUID.randomUUID().toString())
        settings.edit().putString(DEVICE_ID_LOAD_KEY, deviceIdStr)

        return UUID.fromString(deviceIdStr)
    }

//    //
//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//
//        if(intent != null) {
//            Log.v(TAG, "DeviceManagerService接收到命令: ${intent.toString()}")
//            when (intent.action) {
//                getString(R.string.action_request_deviceid) -> doBoardCastDeviceId()
//            }
//        }
//        else {
//            Log.v(TAG, "DeviceManagerService接收到命令: null ...")
//        }
//
//        return super.onStartCommand(intent, flags, startId)
//    }

//    private fun doBoardCastDeviceId() {
//        var intent = Intent(getString(R.string.action_deviceid_ready))
//        intent.putExtra(getString(R.string.key_device_id), getDeviceIdString())
//        sendBroadcast(intent)
//        Log.v(TAG, "广播deviceID：${getDeviceIdString()}")
//
//        // TODO: 测试用
//        QrBitmapMakeIntentService.startActionMakeQRBitmap(applicationContext, getDeviceIdString())
//    }

    override fun onBind(intent: Intent): IBinder? {
        return DeviceIdManagerServiceBinder()
    }

    inner class DeviceIdManagerServiceBinder : Binder() {
        fun getService(): DeviceIdManagerService = this@DeviceIdManagerService
    }

    override fun onCreate() {
        super.onCreate()

        deviceId = loadDeviceId()
    }
}
