package com.onthego.qrcodewithlogogenerator.QRCodeManager

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel

class QRCodeGenerate {
    companion object
    {
        fun convertQRCodeGeneratorWithLogo(activity: Activity,qrText : String, roundImageLogo : Int,bitmapSize : Int) : Bitmap?
        {
            try {
                var displayMetrics = DisplayMetrics()
                activity.windowManager.defaultDisplay.getMetrics(displayMetrics)

                var size = displayMetrics.widthPixels.coerceAtMost(displayMetrics.heightPixels)
                var overlay = ContextCompat.getDrawable(activity,roundImageLogo)!!.toBitmap(bitmapSize.dpToPx(),bitmapSize.dpToPx())
                var bitmap = qrText.encodeAsQrCodeBitmap(size,overlay)
                return bitmap
            }catch (ex : Exception)
            {
                return null
            }
        }
        fun convertQRCodeGeneratorWithoutLogo(activity: Activity,qrText : String) : Bitmap?
        {
            try {
                var displayMetrics = DisplayMetrics()
                activity.windowManager.defaultDisplay.getMetrics(displayMetrics)

                var size = displayMetrics.widthPixels.coerceAtMost(displayMetrics.heightPixels)
                var bitmap = qrText.encodeAsQrCodeBitmap(size,null)
                return bitmap
            }catch (ex : Exception)
            {
                return null
            }
        }




        @Throws(WriterException::class)
        fun String.encodeAsQrCodeBitmap(
            dimension: Int,
            overlayBitmap: Bitmap? = null,
            @ColorInt color1: Int = Color.BLACK,
            @ColorInt color2: Int = Color.WHITE
        ): Bitmap? {

            val result: BitMatrix
            try {
                result = MultiFormatWriter().encode(
                    this,
                    BarcodeFormat.QR_CODE,
                    dimension,
                    dimension,
                    hashMapOf(EncodeHintType.ERROR_CORRECTION to ErrorCorrectionLevel.H)
                )
            } catch (e: IllegalArgumentException) {
                // Unsupported format
                return null
            }

            val w = result.width
            val h = result.height
            val pixels = IntArray(w * h)
            for (y in 0 until h) {
                val offset = y * w
                for (x in 0 until w) {
                    pixels[offset + x] = if (result.get(x, y)) color1 else color2
                }
            }
            val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            bitmap.setPixels(pixels, 0, dimension, 0, 0, w, h)

            return if (overlayBitmap != null) {
                bitmap.addOverlayToCenter(overlayBitmap)
            } else {
                bitmap
            }
        }

        fun Bitmap.addOverlayToCenter(overlayBitmap: Bitmap): Bitmap {

            val bitmap2Width = overlayBitmap.width
            val bitmap2Height = overlayBitmap.height
            val marginLeft = (this.width * 0.5 - bitmap2Width * 0.5).toFloat()
            val marginTop = (this.height * 0.5 - bitmap2Height * 0.5).toFloat()
            val canvas = Canvas(this)
            canvas.drawBitmap(this, Matrix(), null)
            canvas.drawBitmap(overlayBitmap, marginLeft, marginTop, null)
            return this
        }

        fun Int.dpToPx(): Int {
            return (this * Resources.getSystem().displayMetrics.density).toInt()
        }
    }
}