package com.example.sampleijk

import tv.danmaku.ijk.media.player.misc.IMediaDataSource
import java.io.File
import java.io.RandomAccessFile

/**
 * @description
 * @author: Created jiangjiwei in 2023/7/7 07:07
 */
class LocalFileDataSource(filePath: String): IMediaDataSource {

  private val randomAccessFile by lazy { RandomAccessFile(File(filePath), "r") }

  override fun readAt(position: Long, byteArray: ByteArray, offset: Int, size: Int): Int {
    if (size <= 0) {
      return 0
    }
    randomAccessFile.seek(position)
    return randomAccessFile.read(byteArray, offset, size)
  }

  override fun getSize(): Long {
    return randomAccessFile.length()
  }

  override fun close() {
    randomAccessFile.close()
  }
}