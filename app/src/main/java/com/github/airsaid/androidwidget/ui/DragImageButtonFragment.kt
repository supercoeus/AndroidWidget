package com.github.airsaid.androidwidget.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.github.airsaid.androidwidget.R
import com.github.airsaid.androidwidget.listener.SimpleSeekBarChangeListener
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_drag_imagebutton.*

/**
 * @author airsaid
 */
class DragImageButtonFragment : Fragment() {
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_drag_imagebutton, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    isReboundView.setOnCheckedChangeListener { _, isChecked ->
      dragView.isRebound = isChecked
    }

    durationSeek.setOnSeekBarChangeListener(object : SimpleSeekBarChangeListener() {
      override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        super.onProgressChanged(seekBar, progress, fromUser)
        dragView.reboundDuration = progress.toLong()
        durationText.text = "回弹动画时间 ($progress)："
      }
    })

    dragView.setOnClickListener {
      Snackbar.make(dragView, "Click", Snackbar.LENGTH_SHORT).show()
    }
  }
}