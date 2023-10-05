package com.example.definitely_not_spotify

import android.os.Bundle
import androidx.activity.ComponentActivity
import android.media.MediaPlayer
import android.os.Handler

import android.widget.Toast
import android.widget.SeekBar
import com.example.definitely_not_spotify.databinding.LayoutBinding

class MainActivity : ComponentActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var runnable: Runnable
    private var handler: Handler = Handler()
    private var pause: Boolean = false
    private lateinit var layoutBind: LayoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutBind = LayoutBinding.inflate(layoutInflater)
        val view = layoutBind.root

        setContentView(view)

        // Start the media player


        layoutBind.playBtn.setOnClickListener {
            if (pause) {
                mediaPlayer.seekTo(mediaPlayer.currentPosition)
                mediaPlayer.start()
                pause = false
                Toast.makeText(this, "media playing", Toast.LENGTH_SHORT).show()
            } else {

                mediaPlayer = MediaPlayer.create(applicationContext, R.raw.nggyu)
                mediaPlayer.start()
                Toast.makeText(this, "media playing", Toast.LENGTH_SHORT).show()

            }
            initializeSeekBar()
            layoutBind.playBtn.isEnabled = false
            layoutBind.pauseBtn.isEnabled = true
            layoutBind.stopBtn.isEnabled = true

            mediaPlayer.setOnCompletionListener {
                layoutBind.playBtn.isEnabled = true
                layoutBind.pauseBtn.isEnabled = false
                layoutBind.stopBtn.isEnabled = false
                Toast.makeText(this, "end", Toast.LENGTH_SHORT).show()

            }
        }

        // Pause the media player
        layoutBind.pauseBtn.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                pause = true
                layoutBind.playBtn.isEnabled = true
                layoutBind.pauseBtn.isEnabled = false
                layoutBind.stopBtn.isEnabled = true
                Toast.makeText(this, "media pause", Toast.LENGTH_SHORT).show()
            }
        }
        // Stop the media player
        layoutBind.stopBtn.setOnClickListener {
            if (mediaPlayer.isPlaying || pause.equals(true)) {
                pause = false
                layoutBind.seekBar.setProgress(0)
                mediaPlayer.stop()
                mediaPlayer.reset()
                mediaPlayer.release()
                handler.removeCallbacks(runnable)

                layoutBind.playBtn.isEnabled = true
                layoutBind.pauseBtn.isEnabled = false
                layoutBind.stopBtn.isEnabled = false
                layoutBind.tvPass.text = ""
                layoutBind.tvDue.text = ""
                Toast.makeText(this, "media stop", Toast.LENGTH_SHORT).show()
            }
        }

        // Seek bar change listener
        layoutBind.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                if (b) {
                    mediaPlayer.seekTo(i * 1000)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })
    }

    // Method to initialize seek bar and audio stats
    fun initializeSeekBar() {
        layoutBind.seekBar.max = mediaPlayer.seconds

        runnable = Runnable {
            layoutBind.seekBar.progress = mediaPlayer.currentSeconds

            layoutBind.tvPass.text = "${mediaPlayer.currentSeconds} sec"
            val diff = mediaPlayer.seconds - mediaPlayer.currentSeconds
            layoutBind.tvDue.text = "$diff sec"

            handler.postDelayed(runnable, 1000)
        }
        handler.postDelayed(runnable, 1000)
    }
}

// Creating an extension property to get the media player time duration in seconds
val MediaPlayer.seconds: Int
    get() {
        return this.duration / 1000
    }
// Creating an extension property to get media player current position in seconds
val MediaPlayer.currentSeconds: Int
    get() {
        return this.currentPosition / 1000
    }






