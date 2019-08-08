package com.github.airsaid.androidwidget.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.airsaid.androidwidget.R
import com.github.airsaid.androidwidget.widget.OnProgressBarChangeListener
import com.github.airsaid.androidwidget.widget.StepperProgressBar
import kotlinx.android.synthetic.main.fragment_stepperprogressbar.*
import kotlin.random.Random

/**
 * @author airsaid
 */
class StepperProgressBarFragment : Fragment(), OnProgressBarChangeListener {
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_stepperprogressbar, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    progressBar.setOnProgressBarChangeListener(this)
    button.setOnClickListener {
      val progress = Random.nextInt(100)
      progressBar.setProgress(progress, true)
    }
  }

  override fun onProgressChanged(progressBar: StepperProgressBar, progress: Int) {
    Log.d("StepperProgressBar", "onProgressChanged: $progress")
    button.text = getString(R.string.set_progress).plus("(").plus(progress.toString()).plus(")")
    progressBar.endImage = if (progressBar.isInEndRange())
      android.R.drawable.ic_dialog_email else android.R.drawable.ic_dialog_alert
  }
}