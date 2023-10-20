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

  val recorder = RNRecorder(reactContext);

  var filePath =  "${RNGlobal.reactContext.cacheDir}/sound.mp4"
  lateinit var serviceIntent: Intent;

  override fun getName(): String {
    return NAME
  }

  @ReactMethod
  fun startRecord(path:String?, audioConfig: ReadableMap? ,serviceNotification:ReadableMap?, promise: Promise) {

    Log.d("DEV_LOG", path.toString())
    Log.d("DEV_LOG", audioConfig.toString())
    Log.d("DEV_LOG", serviceNotification.toString())

    if(RNGlobal.isRecording == false){
      serviceIntent = Intent(reactContext, AudioService::class.java)

      val appName = RNGlobal.reactContext.applicationInfo.loadLabel(reactContext.packageManager).toString()

      // pass params to Servcie
      //filePath
      serviceIntent.putExtra("path", path ?:  filePath );

      //AudioConfig
      if(audioConfig != null){
       serviceIntent.putExtra("audioSource",  if( audioConfig.hasKey("audioSource"))  audioConfig.getInt("audioSource") else MediaRecorder.AudioSource.MIC)
       serviceIntent.putExtra("outputFormat", if( audioConfig.hasKey("outputFormat"))  audioConfig.getInt("outputFormat") else  MediaRecorder.OutputFormat.MPEG_4)
       serviceIntent.putExtra("audioEncoder", if( audioConfig.hasKey("audioEncoder"))  audioConfig.getInt("audioEncoder") else  MediaRecorder.AudioEncoder.AAC)
       serviceIntent.putExtra("audioSamplingRate",  if( audioConfig.hasKey("audioSamplingRate"))  audioConfig.getInt("audioSamplingRate") else  48000)
       serviceIntent.putExtra("audioEncodingBitRate", if( audioConfig.hasKey("audioEncodingBitRate"))  audioConfig.getInt("audioEncodingBitRate") else 128000)
       serviceIntent.putExtra("audioChannels", if( audioConfig.hasKey("audioChannels"))  audioConfig.getInt("audioChannels") else 2)

     }
      //NotificationConfig
     if(serviceNotification != null){
       serviceIntent.putExtra("channelName",  if( serviceNotification.hasKey("channelName"))  serviceNotification.getString("channelName") else "DEFAULT")
       serviceIntent.putExtra("contentText", if( serviceNotification.hasKey("contentText"))  serviceNotification.getString("contentText") else appName)
       serviceIntent.putExtra("contentTitle",  if( serviceNotification.hasKey("contentTitle"))  serviceNotification.getString("contentTitle") else "recoding now...")
     }

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        reactContext.startForegroundService(serviceIntent)
       } else {
       reactContext.startService(serviceIntent)
      }

      promise.resolve(filePath)
    }
  }

  @ReactMethod
  fun stopRecord(){
    reactContext.stopService(serviceIntent)
  }

  @ReactMethod
  fun playAudio(path: String?){
    if(RNGlobal.isRecording == false){

      recorder.startPlaying(path ?: filePath)
    }
  }

  @ReactMethod
  fun stopAudio(){
      recorder.stopPlaying();
  }

  companion object {
    const val NAME = "BackgroundAudioRecord"
  }
}
