package com.github.airsaid.androidwidget.listener

import android.widget.SeekBar

/**
 * @author airsaid
 */
open class SimpleSeekBarChangeListener : SeekBar.OnSeekBarChangeListener {
  override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {}

  override fun onStartTrackingTouch(seekBar: SeekBar) {}

  override fun onStopTrackingTouch(seekBar: SeekBar) {}
}