package com.wanxiang.work.exhibitioner.activity

import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.ActionMode
import com.wanxiang.work.exhibitioner.R

import kotlinx.android.synthetic.main.activity_qrcode.*

class QRCodeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrcode)

        // 取得bitmap
        if(intent != null) {
            val bis = intent.getByteArrayExtra(getString(R.string.key_qr_bitmap))
            val bmp = BitmapFactory.decodeByteArray(bis, 0, bis.size)
            qrShowView.setImageBitmap(bmp)
        }
    }
}
