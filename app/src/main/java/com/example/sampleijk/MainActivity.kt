package com.example.sampleijk

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.SurfaceHolder
import androidx.appcompat.app.AppCompatActivity
import com.example.sampleijk.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import tv.danmaku.ijk.media.player.IMediaPlayer
import tv.danmaku.ijk.media.player.IjkMediaPlayer
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

  companion object {
    private const val TAG = "MainActivity"

    @JvmStatic
    fun start(context: Context) {
      val starter = Intent(context, MainActivity::class.java)
      context.startActivity(starter)
    }
  }

  private var playType = ""

  private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

  private val player by lazy { IjkMediaPlayer() }
  private lateinit var surfaceHolder: SurfaceHolder

  private val playerPrepareListener = IMediaPlayer.OnPreparedListener {
    // duration format: 00:00:00.000
    val duration = it.duration
    val durationStr = String.format(
      "%02d:%02d:%02d.%03d", duration / 1000 / 60 / 60, duration / 1000 / 60 % 60, duration / 1000 % 60, duration % 1000
    )
    toast("you can play $playType duration $durationStr")
  }

  private val playerSizeChangeListener = IMediaPlayer.OnVideoSizeChangedListener { iMediaPlayer, i, i2, i3, i4 ->
    Log.d(TAG, "onVideoSizeChanged: $i, $i2, $i3, $i4")
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(binding.root)

    player.setOnInfoListener { iMediaPlayer, i, i2 ->
      Log.d(TAG, "onInfo: $i, $i2")
      false
    }
    player.setOnControlMessageListener {
      Log.d(TAG, "onControlMessage: $it")
      ""
    }

    setClick()

    player.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 0);

    binding.surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
      override fun surfaceCreated(p0: SurfaceHolder) {
        this@MainActivity.surfaceHolder = p0
      }

      override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
      }

      override fun surfaceDestroyed(p0: SurfaceHolder) {
      }

    })
  }

  private fun reinitPlayer() {
    player.reset()

    player.setDisplay(surfaceHolder)
    player.setOnPreparedListener(playerPrepareListener)
    player.setOnVideoSizeChangedListener(playerSizeChangeListener)
    player.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 0);
  }

  private fun setClick() {
    binding.toolbar.setNavigationOnClickListener {
      finish()
    }

    binding.btnSet.setOnClickListener {
      reinitPlayer()
      playType = "network"
      player.setDataSource("http://192.168.1.11:8089/static/tomandjerry.mp4")
      player.prepareAsync()
    }
    binding.btnPlay.setOnClickListener {
      player.start()
    }

    binding.btnPause.setOnClickListener {
      player.pause()
    }

    binding.btnSetLocal.setOnClickListener {
      reinitPlayer()
      val filePath = "${getExternalFilesDir(Environment.DIRECTORY_MOVIES)?.absolutePath}/tomandjerry.mp4"
      if (!File(filePath).exists()) {
        toast("file no exists")
        return@setOnClickListener
      }
      playType = "local"
      player.setDataSource(filePath)
      player.prepareAsync()
    }

    binding.btnPlayLocal.setOnClickListener {
      player.start()
    }

    binding.btnPauseLocal.setOnClickListener {
      player.pause()
    }

    binding.btnDownload.setOnClickListener {
      downloadFileTo("http://192.168.1.11:8089/static/tomandjerry.mp4", "${getExternalFilesDir(Environment.DIRECTORY_MOVIES)?.absolutePath}/tomandjerry.mp4")
    }
  }

  private fun downloadFileTo(url: String, filePath: String) {
    val localMd5 = FileUtils.getFileMd5(filePath)
    if (localMd5 == "42bb83121576e727e1de77e9facc77c1") {
      Handler(Looper.getMainLooper()).post {
        toast("file already exists")
      }
      return
    }

    thread {
      val connection = URL(url).openConnection() as HttpURLConnection
      connection.connectTimeout = 5000
      connection.requestMethod = "GET"
      connection.connect()
      if (connection.responseCode == 200) {
        val inputStream = connection.inputStream
        val file = File(filePath)
        if (file.exists()) {
          file.delete()
        }
        file.createNewFile()
        val outputStream = file.outputStream()
        val buffer = ByteArray(1024)
        var len = inputStream.read(buffer)
        while (len != -1) {
          outputStream.write(buffer, 0, len)
          len = inputStream.read(buffer)
        }
        outputStream.close()
        inputStream.close()
      }
      Handler(Looper.getMainLooper()).post {
        toast("download success")
      }
    }
  }

  fun toast(text: String) {
    Log.d(TAG, text)
    Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
  }
}