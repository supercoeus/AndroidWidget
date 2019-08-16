package com.github.airsaid.androidwidget.widget.particle

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.animation.DecelerateInterpolator
import androidx.core.math.MathUtils
import java.util.*

/**
 * 粒子发射器。
 *
 * @author airsaid
 */
class ParticleEmitter private constructor(builder: Builder, draw: IParticleDraw) {

  private val mMaxCount = builder.maxCount
  private val mMinCount = builder.minCount
  private val mMaxRadius = builder.maxRadius
  private val mMinRadius = builder.minRadius
  private val mColor = builder.color
  private val mDuration = builder.duration

  private val mDraw = draw
  private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
  private var mAnimator: ValueAnimator? = null

  private val mParticles = mutableListOf<Particle>()
  private val mParticlePool: ParticlePool
  private var mParticleFactor = 0f
  private var mRandom = Random()

  init {
    mParticlePool = ParticlePool(mMaxCount)
    mPaint.color = mColor
  }

  /** 在指定的位置开始发送粒子 */
  fun send(x: Float, y: Float) {
    stopAnimation()
    initParticles(x, y)
    startAnimation()
  }

  /** 在指定的位置开始发送粒子 */
  fun onDraw(canvas: Canvas) {
    if (!isAnimation()) return

    mPaint.alpha = (255f - 255f * mParticleFactor).toInt()
    mParticles.forEach { particle ->
      val oldX = particle.x
      val oldY = particle.y
      val angle = particle.angle
      val radius = particle.radius
      val offsetX = radius * Math.cos(Math.toRadians(angle.toDouble())).toFloat()
      val offsetY = radius * Math.sin(Math.toRadians(angle.toDouble())).toFloat()
      val x = oldX + offsetX
      val y = oldY + offsetY
      canvas.drawCircle(x, y, radius, mPaint)
      particle.x = x
      particle.y = y
    }
  }

  private fun isAnimation(): Boolean {
    return mAnimator?.isRunning ?: false
  }

  // 初始化指定数量的粒子并设置初始位置
  private fun initParticles(x: Float, y: Float) {
    mParticles.forEach { mParticlePool.release(it) }
    mParticles.clear()

    var angle = 0
    val number = MathUtils.clamp(
        mMinCount + mRandom.nextInt(mMaxCount - mMinCount), mMinCount, mMaxCount
    )

    for (i in 0 until number) {
      val radius = MathUtils.clamp(
          mMinRadius + mRandom.nextInt(mMaxRadius - mMinRadius), mMinRadius, mMaxRadius
      ).toFloat()
      mParticles.add(mParticlePool.acquire(x, y, angle, radius))
      // 根据粒子数量将粒子均匀分布在每个角度
      angle = (angle + 360f / number).toInt()
    }
  }

  private fun startAnimation() {
    mAnimator = ValueAnimator.ofFloat(0f, 1f)
    mAnimator?.let { animator ->
      animator.addUpdateListener { animation ->
        mParticleFactor = animation.animatedValue as Float
        mDraw.invalidate()
      }
      animator.interpolator = DECELERATE_INTERPOLATOR
      animator.duration = mDuration
      animator.start()
    }
  }

  private fun stopAnimation() {
    mAnimator?.cancel()
  }

  class Builder(val draw: IParticleDraw) {

    /** 每次发射最大粒子数量 */
    var maxCount = 30
    /** 每次发射最小粒子数量 */
    var minCount = 20
    /** 每次发射最大粒子大小 */
    var maxRadius = 14
    /** 每次发射最小粒子大小 */
    var minRadius = 14
    /** 粒子颜色 */
    var color = Color.BLACK
    /** 动画持续动画时间 */
    var duration = 500L

    fun build() = ParticleEmitter(this, draw)
  }

  companion object {
    private val DECELERATE_INTERPOLATOR = DecelerateInterpolator()

    inline fun build(draw: IParticleDraw, block: Builder.() -> Unit) = Builder(draw).apply(block).build()
  }
}
