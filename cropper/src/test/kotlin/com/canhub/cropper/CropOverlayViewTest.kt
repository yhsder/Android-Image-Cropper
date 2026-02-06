package com.canhub.cropper

import android.graphics.RectF
import com.canhub.cropper.CropImageView.CropCornerShape
import com.canhub.cropper.CropImageView.CropShape
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CropOverlayViewTest {

  @Test
  fun `WHEN middle segments switch is disabled THEN segments should not draw`() {
    assertFalse(
      CropOverlayView.shouldDrawMiddleSegments(
        showMiddleSegments = false,
        cropShape = CropShape.RECTANGLE,
        cornerShape = CropCornerShape.RECTANGLE,
      ),
    )
  }

  @Test
  fun `WHEN shape is not rectangle or corner shape is oval THEN middle segments should not draw`() {
    assertFalse(
      CropOverlayView.shouldDrawMiddleSegments(
        showMiddleSegments = true,
        cropShape = CropShape.OVAL,
        cornerShape = CropCornerShape.RECTANGLE,
      ),
    )
    assertFalse(
      CropOverlayView.shouldDrawMiddleSegments(
        showMiddleSegments = true,
        cropShape = CropShape.RECTANGLE_VERTICAL_ONLY,
        cornerShape = CropCornerShape.RECTANGLE,
      ),
    )
    assertFalse(
      CropOverlayView.shouldDrawMiddleSegments(
        showMiddleSegments = true,
        cropShape = CropShape.RECTANGLE_HORIZONTAL_ONLY,
        cornerShape = CropCornerShape.RECTANGLE,
      ),
    )
    assertFalse(
      CropOverlayView.shouldDrawMiddleSegments(
        showMiddleSegments = true,
        cropShape = CropShape.RECTANGLE,
        cornerShape = CropCornerShape.OVAL,
      ),
    )
    assertTrue(
      CropOverlayView.shouldDrawMiddleSegments(
        showMiddleSegments = true,
        cropShape = CropShape.RECTANGLE,
        cornerShape = CropCornerShape.RECTANGLE,
      ),
    )
  }

  @Test
  fun `WHEN offset changes THEN middle segment positions follow existing corner offset semantics`() {
    val rect = RectF(10f, 20f, 110f, 220f)
    val zeroOffsetLines = CropOverlayView.getMiddleSegmentLines(
      cropWindowRect = rect,
      borderLineWidth = 2f,
      cornerLineWidth = 6f,
      cornerLength = 10f,
      borderCornerOffset = 0f,
    )
    val positiveOffsetLines = CropOverlayView.getMiddleSegmentLines(
      cropWindowRect = rect,
      borderLineWidth = 2f,
      cornerLineWidth = 6f,
      cornerLength = 10f,
      borderCornerOffset = 4f,
    )
    val negativeOffsetLines = CropOverlayView.getMiddleSegmentLines(
      cropWindowRect = rect,
      borderLineWidth = 2f,
      cornerLineWidth = 6f,
      cornerLength = 10f,
      borderCornerOffset = -4f,
    )

    // Positive offset moves lines inward, negative offset moves lines outward.
    assertTrue(positiveOffsetLines[0].startY > zeroOffsetLines[0].startY)
    assertTrue(negativeOffsetLines[0].startY < zeroOffsetLines[0].startY)

    assertTrue(positiveOffsetLines[1].startY < zeroOffsetLines[1].startY)
    assertTrue(negativeOffsetLines[1].startY > zeroOffsetLines[1].startY)

    assertTrue(positiveOffsetLines[2].startX > zeroOffsetLines[2].startX)
    assertTrue(negativeOffsetLines[2].startX < zeroOffsetLines[2].startX)

    assertTrue(positiveOffsetLines[3].startX < zeroOffsetLines[3].startX)
    assertTrue(negativeOffsetLines[3].startX > zeroOffsetLines[3].startX)

    // Segment length should still match corner length * 2.
    assertEquals(20f, zeroOffsetLines[0].endX - zeroOffsetLines[0].startX, 0.001f)
    assertEquals(20f, zeroOffsetLines[2].endY - zeroOffsetLines[2].startY, 0.001f)
  }
}
