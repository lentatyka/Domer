package com.example.domer.utilits.engine;

import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;

public class TextSpeechEngine extends TextToSpeech {
    private final Bundle params;
    public TextSpeechEngine(Context context, OnInitListener listener) {
        this(context, listener, TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID);
    }

    public TextSpeechEngine(Context context, OnInitListener listener, String engine) {
        super(context, listener, engine);
        super.setSpeechRate(0.8f);
        params = new Bundle();
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "");
    }

    public void speakIt(String[] answer){
        speak(answer[1], TextToSpeech.QUEUE_FLUSH, params, answer[0]);
    }
}
