import React from 'react';
import { Button, StyleSheet, View } from 'react-native';
import {
  recordOnBackground,
  stopRecord,
} from 'react-native-background-audio-record';
import RNFS from 'react-native-fs';

// const audioRecorderPlayer = new AudioRecorderPlayer();

const App = () => {
  const path = `${RNFS.DownloadDirectoryPath}/sound.mp3`;

  const onPressRecord = async () => {
    recordOnBackground(path);
  };

  const onPressStop = () => stopRecord();

  const onPressRecordPlay = () => {
    // audioRecorderPlayer.startPlayer(path);
  };

  return (
    <View style={styles.background}>
      <Button title="record" onPress={onPressRecord}></Button>
      <Button title="stop" onPress={onPressStop}></Button>
      <Button title="Play" onPress={onPressRecordPlay}></Button>
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
