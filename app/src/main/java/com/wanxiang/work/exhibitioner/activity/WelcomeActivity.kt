package com.wanxiang.work.exhibitioner.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.wanxiang.work.exhibitioner.R
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        startService(Intent(getString(R.string.action_request_deviceid)))
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
