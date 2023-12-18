package com.backgroundaudiorecord

import com.facebook.react.bridge.ReactContext
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import androidx.annotation.RequiresApi
import com.facebook.react.bridge.*
import java.io.IOException
import java.util.*



class RNRecorder (private val reactContext: ReactContext){
  companion object {
    var isRecording = false;
    private var recorder:MediaRecorder? = null;
    private var player: MediaPlayer? = null;
  }


  @RequiresApi(Build.VERSION_CODES.S)
  fun startRecording(path:String,audioCofnig: AudioConfig) {
    if(isRecording == false){
      isRecording = true
      recorder = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) MediaRecorder(reactContext) else MediaRecorder()

      recorder?.apply {
        setAudioSource(audioCofnig?.AudioSource ?: MediaRecorder.AudioSource.MIC)
        setOutputFormat(audioCofnig?.OutputFormat ?: MediaRecorder.OutputFormat.MPEG_4)
        setAudioEncoder(audioCofnig?.AudioEncoder ?: MediaRecorder.AudioEncoder.AAC)
        setAudioSamplingRate(audioCofnig?.AudioSamplingRate ?: 48000)
        setAudioEncodingBitRate(audioCofnig?.AudioEncodingBitRate ?: 128000)
        setAudioChannels(audioCofnig?.AudioChannels ?: 2)
        setOutputFile(path)

        try {
          prepare()
          start()
        }catch (e:IOException){

        }

     }
    }
  }

  fun stopRecording () {
    try {
      if(isRecording){
        isRecording = false
        recorder?.apply {
          stop()
          release()
        }
        recorder = null
      }
    }catch (e:IOException){

    }

  }

   fun startPlaying(path:String) {

     player = MediaPlayer().apply {
      try {
        setDataSource(path)
        prepare()
        start()
      } catch (e: IOException) {
      }
    }
  }

   fun stopPlaying() {
    player?.release()
    player = null
  }
}
