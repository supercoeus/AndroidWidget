package com.github.airsaid.androidwidget.util

import android.animation.ObjectAnimator
import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import androidx.core.math.MathUtils

/**
 * @author airsaid
 */
class DragHelper(context: Context, target: View) {

  private val view = target
  private var downX = 0f
  private var downY = 0f
  private var lastX = 0f
  private var lastY = 0f
  private var isDrag = false
  private var parentWidth = 0
  private var parentHeight = 0
  private val scaledTouchSlop = ViewConfiguration.get(context).scaledTouchSlop

  var isRebound = true
  var reboundDuration = 300L
  var reboundInterpolator = OvershootInterpolator()

  fun dispatchTouchEvent(event: MotionEvent): Boolean {
    val rawX = event.rawX
    val rawY = event.rawY
    when (event.actionMasked) {
      MotionEvent.ACTION_DOWN -> {
        isDrag = false
        // 记录按下位置
        downX = rawX
        downY = rawY
        return true
      }
      MotionEvent.ACTION_MOVE -> {
        // 获取滑动距离
        val dx = rawX - downX
        val dy = rawY - downY
        // 判断是否该处理拖拽
        isDrag = !(Math.abs(dx) <= scaledTouchSlop && Math.abs(dy) <= scaledTouchSlop)
      }
      MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
        if (!isDrag) view.performClick()
      }
    }
    return isDrag
  }

  fun onTouchEvent(event: MotionEvent): Boolean {
    val rawX = event.rawX
    val rawY = event.rawY
    when (event.actionMasked) {
      MotionEvent.ACTION_DOWN -> {
        // 记录按下位置
        lastX = rawX
        lastY = rawY
        // 获取父控件宽高
        view.parent?.let { p ->
          if (p is ViewGroup) {
            parentWidth = p.width
            parentHeight = p.height
          }
        }
      }
      MotionEvent.ACTION_MOVE -> {
        // 获取滑动距离
        val dx = rawX - lastX
        val dy = rawY - lastY
        // 不可超过父控件
        view.x = MathUtils.clamp(dx + view.x, 0f, (parentWidth - view.width).toFloat())
        view.y = MathUtils.clamp(dy + view.y, 0f, (parentHeight - view.height).toFloat())
        lastX = rawX
        lastY = rawY
      }
      MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
        if (isRebound) rebound(view, rawX)
      }
    }
    return true
  }

  private fun rebound(view: View, x: Float) {
    if (x <= parentWidth / 2) {
      // 回弹到左侧
      startReboundAnimator(view, x, 0f)
    } else {
      // 回弹到右侧
      startReboundAnimator(view, x, (parentWidth - view.width).toFloat())
    }
  }

  private fun startReboundAnimator(view: View, from: Float, to: Float) {
    val animator = ObjectAnimator.ofFloat(view, View.X, from, to)
    animator.interpolator = reboundInterpolator
    animator.duration = reboundDuration
    animator.start()
  }

}