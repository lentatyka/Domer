package com.example.domer;


import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.app.SearchManager;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.domer.databinding.ActivityMainBinding;
import com.example.domer.databinding.ToolbarBinding;
import com.example.domer.interfaces.DialogListener;
import com.example.domer.screens.main.SharedViewModel;
import com.example.domer.screens.main.searchscreen.SearchViewModel;
import com.example.domer.screens.main.signscreen.SignInFragment;
import com.example.domer.utilits.OptionsDialog;
import com.example.domer.utilits.Preferences;
import com.example.domer.utilits.engine.TextSpeechEngine;
import com.example.domer.utilits.engine.CustomResultContract;
import com.example.domer.utilits.engine.Engine;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SignInFragment.Auth,
        DialogListener {
    ToolbarBinding mtoolbar;
    @Nullable
    ActivityMainBinding binding;
    SensorManager sensorManager;
    Sensor sensor;
    SharedViewModel sharedViewModel;
    SearchViewModel searchViewModel;
    SensorEventListener sensorListener;
    UtteranceProgressListener textSpeechListener;
    ActivityResultLauncher<Integer> speechRec;
    Observer<String[]> textSpeechObserver;
    TextSpeechEngine textSpeech;
    Bundle params;
    Preferences sharedPreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //don't lock screen
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        sharedPreferences = Preferences.SHARED_PREFERENCES;
        sharedPreferences.getInstance(this);
        setContentView(binding.getRoot());
    }

    private void setTextSpeech() {
        textSpeech = new TextSpeechEngine(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = textSpeech.setLanguage(Locale.getDefault());
                setTextSpeechListener();
                if (result == TextToSpeech.LANG_MISSING_DATA
                        || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(getApplicationContext(), "Language not supported",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Init failed", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void setTextSpeechListener() {
        textSpeechListener = new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {

            }

            @Override
            public void onDone(String utteranceId) {
                if(utteranceId.equals(Engine.COLOR_SIZE_REQ) ||
                utteranceId.equals(Engine.SIZE_REQ))
                    speechRec.launch(0);
            }

            @Override
            public void onError(String utteranceId) {

            }
        };
        textSpeech.setOnUtteranceProgressListener(textSpeechListener);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearData();
        binding = null;
    }

    private void clearData() {
        if(sharedPreferences.getInitUser()){
            searchViewModel.getTextSpeech().removeObserver(textSpeechObserver);
            sensorManager = null;
            sensor = null;
            textSpeech = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_action_menu, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.requestFocusFromTouch();
        searchView.setFocusable(true);
                searchView.setOnQueryTextListener(
                        new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                if (query == null)
                                    return false;
                                searchView.clearFocus();
                                searchViewModel.setProducts(query);
                                return true;
                            }

                            @Override
                            public boolean onQueryTextChange(String newText) {
                                return false;
                            }
                        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.btm_mic).setVisible(true);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.btm_mic) {
            if(sharedPreferences.getSOUND())
                textSpeech.stop();
            speechRec.launch(0);
        }else if(itemId == R.id.btn_options){
            DialogFragment dialog = new OptionsDialog();
            dialog.show(getSupportFragmentManager(), "NoticeDialogFragment");
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void accept() {
        init();
    }

    private void init() {
        assert binding != null;
        mtoolbar = binding.toolbar;
        setSupportActionBar(mtoolbar.getRoot());
        //TextToSpeech engine
        params = new Bundle();
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "");

        //SpeechRecognizer
        speechRec = registerForActivityResult(new CustomResultContract(),
                result -> sharedViewModel.setScreen(result));

        //Viewmodeles
        textSpeechObserver = s -> {
            if(sharedPreferences.getSOUND())
                textSpeech.speakIt(s);
        };
        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        searchViewModel.getTextSpeech().observe(this, textSpeechObserver);
        //Preferences
        setTextSpeech();

    }

    private void setSensor() {
        if(sensorListener == null){
            sensorListener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    if(event.sensor.getType() == Sensor.TYPE_PROXIMITY &&
                            event.values[0] == 0){
                        if(sharedPreferences.getSOUND() && textSpeech!=null)
                            textSpeech.stop();
                        speechRec.launch(0);
                    }
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {

                }
            };
        }
        if(sensorManager == null)
            sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        if(sensor == null)
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        sensorManager.registerListener(sensorListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onStart() {
        if(sharedPreferences.getSENSOR())
            setSensor();
        if(sharedPreferences.getInitUser())
            init();
        super.onStart();
    }

    @Override
    protected void onStop() {
        if(sharedPreferences.getSENSOR())
            sensorManager.unregisterListener(sensorListener);
        if( textSpeech != null && sharedPreferences.getSOUND())
            textSpeech.stop();
        super.onStop();
    }

    private void unSetSensor(){
        if(sensorListener != null)
            sensorManager.unregisterListener(sensorListener);
        sensorManager = null;
        sensor = null;
        sensorListener = null;
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        //only 2 options "Sensor" "VOICE"
        OptionsDialog od = (OptionsDialog)dialog;
        boolean[] selectedItems = od.getSelectedItems();
        sharedPreferences.setSENSOR(selectedItems[0]);
        sharedPreferences.setSOUND(selectedItems[1]);
        if(selectedItems[0])
            setSensor();
        else
            unSetSensor();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {}
}