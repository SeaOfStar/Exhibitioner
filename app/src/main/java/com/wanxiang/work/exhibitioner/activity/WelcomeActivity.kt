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

    private val conn: ServiceCon = ServiceCon()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        Log.v("WelcomActivity", "发送广播消息")

//        val intent = Intent(getString(R.string.action_request_deviceid))
        val intent = Intent(this, DeviceIdManagerService::class.java)
//        startService(intent)

        bindService(intent, conn, Context.BIND_AUTO_CREATE)
    }

    override fun onStart() {
        super.onStart()

        val intent = getIntent();
        when (intent.action) {
            getString(R.string.action_qr_ready) -> {
                // 取得Bitmap数据，解码
                val bis = intent.getByteArrayExtra(getString(R.string.key_qr_bitmap))
                val bitmap = BitmapFactory.decodeByteArray(bis, 0, bis.size)
                showQrCode(bitmap)
            }
        }
    }

    private fun showQrCode(bitmap: Bitmap) {
//        qrShowView.setImageBitmap(bitmap)
    }
}
