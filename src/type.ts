export type serviceNoticationConfig = {
  channelName?: string;
  contentTitle?: string;
  contentText?: string;
};

export type audioResource = {
  audioSource?: number;
  audioOutputFormat?: number;
  audioEncoder?: number;
  audioSamplingRate?: number;
  audioEncodingBitRate?: number;
  audioChannels?: number;
};

export type audioConfig = {
  audioSource?: number;
  OutputFormat?: number;
  audioEncoder?: number;
  audioSamplingRate?: number;
  audioEncodingBitRate?: number;
  audioChannels?: number;
};
