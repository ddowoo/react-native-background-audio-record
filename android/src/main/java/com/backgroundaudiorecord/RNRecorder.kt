package com.backgroundaudiorecord

import android.content.pm.PackageManager
import com.facebook.react.bridge.ReactContext
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.facebook.react.bridge.*
import com.facebook.react.modules.core.DeviceEventManagerModule
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import java.util.jar.Manifest
import kotlin.math.log10


class RNRecorder (private val reactContext: ReactContext){

  private var recorder:MediaRecorder? = null;

  @RequiresApi(Build.VERSION_CODES.S)
  fun startRecording(path:String,audioCofnig: AudioConfig) {
    RNGlobal.isRecording = true
    recorder = MediaRecorder(reactContext).apply {
      setAudioSource(audioCofnig?.AudioSource ?: MediaRecorder.AudioSource.MIC)
      setOutputFormat(audioCofnig?.OutputFormat ?: MediaRecorder.OutputFormat.MPEG_4)
      setAudioEncoder(audioCofnig?.AudioEncoder ?: MediaRecorder.AudioEncoder.AAC)
      setAudioSamplingRate(audioCofnig?.AudioSamplingRate ?: 48000)
      setAudioEncodingBitRate(audioCofnig?.AudioEncodingBitRate ?: 128000)
      setAudioChannels(audioCofnig?.AudioChannels ?: 2)
      Log.d("DEV_LOG", path)
      setOutputFile(path)
      try {
        prepare()
      } catch (e: IOException) {

      }
      start()

    }
  }

  fun stopRecording () {
    RNGlobal.isRecording = false
    recorder?.apply {
      stop()
      release()
    }
    recorder = null
  }
}
