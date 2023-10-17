import * as React from 'react';

import { StyleSheet, View, Button } from 'react-native';
import {
  recordOnBackground,
  stopRecord,
} from 'react-native-background-audio-record';
import RNFS from 'react-native-fs';

export default function App() {
  const onPressButton = () => {
    const path = `${RNFS.DownloadDirectoryPath}/sound.mp3`;

    recordOnBackground(path);
  };

  const onPressStop = () => {
    stopRecord();
  };

  return (
    <View style={styles.container}>
      <Button title="record" onPress={onPressButton} />
      <Button title="stop" onPress={onPressStop} />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
});
