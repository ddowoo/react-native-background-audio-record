# react-native-background-audio-record

This library provides the feature to record when in the background on ` Android`.

This Library use `FOREGROUND_SERVICE` of Android to provide a stable recording function in the background and when the app is turned off.
That is why Push Notification is sent at the same time as recording.
Users can edit the title and content of the Push Notification.

<br/>

## Screenshot

<br/>

## Install

```sh
npm install react-native-background-audio-record
```

<br/>

## Permissions

```xml
<uses-permission android:name="android.permission.RECORD_AUDIO" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />

```

<br/>

## Method

| Method        | Description                                                                  | Param                                                     |
| ------------- | ---------------------------------------------------------------------------- | --------------------------------------------------------- |
| startRecord() | start recording with FOREGROUND_SERVICE and show push notification           | path?:string <br/> audioConfig? <br/> notificationConfig? |
| stopRecord()  | stop recording (push notification will be closed)                            |                                                           |
| playAudio()   | play recording audio if you don't pass path then will play default path file | path? : string                                            |
| stopAudio()   | stop playing audio file                                                      |                                                           |

<br/>

## Examples

you can also check code in Example

```js
import React from 'react';
import { Button, StyleSheet, View } from 'react-native';
import BgRecorder, {
  AudioSource,
  OutputFormat,
  AudioEncoder,
  type NoticationConfig,
  type AudioConfig,
} from 'react-native-background-audio-record';
import RNFS from 'react-native-fs';

const path = `${RNFS.DownloadDirectoryPath}/sound.m4a`;

const notificationConfig: NoticationConfig = {
  channelName: 'name',
  contentText: 'text',
  contentTitle: 'title',
};

const audioConfig: AudioConfig = {
  audioSource: AudioSource.MIC,
  outputFormat: OutputFormat.DEFAULT,
  audioEncoder: AudioEncoder.AAC,
  audioSamplingRate: 48000,
  audioEncodingBitRate: 128000,
  audioChannels: 2,
};
// ---------   recordOnBackground   ---------//
// default path is
// AOS : {cacheDir}/sound.m4a
const recorder = BgRecorder.recorder();

recorder.startRecord({ path, notificationConfig, audioConfig });
recorder.stopRecord();
recorder.startAudio(path);
recorder.stopAudio();
```
