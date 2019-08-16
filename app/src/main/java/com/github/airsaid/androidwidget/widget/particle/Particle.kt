package com.github.airsaid.androidwidget.widget.particle

/**
 * 粒子对象。
 *
 * @author airsaid
 */
class Particle(var x: Float, var y: Float, var angle: Int, var radius: Float) {
  fun reset() {
    x = 0f
    y = 0f
    angle = 0
    radius = 0f
  }
}