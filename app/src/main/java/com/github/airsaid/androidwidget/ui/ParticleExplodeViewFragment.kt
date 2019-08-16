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
class ParticleExplodeViewFragment : Fragment() {
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_particle_explode, container, false)
  }
}