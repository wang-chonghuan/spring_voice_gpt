package com.waltwang.maivc.pojo;

import lombok.Data;
import java.util.List;

@Data
public class TextToSpeechRequestJson {
    private AudioConfig audioConfig;
    private Input input;
    private Voice voice;

    @Data
    public static class AudioConfig {
        private String audioEncoding;
        private List<String> effectsProfileId;
        private int pitch;
        private float speakingRate;
    }

    @Data
    public static class Input {
        private String text;
    }

    @Data
    public static class Voice {
        private String languageCode;
        private String name;
    }
}

/*
{
  "audioConfig": {
    "audioEncoding": "LINEAR16",
    "effectsProfileId": [
      "small-bluetooth-speaker-class-device"
    ],
    "pitch": 0,
    "speakingRate": 1
  },
  "input": {
    "text": "你好,我随时可以和你聊天."
  },
  "voice": {
    "languageCode": "cmn-CN",
    "name": "cmn-CN-Standard-A"
  }
}
*/
