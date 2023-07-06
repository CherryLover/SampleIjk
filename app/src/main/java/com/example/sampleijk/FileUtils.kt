package com.example.sampleijk

import java.io.File

/**
 * @description
 * @author: Created jiangjiwei in 2023/7/7 07:18
 */
object FileUtils {
  fun getFileName(url: String): String {
    return url.substring(url.lastIndexOf("/") + 1)
  }

  fun getFileMd5(path: String): String {
    val file = File(path)
    if (!file.exists()) {
      return ""
    }
    return file.inputStream().use { inputStream ->
      val md5 = java.security.MessageDigest.getInstance("MD5")
      val buffer = ByteArray(1024 * 1024 * 10)
      var len = inputStream.read(buffer)
      while (len > 0) {
        md5.update(buffer, 0, len)
        len = inputStream.read(buffer)
      }
      val digest = md5.digest()
      val sb = StringBuilder()
      for (b in digest) {
        val i = b.toInt() and 0xff
        val hexString = Integer.toHexString(i)
        if (hexString.length < 2) {
          sb.append("0")
        }
        sb.append(hexString)
      }
      sb.toString()
    }
  }
}