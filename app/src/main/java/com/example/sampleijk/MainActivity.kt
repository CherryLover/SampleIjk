package com.example.sampleijk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import com.example.sampleijk.databinding.ActivityMainBinding
import tv.danmaku.ijk.media.player.IjkMediaPlayer

class MainActivity : AppCompatActivity() {

  companion object {
    private const val TAG = "MainActivity"
  }

  private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

  private val player by lazy { IjkMediaPlayer() }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(binding.root)

//    System.load("libijkffmpeg.so");
//    System.load("libijkplayer.so");
//    System.load("libijksdl.so");


//    player.setOnPreparedListener {
//      Log.d(TAG, "prepared to play");
//      player.start()
//    }

    binding.surfaceView.holder.addCallback(object: SurfaceHolder.Callback {
      override fun surfaceCreated(p0: SurfaceHolder) {
        player.apply {
          setDisplay(p0)
          dataSource = "http://192.168.1.11:8089/static/tomandjerry.mp4"
          _prepareAsync()
        }
      }

      override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
      }

      override fun surfaceDestroyed(p0: SurfaceHolder) {
      }

    })
  }
}