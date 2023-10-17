import { NativeModules, Platform, PermissionsAndroid } from 'react-native';
import type { audioConfig, serviceNoticationConfig } from './type';
export * from './type';

async function reqeustRecordAndStoragePermission() {
  try {
    const granted = await PermissionsAndroid.requestMultiple([
      'android.permission.RECORD_AUDIO',
      'android.permission.WRITE_EXTERNAL_STORAGE',
    ]);
    console.log('granted : ', granted);
    if (Object.values(granted).every((value) => value === 'granted'))
      return true;
    else return false;
  } catch (err) {
    console.warn(err);

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

export async function recordOnBackground(
  path?: string,
  notificationConfig?: serviceNoticationConfig,
  audioConfig?: audioConfig
) {
  try {
    if (Platform.OS === 'android') {
      if (await reqeustRecordAndStoragePermission()) {
        BackgroundAudioRecord.recordOnBackground(
          path,
          audioConfig,
          notificationConfig
        ).then((res: string) => {
          console.log('___res');
          console.log(res);
        });
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
