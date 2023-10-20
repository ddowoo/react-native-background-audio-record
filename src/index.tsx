import { NativeModules, Platform, PermissionsAndroid } from 'react-native';
import type { AudioConfig, NoticationConfig } from './type';
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

export async function startRecord({
  path,
  notificationConfig,
  audioConfig,
}: {
  path?: string;
  notificationConfig?: NoticationConfig;
  audioConfig?: AudioConfig;
} = {}) {
  try {
    if (Platform.OS === 'android') {
      if (await reqeustRecordAndStoragePermission()) {
        BackgroundAudioRecord.startRecord(
          path,
          audioConfig,
          notificationConfig
        );
      }
    }
  } catch (e) {
    console.warn(e);
  }
}

export function stopRecord() {
  if (Platform.OS === 'android') {
    return BackgroundAudioRecord.stopRecord();
  }
}

export function startAudio(path?: string) {
  if (Platform.OS === 'android') {
    BackgroundAudioRecord.playAudio(path);
  }
}

export function stopAudio() {
  if (Platform.OS === 'android') {
    BackgroundAudioRecord.stopAudio();
  }
}
