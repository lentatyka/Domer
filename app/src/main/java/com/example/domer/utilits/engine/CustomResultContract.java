package com.example.domer.utilits.engine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.domer.R;

public class CustomResultContract extends ActivityResultContract<Integer, String> {
    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, Integer val) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        //Use a language model based on free-form speech recognition.
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ru");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, R.string.speak);
        return intent;
    }

    @Override
    public String parseResult(int resultCode, @Nullable Intent result) {
        if (resultCode != Activity.RESULT_OK || result == null) {
            return null;
        }
        return result.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0);
    }
}