package com.backgroundaudiorecord

import com.facebook.react.bridge.ReactApplicationContext

object RNGlobal {
  lateinit var reactContext: ReactApplicationContext
  var isRecording = false;
}
