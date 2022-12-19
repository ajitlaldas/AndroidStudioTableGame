package com.example.tablegame;

import android.content.Context;
import android.content.Intent;

import android.net.Uri;
import android.provider.ContactsContract;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity  {

    EditText etTableNumber;
    Button buttonPlayTableGame;

    final int REQUEST_CODE_START_TABLEGAME_ACTIVITY = 1;
    final int MAX_TABLE_NUMBER = 20;
    TextToSpeech tts;
    boolean ttsStatus = false;








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        buttonPlayTableGame =findViewById(R.id.buttonPlayTableGame);
        etTableNumber = findViewById(R.id.etTableNumber);
        etTableNumber.requestFocus();

        /*textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {


                if(i!=TextToSpeech.ERROR){
                    // To Choose language of speech

                    String toastString = "Text to speech initialized " + MAX_TABLE_NUMBER;
                    Toast.makeText(MainActivity.this, toastString, Toast.LENGTH_SHORT).show();
                }
                else {
                    String toastString = "Text to speech not initialized " + MAX_TABLE_NUMBER;
                    Toast.makeText(MainActivity.this, toastString, Toast.LENGTH_SHORT).show();
                }
            }
        });*/



            tts = new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                // TODO Auto-generated method stub
                // TTS is successfully initialized
                if (status == TextToSpeech.SUCCESS) {
                    //tts.speak("Starting Table Game for " + etTableNumber.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);

                    Toast.makeText(MainActivity.this, "TTS Initilization Success", Toast.LENGTH_LONG)
                            .show();
                    // Setting speech language
                    int result = tts.setLanguage(Locale.US);
                    // If your device doesn't support language you set above
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        // Cook simple toast message with message
                        Toast.makeText(getApplicationContext(), "Language not supported",
                                Toast.LENGTH_LONG).show();
                        Log.e("TTS", "Language is not supported");
                    }
                    else {
                                                Toast.makeText(MainActivity.this, "Language Initialized to " + tts.getLanguage().toString(), Toast.LENGTH_LONG)
                                .show();

                    }
                    // Enable the button - It was disabled in main.xml (Go back and
                    // Check it)

                    // TTS is not initialized properly
                } else {
                    Toast.makeText(MainActivity.this, "TTS Initilization Failed", Toast.LENGTH_LONG)
                            .show();
                    Log.e("TTS", "Initilization Failed");
                }
            }
        });




        buttonPlayTableGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                if ((etTableNumber.getText().toString().isEmpty())) {

                    String toastString = "Pl enter some number between 2 & " + MAX_TABLE_NUMBER;
                    Toast.makeText(MainActivity.this, toastString, Toast.LENGTH_SHORT).show();

                } else if ((Integer.parseInt(etTableNumber.getText().toString()) < 2 || Integer.parseInt(etTableNumber.getText().toString()) > MAX_TABLE_NUMBER)) {
                    String toastString = "Pl enter number between 2 & " + MAX_TABLE_NUMBER;
                    Toast.makeText(MainActivity.this, toastString, Toast.LENGTH_SHORT).show();
                } else {
                    tts.speak("Starting Game For " + etTableNumber.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                    while(tts.isSpeaking());
                    /*try {

                        Thread.sleep(1000);
                    } catch (InterruptedException e) {

                    }*/
                    Intent intent = new Intent(MainActivity.this, com.example.tablegame.TableGame.class);
                    intent.putExtra("tableNumber", etTableNumber.getText().toString());
                    tts.shutdown();
                    startActivity(intent);
                }
            }
        });





    }



}