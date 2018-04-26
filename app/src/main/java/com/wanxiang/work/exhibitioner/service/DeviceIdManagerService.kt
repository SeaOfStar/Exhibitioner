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
    public lateinit var deviceId: UUID
        private set

    override fun onCreate() {
        super.onCreate()
        deviceId = loadDeviceId()
    }

    val deviceIdString: String
        get() = deviceId.toString()

    private fun loadDeviceId(): UUID {
        // 从配置中读取，如果不存在使用随机数创建一个
        val settings = getSharedPreferences(DEVICE_INFO_KEY, 0)
        val deviceIdStr = settings.getString(DEVICE_ID_LOAD_KEY, UUID.randomUUID().toString())
        settings.edit().putString(DEVICE_ID_LOAD_KEY, deviceIdStr)

        return UUID.fromString(deviceIdStr)
    }

    override fun onBind(intent: Intent): IBinder? {
        return DeviceIdManagerServiceBinder()
    }

    inner class DeviceIdManagerServiceBinder : Binder() {
        val service
            get() = this@DeviceIdManagerService
//        fun getService(): DeviceIdManagerService = this@DeviceIdManagerService
    }
}
