package com.backgroundaudiorecord

import android.content.pm.PackageManager
import android.os.Build
import  android.Manifest
import android.content.Intent
import android.media.MediaRecorder
import android.util.Log
import androidx.core.app.ActivityCompat
import com.backgroundaudiorecord.RNGlobal.reactContext
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReadableMap

class BackgroundAudioRecordModule(reactContext: ReactApplicationContext) :
  ReactContextBaseJavaModule(reactContext) {

  var filePath = ""
  lateinit var serviceIntent: Intent;

  override fun getName(): String {
    return NAME
  }

  @ReactMethod
  fun recordOnBackground(path:String?, audioConfig: ReadableMap? ,serviceNotification:ReadableMap, promise: Promise) {
    serviceIntent = Intent(reactContext, AudioService::class.java)
    val appName = RNGlobal.reactContext.applicationInfo.loadLabel(reactContext.packageManager).toString()
    filePath = audioConfig!!.getString("path") ?: "${reactContext.cacheDir}/sound.mp3";

    // pass params to Servcie
    if(audioConfig!=null){
      //filePath
      serviceIntent.putExtra("path",  filePath );

      //AudioConfig
      serviceIntent.putExtra("audioSource",  if( audioConfig.hasKey("audioSource"))  audioConfig.getInt("audioSource") else MediaRecorder.AudioSource.MIC)
      serviceIntent.putExtra("outputFormat", if( audioConfig.hasKey("outputFormat"))  audioConfig.getInt("outputFormat") else  MediaRecorder.OutputFormat.MPEG_4)
      serviceIntent.putExtra("audioEncoder", if( audioConfig.hasKey("audioEncoder"))  audioConfig.getInt("audioEncoder") else  MediaRecorder.AudioEncoder.AAC)
      serviceIntent.putExtra("audioSamplingRate",  if( audioConfig.hasKey("audioSamplingRate"))  audioConfig.getInt("audioSamplingRate") else  48000)
      serviceIntent.putExtra("audioEncodingBitRate", if( audioConfig.hasKey("audioEncodingBitRate"))  audioConfig.getInt("audioEncodingBitRate") else 128000)
      serviceIntent.putExtra("audioChannels", if( audioConfig.hasKey("audioChannels"))  audioConfig.getInt("audioChannels") else 2)

      //NotificationConfig
      serviceIntent.putExtra("channelName",  if( audioConfig.hasKey("channelName"))  audioConfig.getInt("channelName") else "DEFAULT")
      serviceIntent.putExtra("contentText", if( audioConfig.hasKey("contentText"))  audioConfig.getInt("contentText") else appName)
      serviceIntent.putExtra("contentTitle",  if( audioConfig.hasKey("contentTitle"))  audioConfig.getInt("contentTitle") else "recoding now...")
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      reactContext.startForegroundService(serviceIntent)
    } else {
      reactContext.startService(serviceIntent)
    }

    promise.resolve("file:///${filePath}")
  }

  @ReactMethod
  fun stopRecord(){
    reactContext.stopService(serviceIntent)
  }

  companion object {
    const val NAME = "BackgroundAudioRecord"
  }
}
