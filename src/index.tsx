import { NativeModules, Platform, PermissionsAndroid } from 'react-native';
import type { Recorder } from './type';
export * from './type';

async function reqeustRecordAndStoragePermission() {
  try {
    const granted = await PermissionsAndroid.requestMultiple([
      'android.permission.RECORD_AUDIO',
      'android.permission.WRITE_EXTERNAL_STORAGE',
    ]);
    if (Object.values(granted).every((value) => value === 'granted'))
      return true;
    else return false;
  } catch (err) {
    return false;
  }
}

const LINKING_ERROR =
  `The package 'react-native-background-audio-record' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const BackgroundAudioRecord = NativeModules.BackgroundAudioRecord
  ? NativeModules.BackgroundAudioRecord
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

const BgRecorder = (function () {
  let instance: Recorder | undefined;
  let isRecording = false;
  let isPlaying = false;

  function getPlayer(): Recorder {
    return {
      startRecord: async function startRecord({
        path,
        notificationConfig,
        audioConfig,
      } = {}) {
        try {
          if (Platform.OS === 'android') {
            if (await reqeustRecordAndStoragePermission()) {
              if (isRecording === false) {
                isRecording = true;
                BackgroundAudioRecord.startRecord(
                  path,
                  audioConfig,
                  notificationConfig
                );
              } else {
                console.warn('already recording...');
              }
            }
          }
        } catch (e) {
          console.warn(e);
        }
      },
      stopRecord: function stopRecord() {
        if (Platform.OS === 'android') {
          isRecording = false;
          return BackgroundAudioRecord.stopRecord();
        }
      },
      startAudio: function startAudio(path?: string) {
        if (Platform.OS === 'android') {
          if (isPlaying === false) {
            isPlaying = true;
            BackgroundAudioRecord.playAudio(path);
          } else {
            console.warn('already playing...');
          }
        }
      },
      stopAudio: function stopAudio() {
        if (Platform.OS === 'android') {
          isPlaying = false;
          BackgroundAudioRecord.stopAudio();
        }
      },
    };
  }

  return {
    recorder: function () {
      if (instance === undefined) {
        instance = getPlayer();
      }
      return instance;
    },
  };
})();

export default BgRecorder;
