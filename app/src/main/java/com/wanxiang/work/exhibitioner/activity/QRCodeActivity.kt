package com.wanxiang.work.exhibitioner.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.ActionMode
import com.wanxiang.work.exhibitioner.R

import kotlinx.android.synthetic.main.activity_qrcode.*
import java.io.ByteArrayOutputStream

class QRCodeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrcode)

        // 取得bitmap
        if(intent != null) {
            val bis = intent.getByteArrayExtra(ACTION_SHOW_BITMAP_PARAM_BYTE_ARRAY)
            val bmp = BitmapFactory.decodeByteArray(bis, 0, bis.size)
            qrShowView.setImageBitmap(bmp)
        }
    }

    companion object {
        private const val ACTION_SHOW_BITMAP = "com.wanxiang.work.exhibitioner.activity.QRCodeActivity.action.showbitmap"
        private const val ACTION_SHOW_BITMAP_PARAM_BYTE_ARRAY = "com.wanxiang.work.exhibitioner.activity.QRCodeActivity.action.showbitmap.param.bitmap.array"

        fun startShowBitmapAction(context: Context, bitmap: Bitmap, useNewTask:Boolean = false) {
            // 将bitmap解析成byte数组
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)

            // 将数组发送出去
            val intent = Intent(context, QRCodeActivity::class.java)
            intent.setAction(ACTION_SHOW_BITMAP)
            intent.putExtra(ACTION_SHOW_BITMAP_PARAM_BYTE_ARRAY, baos.toByteArray())
            if (useNewTask) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }
}
