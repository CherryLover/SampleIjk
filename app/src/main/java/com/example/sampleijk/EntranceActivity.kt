package com.example.sampleijk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sampleijk.databinding.ActivityEntranceBinding

class EntranceActivity : AppCompatActivity() {

  private val binding by lazy { ActivityEntranceBinding.inflate(layoutInflater) }
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(binding.root)

    binding.btnPlayer.setOnClickListener {
      MainActivity.start(this)
    }

    MainActivity.start(this)
  }
}