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

const recorder = BgRecorder.recorder();

const App = () => {
  const onPressRecord = async () =>
    recorder.startRecord({ path, notificationConfig, audioConfig });

  const onPressStop = () => recorder.stopRecord();

  const onPressRecordPlay = () => recorder.startAudio(path);

  const onPressStopAudio = () => recorder.stopAudio();

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
