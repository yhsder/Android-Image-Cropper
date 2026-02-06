package com.canhub.cropper

import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.RectF
import android.view.View
import android.widget.ImageView
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class CropImageViewGeometryTest {

  @Test
  fun `WHEN gutter inset is extreme THEN content rect keeps minimum visible area`() {
    val contentRect = CropImageView.calculateImageContentRect(
      width = 40f,
      height = 30f,
      visualGutterInset = 100f,
    )

    assertEquals(14f, contentRect.left, 0.001f)
    assertEquals(14f, contentRect.top, 0.001f)
    assertEquals(26f, contentRect.right, 0.001f)
    assertEquals(16f, contentRect.bottom, 0.001f)
  }

  @Test
  fun `WHEN gutter inset is zero THEN content rect matches full view bounds`() {
    val contentRect = CropImageView.calculateImageContentRect(
      width = 120f,
      height = 80f,
      visualGutterInset = 0f,
    )

    assertEquals(RectF(0f, 0f, 120f, 80f), contentRect)
  }

  @Test
  fun `WHEN mapping crop points THEN source coordinates follow inverse matrix semantics`() {
    val matrix = Matrix().apply {
      setScale(2f, 3f)
      postTranslate(10f, 15f)
    }

    val points = CropImageView.getCropPointsInSourceImage(
      cropWindowRect = RectF(12f, 21f, 32f, 51f),
      imageMatrix = matrix,
      loadedSampleSize = 2,
    )

    assertArrayEquals(
      floatArrayOf(
        2f,
        4f,
        22f,
        4f,
        22f,
        24f,
        2f,
        24f,
      ),
      points,
      0.001f,
    )
  }

  @Test
  fun `WHEN options change visual gutter THEN image matrix is recomputed immediately`() {
    val cropImageView = CropImageView(RuntimeEnvironment.getApplication())
    val measureSpec = View.MeasureSpec.makeMeasureSpec(400, View.MeasureSpec.EXACTLY)
    cropImageView.measure(measureSpec, measureSpec)
    cropImageView.layout(0, 0, 400, 400)
    cropImageView.setImageBitmap(Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888))

    val imageView = cropImageView.findViewById<ImageView>(R.id.ImageView_image)
    val beforeMatrixValues = FloatArray(9).also { imageView.imageMatrix.getValues(it) }

    cropImageView.setImageCropOptions(
      CropImageOptions().copy(
        cropShape = CropImageView.CropShape.RECTANGLE,
        cornerShape = CropImageView.CropCornerShape.RECTANGLE,
        borderLineThickness = 2f,
        borderCornerThickness = 8f,
        borderCornerLength = 28f,
        borderCornerOffset = -16f,
      ),
    )

    val afterMatrixValues = FloatArray(9).also { imageView.imageMatrix.getValues(it) }
    assertFalse(beforeMatrixValues.contentEquals(afterMatrixValues))
  }
}
