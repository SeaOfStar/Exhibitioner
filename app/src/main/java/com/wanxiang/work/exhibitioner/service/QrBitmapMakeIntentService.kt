package com.wanxiang.work.exhibitioner.service

import android.app.IntentService
import android.content.Intent
import android.content.Context
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.wanxiang.work.exhibitioner.R
import com.wanxiang.work.exhibitioner.activity.QRCodeActivity
import java.io.ByteArrayOutputStream

/**
 * An [IntentService] subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 *
 *
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
class QrBitmapMakeIntentService : IntentService("QrBitmapMakeIntentService") {

    override fun onHandleIntent(intent: Intent?) {
        if (intent != null) {
            val action = intent.action
            if (ACTION_MAKE_BITMAP == action) {
                val content = intent.getStringExtra(EXTRA_PARAM_CONTENT)
                val width = intent.getIntExtra(EXTRA_PARAM_WIDTH, PARAM_DEFALUT_WIDTH)
                val height = intent.getIntExtra(EXTRA_PARAM_HEIGHT, PARAM_DEFALUT_HEIGHT)
                handleActionMakeBitmap(content, width, height)
            }
        }
    }

    /**
     * 将文字转化成QRbitmap的二进制字符数组，并广播出去
     */
    private fun handleActionMakeBitmap(content: String, width: Int, height: Int) {
        // 生成bitmap
        val writer = QRCodeWriter()
        val hints = HashMap<EncodeHintType, String>()
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8")

        val encode = writer.encode(content, BarcodeFormat.QR_CODE, width, height, hints)
        val pixels = IntArray(width * height)

        for(i in 0 until height) {
            for (j in 0 until width) {
                pixels[i * width + j] = if (encode.get(j, i)) 0x00 else -1
            }
        }

        val bmp = Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565)
        QRCodeActivity.startShowBitmapAction(applicationContext, bmp, true)
    }

    companion object {
        private const val ACTION_MAKE_BITMAP = "com.wanxiang.work.exhibitioner.service.action.make.bitmap"
        private const val EXTRA_PARAM_CONTENT = "com.wanxiang.work.exhibitioner.service.extra.content"
        private const val EXTRA_PARAM_WIDTH = "com.wanxiang.work.exhibitioner.service.extra.width"
        private const val EXTRA_PARAM_HEIGHT = "com.wanxiang.work.exhibitioner.service.extra.height"

        private const val PARAM_DEFALUT_WIDTH: Int = 400
        private const val PARAM_DEFALUT_HEIGHT: Int = 400


        /**
         * Starts this service to perform action Foo with the given parameters. If
         * the service is already performing a task this action will be queued.
         *
         * @see IntentService
         */
        fun startActionMakeQRBitmap(context: Context, contentString: String, width: Int = PARAM_DEFALUT_WIDTH, height: Int = PARAM_DEFALUT_HEIGHT) {
            val intent = Intent(context, QrBitmapMakeIntentService::class.java)
            intent.action = ACTION_MAKE_BITMAP
            intent.putExtra(EXTRA_PARAM_CONTENT, contentString)
            intent.putExtra(EXTRA_PARAM_WIDTH, width)
            intent.putExtra(EXTRA_PARAM_HEIGHT, height)

            context.startService(intent)
        }
    }
}
