package com.github.airsaid.androidwidget.widget.particle

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat

/**
 * @author airsaid
 */
class ParticleExplodeView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    View(context, attrs, defStyleAttr), IParticleDraw {

  private val mGestureDetectorCompat: GestureDetectorCompat
  private val mParticleEmitter = ParticleEmitter.build(this) {
    maxCount = 30
    minCount = 10
    maxRadius = 16
    minRadius = 10
    color = Color.BLUE
    duration = 500
  }

  init {
    mGestureDetectorCompat = GestureDetectorCompat(context, object : GestureDetector.SimpleOnGestureListener() {
      override fun onShowPress(e: MotionEvent) {
        mParticleEmitter.send(e.x, e.y)
      }
    })
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)
    mParticleEmitter.onDraw(canvas)
  }

  @SuppressLint("ClickableViewAccessibility")
  override fun onTouchEvent(event: MotionEvent?): Boolean {
    return mGestureDetectorCompat.onTouchEvent(event)
  }
}