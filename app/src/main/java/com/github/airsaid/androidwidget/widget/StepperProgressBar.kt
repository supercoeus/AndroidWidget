package com.github.airsaid.androidwidget.widget

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.Keep
import androidx.core.math.MathUtils
import com.github.airsaid.androidwidget.R

/**
 * 自定义步进进度条。
 *
 * @author airsaid
 */
class StepperProgressBar @JvmOverloads constructor
(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

  private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
  private var progressAnimator: ObjectAnimator? = null
  private var progressBarChangeListener: OnProgressBarChangeListener? = null

  private var startImageBitmap: Bitmap? = null
  private var endImageBitmap: Bitmap? = null

  // 视觉用的 Progress 值，范围在 [0...1]
  private var visualProgress: Float = 0f
    @Keep
    set(value) {
      field = value
      invalidate()
    }

  private var progress = 0

  var min = 0
    set(value) {
      field = value
      if (progress < value) {
        progress = value
        refreshProgress(progress, false)
      }
    }

  var max = 100
    set(value) {
      field = value
      if (progress > max) {
        progress = max
        refreshProgress(progress, false)
      }
    }

  var duration = 300L

  @ColorInt
  var normalColor = Color.GRAY
    set(value) {
      if (value != field) {
        field = value
        invalidate()
      }
    }

  @ColorInt
  var progressColor = Color.BLUE
    set(value) {
      if (value != field) {
        field = value
        invalidate()
      }
    }

  var progressHeight = 0
    set(value) {
      if (value != field) {
        field = value
        invalidate()
      }
    }

  @DrawableRes
  var startImage = 0
    set(value) {
      if (value != field) {
        startImageBitmap = if (value > 0)
          BitmapFactory.decodeResource(resources, value) else null
        field = value
        invalidate()
      }
    }

  @DrawableRes
  var endImage: Int = 0
    set(value) {
      if (value != field) {
        endImageBitmap = if (value > 0)
          BitmapFactory.decodeResource(resources, value) else null
        field = value
        invalidate()
      }
    }

  init {
    val a = context.obtainStyledAttributes(attrs, R.styleable.StepperProgressBar, 0, 0)
    progress = a.getInt(R.styleable.StepperProgressBar_spb_progress, progress)
    min = a.getInt(R.styleable.StepperProgressBar_spb_min, min)
    max = a.getInt(R.styleable.StepperProgressBar_spb_max, max)
    duration = a.getInt(R.styleable.StepperProgressBar_spb_duration, duration.toInt()).toLong()
    normalColor = a.getColor(R.styleable.StepperProgressBar_spb_normalColor, normalColor)
    progressColor = a.getColor(R.styleable.StepperProgressBar_spb_progressColor, progressColor)
    progressHeight = a.getDimensionPixelSize(R.styleable.StepperProgressBar_spb_progressHeight, progressHeight)
    startImage = a.getResourceId(R.styleable.StepperProgressBar_spb_startImage, startImage)
    endImage = a.getResourceId(R.styleable.StepperProgressBar_spb_endImage, endImage)
    a.recycle()
  }

  @JvmOverloads
  fun setProgress(progress: Int, animate: Boolean = false) = setProgressInternal(progress, animate)

  fun getProgress() = progress

  fun getProgress(distance: Float): Int {
    val scale = distance / width
    val progress = getProgressForScale(scale)
    return Math.round(progress.toDouble()).toInt()
  }

  fun setOnProgressBarChangeListener(listener: OnProgressBarChangeListener) {
    this.progressBarChangeListener = listener
  }

  fun isAnimation(): Boolean {
    return progressAnimator?.isRunning ?: false
  }

  fun isInStartRange(): Boolean {
    val distance = getDistance(progress)
    val stepperRadius = getStepperRadius()
    val startCirclePoint = getStartCirclePoint()
    return distance <= (startCirclePoint.x + stepperRadius)
  }

  fun isInEndRange(): Boolean {
    val distance = getDistance(progress)
    val stepperRadius = getStepperRadius()
    val endCirclePoint = getEndCirclePoint()
    return distance >= (endCirclePoint.x - stepperRadius)
  }

  fun getStepperRadius() = height / 2f

  fun getStartCirclePoint(): PointF {
    val radius = getStepperRadius()
    return PointF(radius, radius)
  }

  fun getEndCirclePoint(): PointF {
    val radius = getStepperRadius()
    val progressWidth = getProgressWidth()
    return PointF(radius + progressWidth, radius)
  }

  private fun setProgressInternal(progress: Int, animate: Boolean = false): Boolean {
    val progressValue = MathUtils.clamp(progress, min, max)

    if (progressValue == this.progress) {
      return false
    }

    this.progress = progressValue
    refreshProgress(progressValue, animate)
    return true
  }

  private fun refreshProgress(progress: Int, animate: Boolean) {
    val scale = getScaleForProgress(progress)

    if (animate) {
      progressAnimator = ObjectAnimator.ofFloat(this, VISUAL_PROGRESS, visualProgress, scale)
      progressAnimator?.let { animator ->
        animator.addUpdateListener { animation ->
          val value = animation.animatedValue as Float
          onProgressRefresh((value * (max - min) + min).toInt())
        }
        animator.setAutoCancel(true)
        animator.interpolator = PROGRESS_ANIM_INTERPOLATOR
        animator.duration = duration
        animator.start()
      }
    } else {
      visualProgress = scale
      onProgressRefresh(progress)
    }
  }

  private fun getScaleForProgress(progress: Int): Float {
    val range = (max - min).toFloat()
    return if (range > 0f) (progress - min) / range else 0f
  }

  private fun getProgressForScale(scale: Float): Int {
    val range = (max - min).toFloat()
    return (scale * range).toInt()
  }

  private fun onProgressRefresh(progress: Int) {
    progressBarChangeListener?.onProgressChanged(this, progress)
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)
    // 使用离屏缓冲
    val saveCount = canvas.saveLayer(null, null, Canvas.ALL_SAVE_FLAG)

    // 绘制底层背景
    paint.color = normalColor
    drawBackground(canvas)

    // 绘制进度
    paint.color = progressColor
    paint.xfermode = XFERMODE
    drawProgress(canvas)
    paint.xfermode = null
    canvas.restoreToCount(saveCount)

    // 绘制两侧图片
    drawBitmaps(canvas)
  }

  private fun drawBackground(canvas: Canvas) {
    val radius = getStepperRadius()
    val startCirclePoint = getStartCirclePoint()
    canvas.drawCircle(startCirclePoint.x, startCirclePoint.y, radius, paint)

    val progressWidth = getProgressWidth()
    val top = height / 2f - progressHeight / 2f
    val right = radius + progressWidth
    val bottom = top + progressHeight
    canvas.drawRect(radius, top, right, bottom, paint)

    val endCirclePoint = getEndCirclePoint()
    canvas.drawCircle(endCirclePoint.x, endCirclePoint.y, radius, paint)
  }

  private fun drawProgress(canvas: Canvas) {
    canvas.drawRect(0f, 0f, width * visualProgress, height.toFloat(), paint)
  }

  private fun drawBitmaps(canvas: Canvas) {
    val radius = getStepperRadius()

    startImageBitmap?.let { bitmap ->
      val left = radius - bitmap.width / 2f
      val top = height / 2f - bitmap.height / 2f
      canvas.drawBitmap(bitmap, left, top, paint)
    }

    endImageBitmap?.let { bitmap ->
      val left = (width - radius) - bitmap.width / 2f
      val top = height / 2f - bitmap.height / 2f
      canvas.drawBitmap(bitmap, left, top, paint)
    }
  }

  private fun getDistance(progress: Int) = width * getScaleForProgress(progress)

  private fun getProgressWidth() = (width - getStepperRadius() * 2f)

  override fun onSaveInstanceState(): Parcelable {
    // 在父类 View 状态的基础上保存当前 View 状态
    val savedState = SavedState(super.onSaveInstanceState())
    savedState.progress = this.progress
    return savedState
  }

  override fun onRestoreInstanceState(state: Parcelable?) {
    if (state is SavedState) {
      // 先恢复父类 View 的状态
      super.onRestoreInstanceState(state.superState)
      // 恢复当前 View 的状态
      setProgress(state.progress)
    } else {
      super.onRestoreInstanceState(state)
    }
  }

  internal class SavedState : BaseSavedState {
    var progress = 0

    constructor(source: Parcel) : super(source) {
      progress = source.readInt()
    }

    constructor(superState: Parcelable?) : super(superState)

    override fun writeToParcel(out: Parcel, flags: Int) {
      super.writeToParcel(out, flags)
      out.writeInt(progress)
    }

    companion object {
      @JvmField
      val CRAETOR = object : Parcelable.Creator<SavedState> {
        override fun createFromParcel(source: Parcel): SavedState {
          return SavedState(source)
        }

        override fun newArray(size: Int): Array<SavedState?> {
          return arrayOfNulls(size)
        }
      }
    }
  }

  companion object {
    private val TAG = StepperProgressBar::class.java.simpleName
    private val PROGRESS_ANIM_INTERPOLATOR = DecelerateInterpolator()
    private val XFERMODE = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    private const val VISUAL_PROGRESS = "visualProgress"
  }

}