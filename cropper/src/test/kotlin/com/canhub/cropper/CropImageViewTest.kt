package com.canhub.cropper

import android.graphics.BitmapFactory
import android.graphics.Rect
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.ImageView
import app.cash.paparazzi.DeviceConfig.Companion.PIXEL_5
import app.cash.paparazzi.Paparazzi
import org.junit.Rule
import org.junit.Test
import java.io.File

class CropImageViewTest {
  @get:Rule
  val paparazzi = Paparazzi(
    deviceConfig = PIXEL_5,
    theme = "Theme.MaterialComponents.DayNight.DarkActionBar",
  )

  @Test fun ovalBitmap() {
    val file = fileFromAsset("small-tree.jpg")
    val imageView = ImageView(paparazzi.context)
    imageView.setImageBitmap(CropImage.toOvalBitmap(BitmapFactory.decodeStream(file.inputStream())))
    paparazzi.snapshot(imageView)
  }

  @Test fun documentFrameBitmap() {
    val file = fileFromAsset("small-tree.jpg")
    val cropImageView = CropImageView(paparazzi.context).apply {
      layoutParams = ViewGroup.LayoutParams(dp(360), dp(640))
      setImageBitmap(BitmapFactory.decodeStream(file.inputStream()))
      setImageCropOptions(CropImageOptions.defaultDocumentStyleOptions())
      cropRect = Rect(42, 192, 412, 566)
    }
    paparazzi.snapshot(cropImageView)
  }

  private fun fileFromAsset(name: String) =
    File(CropImageViewTest::class.java.classLoader?.getResource(name)?.file!!)

  private fun dp(value: Int): Int =
    TypedValue.applyDimension(
      TypedValue.COMPLEX_UNIT_DIP,
      value.toFloat(),
      paparazzi.context.resources.displayMetrics,
    ).toInt()
}
