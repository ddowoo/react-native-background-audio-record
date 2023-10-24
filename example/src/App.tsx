import React from 'react';
import { Button, StyleSheet, View } from 'react-native';
import {
  stopRecord,
  startRecord,
  stopAudio,
  startAudio,
  type NoticationConfig,
  type AudioConfig,
  AudioSource,
  OutputFormat,
  AudioEncoder,
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

const App = () => {
  const onPressRecord = async () =>
    startRecord({ path, notificationConfig, audioConfig });

  const onPressStop = () => stopRecord();

  const onPressRecordPlay = () => startAudio(path);

  const onPressStopAudio = () => stopAudio();

  return (
    <View style={styles.background}>
      <Button title="record" onPress={onPressRecord}></Button>
      <Button title="stop" onPress={onPressStop}></Button>
      <Button title="Play" onPress={onPressRecordPlay}></Button>
      <Button title="Audio Stop" onPress={onPressStopAudio}></Button>
    </View>
  );
};

export default App;

const styles = StyleSheet.create({
  background: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#000',
  },
});
