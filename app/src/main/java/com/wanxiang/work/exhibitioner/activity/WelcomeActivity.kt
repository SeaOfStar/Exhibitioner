package com.wanxiang.work.exhibitioner.activity

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.nfc.Tag
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import com.wanxiang.work.exhibitioner.R
import com.wanxiang.work.exhibitioner.service.DeviceIdManagerService
import com.wanxiang.work.exhibitioner.service.QrBitmapMakeIntentService
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : AppCompatActivity() {

    private val TAG = "WelcomeActivity"

    private val conn: ServiceCon = ServiceCon()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        Log.v("WelcomActivity", "发送广播消息")

        val intent = Intent(this, DeviceIdManagerService::class.java)
        bindService(intent, conn, Context.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        unbindService(conn)
        super.onDestroy()
    }

    inner class ServiceCon: ServiceConnection{
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            Log.v(TAG, "连接到Service")
            val service = (p1 as DeviceIdManagerService.DeviceIdManagerServiceBinder).getService()
            val uuidStr = service.deviceIdString
            Log.v(TAG, "取得deviceId： $uuidStr")

            QrBitmapMakeIntentService.startActionMakeQRBitmap(applicationContext, uuidStr)
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            Log.v(TAG, "断开连接")
        }
    }
}
