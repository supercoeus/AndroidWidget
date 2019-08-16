package com.github.airsaid.androidwidget.widget.particle

import androidx.core.util.Pools

/**
 * 粒子缓存池。
 *
 * @author airsaid
 */
class ParticlePool(val maxPoolSize: Int) {

  private val particlePool = Pools.SimplePool<Particle>(maxPoolSize)

  fun acquire(x: Float, y: Float, angle: Int, radius: Float): Particle {
    // 使用缓存池获取一个实例
    var particle = particlePool.acquire()
    if (particle == null) {
      // 如果缓存池中没有缓存，那我们需要直接 new 一个
      particle = Particle(x, y, angle, radius)
    } else {
      particle.x = x
      particle.y = y
      particle.angle = angle
      particle.radius = radius
    }
    return particle
  }

  fun release(particle: Particle?) {
    particle?.let {
      // 重置粒子
      particle.reset()
      // 通知缓存池释放实例
      particlePool.release(particle)
    }
  }

}