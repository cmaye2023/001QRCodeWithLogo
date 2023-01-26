package com.onthego.a001qrcodewithlogo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.onthego.a001qrcodewithlogo.QRCodeManager.QRCodeGenerate.Companion.dpToPx
import com.onthego.a001qrcodewithlogo.QRCodeManager.QRCodeGenerate.Companion.encodeAsQrCodeBitmap
import com.onthego.a001qrcodewithlogo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            var displayMetrics = DisplayMetrics()
            this.windowManager.defaultDisplay.getMetrics(displayMetrics)

            var size = displayMetrics.widthPixels.coerceAtMost(displayMetrics.heightPixels)
            var overlay = ContextCompat.getDrawable(this,R.drawable.dev_circle_img)!!.toBitmap(75.dpToPx(),75.dpToPx())
            var bitmap = "https://www.cmaye.com".encodeAsQrCodeBitmap(size,overlay)

            binding.imageView.setImageBitmap(bitmap)
        }catch (ex : Exception)
        {
            Toast.makeText(this, "Error! $ex", Toast.LENGTH_SHORT).show()
        }
    }
}