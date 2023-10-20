package com.backgroundaudiorecord

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaRecorder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import org.json.JSONObject


data class AudioConfig(val AudioSource: Int?, val OutputFormat: Int?, val AudioEncoder:Int?, val AudioSamplingRate: Int?, val AudioEncodingBitRate:Int?, val AudioChannels:Int?)


class AudioService: Service() {
  var path = "${RNGlobal.reactContext.cacheDir}/sound.mp4"

  var audioSource:Int = MediaRecorder.AudioSource.MIC
  var outputFormat = MediaRecorder.OutputFormat.MPEG_4
  var audioEncoder = MediaRecorder.AudioEncoder.AAC
  var audioSamplingRate = 48000;
  var audioEncodingBitRate = 128000;
  var audioChannels = 2

  var channelName = "DEFAULT";
  var contentTitle = RNGlobal.reactContext.applicationInfo.loadLabel(RNGlobal.reactContext.packageManager).toString();
  var contentText = "recoding now...";

  private val NOTIFICATION_ID = 1

  val recorder = RNRecorder(RNGlobal.reactContext);


  override fun onCreate() {
    super.onCreate()
    resetVariables()
  }

  @RequiresApi(Build.VERSION_CODES.S)
  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    path = intent!!.getStringExtra("path") ?: path

    audioSource = intent!!.getIntExtra("audioSource",MediaRecorder.AudioSource.MIC)
    outputFormat = intent!!.getIntExtra("outputFormat",MediaRecorder.OutputFormat.MPEG_4)
    audioEncoder = intent!!.getIntExtra("audioEncoder",MediaRecorder.AudioEncoder.AAC)
    audioSamplingRate = intent!!.getIntExtra("audioSamplingRate",48000)
    audioEncodingBitRate = intent!!.getIntExtra("audioEncodingBitRate",128000)
    audioChannels = intent!!.getIntExtra("audioChannels",2)

    channelName = intent!!.getStringExtra("channelName") ?: channelName
    contentText = intent!!.getStringExtra("contentText") ?: contentText
    contentTitle = intent!!.getStringExtra("contentTitle") ?: contentTitle

    // make notification channel and build pushNotification
    try {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        createNotificationChannel(channelName)
        val intent = Intent(this,RNGlobal.reactContext.currentActivity!!::class.java).apply {
          Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP )
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE)
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                           .setContentTitle(contentTitle)
                            .setContentText(contentText)
                            .setSmallIcon(RNGlobal.reactContext.resources.getIdentifier("ic_launcher", "mipmap", RNGlobal.reactContext.getPackageName()))
                            .setContentIntent(pendingIntent)
                            .build()
        startForeground(NOTIFICATION_ID, notification)
      }
    } catch (ne: NullPointerException) {
    }

    // start reocrd
    try {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val audioConfig = AudioConfig(audioSource,  outputFormat, audioEncoder, audioSamplingRate, audioEncodingBitRate, audioChannels )
        recorder.startRecording(path, audioConfig);
      }
    } catch (ne: NullPointerException) {
    }


    return START_STICKY
  }

  override fun onDestroy() {
    recorder.stopRecording();
    super.onDestroy()
  }

  @RequiresApi(Build.VERSION_CODES.O)
  private fun createNotificationChannel(channelName:String) {
    val notificationChannel = NotificationChannel(
      CHANNEL_ID,
      channelName,
      NotificationManager.IMPORTANCE_HIGH
    )
    notificationChannel.enableLights(true)
    notificationChannel.enableVibration(true)

    val notificationManager = applicationContext.getSystemService(
      Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(
      notificationChannel)
  }

  private fun resetVariables(){
    path = "${RNGlobal.reactContext.cacheDir}/sound.wav"

    audioSource = MediaRecorder.AudioSource.MIC
    outputFormat = MediaRecorder.OutputFormat.MPEG_4
    audioEncoder = MediaRecorder.AudioEncoder.AAC
    audioSamplingRate = 48000;
    audioEncodingBitRate = 128000;
    audioChannels = 2

    channelName = "DEFAULT";
    contentTitle = RNGlobal.reactContext.applicationInfo.loadLabel(RNGlobal.reactContext.packageManager).toString();
    contentText = "recoding now...";

  }

  override fun onBind(intent: Intent): IBinder? {
    return null
  }

  companion object {
    private const val CHANNEL_ID = "MyForegroundServiceChannel"
  }
}
