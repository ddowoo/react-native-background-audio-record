export const OutputFormat = {
  DEFAULT: 0,
  THREE_GPP: 1,
  MPEG_4: 2,
  AMR_NB: 3,
  AMR_WB: 4,
  AAC_ADIF: 5,
  AAC_ADTS: 6,
  OUTPUT_FORMAT_RTP_AVP: 7,
  MPEG_2_TS: 8,
  WEBM: 9,
};

type OutputFormat = (typeof OutputFormat)[keyof typeof OutputFormat];

export const AudioSource = {
  DEFAULT: 0,
  MIC: 1,
  VOICE_UPLINK: 2,
  VOICE_DOWNLINK: 3,
  VOICE_CALL: 4,
  CAMCORDER: 5,
  VOICE_RECOGNITION: 6,
  VOICE_COMMUNICATION: 7,
  REMOTE_SUBMIX: 8,
  UNPROCESSED: 9,
  VOICE_PERFORMANCE: 10,
};

type AudioSource = (typeof AudioSource)[keyof typeof AudioSource];

export const AudioEncoder = {
  DEFAULT: 0,
  AMR_NB: 1,
  AMR_WB: 2,
  AAC: 3,
  HE_AAC: 4,
  AAC_ELD: 5,
  VORBIS: 6,
  OPUS: 7,
};

type AudioEncoder = (typeof AudioEncoder)[keyof typeof AudioEncoder];

export type NoticationConfig = {
  channelName?: string;
  contentTitle?: string;
  contentText?: string;
};

export type AudioConfig = {
  audioSource?: AudioSource;
  outputFormat?: OutputFormat;
  audioEncoder?: AudioEncoder;
  audioSamplingRate?: number;
  audioEncodingBitRate?: number;
  audioChannels?: number;
};

export type Recorder = {
  startRecord: (params?: startRecordParams) => void;
  stopRecord: () => void;
  startAudio: (path?: string) => void;
  stopAudio: () => void;
};

export type startRecordParams = {
  path?: string;
  notificationConfig?: NoticationConfig;
  audioConfig?: AudioConfig;
};
