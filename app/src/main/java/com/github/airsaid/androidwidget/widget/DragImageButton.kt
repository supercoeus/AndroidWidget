package com.github.airsaid.androidwidget.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ImageButton
import androidx.cardview.widget.CardView
import com.github.airsaid.androidwidget.util.DragHelper

/**
 * @author airsaid
 */
class DragImageButton @JvmOverloads constructor
  (context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ImageButton(context, attrs, defStyleAttr) {

  private val dragHelper = DragHelper(context, this)

  var isRebound = dragHelper.isRebound
    set(value) {
      dragHelper.isRebound = value
      field = value
    }

  var reboundDuration = dragHelper.reboundDuration
    set(value) {
      dragHelper.reboundDuration = reboundDuration
      field = value
    }

  var reboundInterpolator = dragHelper.reboundInterpolator
    set(value) {
      dragHelper.reboundInterpolator = value
      field = value
    }

  override fun dispatchTouchEvent(event: MotionEvent): Boolean {
    if (dragHelper.dispatchTouchEvent(event)) {
      return super.dispatchTouchEvent(event)
    } else {
      return false
    }
  }

  @SuppressLint("ClickableViewAccessibility")
  override fun onTouchEvent(event: MotionEvent): Boolean {
    return dragHelper.onTouchEvent(event)
  }

}