package com.example.tablegame;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaDescrambler;
import android.media.MediaPlayer;
import android.provider.MediaStore;

import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/*class TableSound{

    static int [] sounds = {R.raw.en_num_ja
            ,R.raw.en_num_01, R.raw.en_num_02, R.raw.en_num_03, R.raw.en_num_04
            ,R.raw.en_num_05, R.raw.en_num_06, R.raw.en_num_07, R.raw.en_num_08
            ,R.raw.en_num_09, R.raw.en_num_10, R.raw.en_num_11, R.raw.en_num_12
            ,R.raw.en_num_13, R.raw.en_num_14, R.raw.en_num_15, R.raw.en_num_16
            ,R.raw.en_num_17, R.raw.en_num_18, R.raw.en_num_19, R.raw.en_num_20
    };

    /*static {
        int i =0;
        for (Field field : R.raw.class.getFields()) {
            try {
                sounds[i] = field.getInt();
            } catch (IllegalAccessException e) {

            }
            i++;
        }



    static MediaPlayer[] createSound(Context context, int tableNumber, int tableMultiplier){
        MediaPlayer[] media = new MediaPlayer[3];
        media[0] = MediaPlayer.create(context, sounds[tableNumber]);
        media[1] = MediaPlayer.create(context, sounds[tableMultiplier]);
        media[2] = MediaPlayer.create(context, sounds[0]);
        return media;
    }

    static void playSound(MediaPlayer [] media){
        media[0].start();

        media[0].setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer media1) {
                media[1].start();
            }

        });


        media[1].setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer media1) {
                media[2].start();
            }

        });

        media[2].setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer media1) {
                media[0].release();
                media[1].release();
                media[2].release();
            }

        });
    }

    static void releaseSound(MediaPlayer [] media){
        media[0].release();
        media[1].release();
        media[2].release();
    }


}*/

public class TableGame extends AppCompatActivity {


    LinearLayout tablePlayAreaLayout;
    LinearLayout choiceLayout;
    TextView [] tvTableRow = new TextView[10];
    TextView [] tvChoiceBox = new TextView[5];
    int tvTableRowCurrentIndex = 0, tvChoiceBoxCurrentIndex = 0;
    MediaPlayer [] media = new MediaPlayer[3];
    MediaPlayer gameStatusSound =new MediaPlayer();
    ImageView ivGameStatus;

    TextToSpeech tts;


    boolean gameON = false;
    static final int PURPLE = 0xFF9E02B8;
    static final int GREEN = 0xFF76FF03;
    static final int ORANGE = 0xFFFF5722;
    static final int BUFF = 0xFFED6868;
    static final int MUSTARD_LIGHT = 0x7CFF9100;

    static final int GAME_MODE_LEARN = 0;
    static final int GAME_MODE_KID = 1;
    static final int GAME_MODE_CHALLENGE = 2;
    static final int GAME_MODE_CHALLENGE_PLUS = 3;
    static final int GAME_MODE_CHALLENGE_PLUS_PLUS = 4;

    static int tableNumber;
    static boolean isSoundOn = true;
    static int gameMode = GAME_MODE_KID;

    static boolean correctAnswerClicked =false;
    static boolean wrongAnswerClicked = false;

    static int tableMultiplier = 1;

    static List<Integer> choiceList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_game);
        Intent data = getIntent();
        tableNumber= Integer.parseInt(data.getStringExtra("tableNumber"));


        tablePlayAreaLayout = findViewById(R.id.tablePlayAreaLayout);
        tablePlayAreaLayout.setVerticalScrollBarEnabled(true);
        choiceLayout = findViewById(R.id.choiceLayout);

        ivGameStatus = findViewById(R.id.ivGameStatus);
        ivGameStatus.setVisibility(View.GONE);

        tableNumber= Integer.parseInt(data.getStringExtra("tableNumber"));


        //tvTableRow is array[10] of TableRow Views. Here we are initializing it & setting visibility to GONE
        for (tvTableRowCurrentIndex=0; tvTableRowCurrentIndex<10; tvTableRowCurrentIndex++){
            tvTableRow[tvTableRowCurrentIndex] = (TextView) (tablePlayAreaLayout.getChildAt(tvTableRowCurrentIndex));
            tvTableRow[tvTableRowCurrentIndex].setVisibility(View.GONE);

        }
        tvTableRowCurrentIndex = 0;

        //tvChocieBox is array[5] of Choice Box Views. Here we are initializing it & setting visibility to GONE.
        //Also OnClickListeners are set for all choice boxes to gather if correct answer is clicked by player.
        for (tvChoiceBoxCurrentIndex=0; tvChoiceBoxCurrentIndex<5; tvChoiceBoxCurrentIndex++){
            tvChoiceBox[tvChoiceBoxCurrentIndex] = (TextView) (choiceLayout.getChildAt(tvChoiceBoxCurrentIndex));
            tvChoiceBox[tvChoiceBoxCurrentIndex].setVisibility(View.GONE);
            tvChoiceBox[tvChoiceBoxCurrentIndex].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view1) {
                    TextView tv1 = (TextView) view1;
                    if (Integer.parseInt(tv1.getText().toString()) == tableNumber*tableMultiplier){
                        correctAnswerClicked = true;
                        wrongAnswerClicked = false;
                        playTableGame();
                    }
                    else {
                        correctAnswerClicked = false;
                        wrongAnswerClicked = true;
                        playTableGame();
                    }

                }

            });
        }
        tvChoiceBoxCurrentIndex = 0;

        /*for (tvTableRowCurrentIndex=0; tvTableRowCurrentIndex<10; tvTableRowCurrentIndex++){
            tvTableRow[tvTableRowCurrentIndex].setVisibility(View.GONE);
        }
        tvTableRowCurrentIndex = 0;*/

        /*for (tvChoiceBoxCurrentIndex=0; tvChoiceBoxCurrentIndex<5; tvChoiceBoxCurrentIndex++){
            tvChoiceBox[tvChoiceBoxCurrentIndex].setVisibility(View.GONE);
        }
        tvChoiceBoxCurrentIndex = 0;

        if(gameON) {
            TableSound.releaseSound(media);
        }

        */





        if (isSoundOn){
            tts = new TextToSpeech(TableGame.this, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {

                    if (status == TextToSpeech.SUCCESS) {
                        //tts.speak("Starting Table Game for " + etTableNumber.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);

                        //Toast.makeText(TableGame.this, "TTS Initilization Success", Toast.LENGTH_LONG).show();

                        // Setting speech language

                        int result = tts.setLanguage(Locale.UK);
                        // If your device doesn't support language you set above
                        if (result == TextToSpeech.LANG_MISSING_DATA
                                || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                            // Cook simple toast message with message
                            Toast.makeText(getApplicationContext(), "Language not supported",
                                    Toast.LENGTH_LONG).show();
                            Log.e("TTS", "Language is not supported");
                        }
                        else {
                            Toast.makeText(TableGame.this, "Language Initialized to " + tts.getLanguage().toString(), Toast.LENGTH_LONG)
                                    .show();

                            ivGameStatus.setImageResource(R.drawable.gameison);
                            ivGameStatus.setVisibility(View.VISIBLE);

                            ScaleAnimation scaleAnimation = new ScaleAnimation(0.1f, 1.0f, 0.1f, 1.0f);
                            scaleAnimation.setDuration(500l);
                            ivGameStatus.startAnimation(scaleAnimation);

                            //media = TableSound.createSound(TableGame.this, tableNumber, tableMultiplier);
                            //TableSound.playSound(media);
                            playTableGame();

                        }
                        // Enable the button - It was disabled in main.xml (Go back and
                        // Check it)

                        // TTS is not initialized properly
                    } else {
                        Toast.makeText(TableGame.this, "TTS Initilization Failed", Toast.LENGTH_LONG)
                                .show();
                        Log.e("TTS", "Initilization Failed");
                    }
                }
            });
        }



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tts.shutdown();
        gameStatusSound.release();
    }

    protected void playTableGame(){

        if (gameMode == GAME_MODE_KID){


            if(!gameON){

                //tts.setSpeechRate(0.2f);
                try {

                    Thread.sleep(700);
                } catch (InterruptedException e) {
                }

                tableMultiplier = 1;
                correctAnswerClicked = false;
                wrongAnswerClicked = false;
                gameON = true;

                tvTableRowCurrentIndex = 0;
                tvChoiceBoxCurrentIndex = 0;

                tvTableRow[tvTableRowCurrentIndex].setText(createTableRowText());
                tvTableRow[tvTableRowCurrentIndex].setBackgroundColor(PURPLE);//
                tvTableRow[tvTableRowCurrentIndex].setTextColor(GREEN);
                tvTableRow[tvTableRowCurrentIndex].setVisibility(View.VISIBLE);
                tts.speak(tableNumber + "..  " + tableMultiplier + "'s aa", TextToSpeech.QUEUE_FLUSH, null);

                AlphaAnimation alphaAnimation = new AlphaAnimation(0.1f, 1.0f);
                alphaAnimation.setDuration(600l);
                tvTableRow[tvTableRowCurrentIndex].setAlpha(1.0f);
                tvTableRow[tvTableRowCurrentIndex].startAnimation(alphaAnimation);

                setChoiceList();

                for (tvChoiceBoxCurrentIndex = 0; tvChoiceBoxCurrentIndex < 5; tvChoiceBoxCurrentIndex++) {
                    tvChoiceBox[tvChoiceBoxCurrentIndex].setText(choiceList.get(tvChoiceBoxCurrentIndex).toString());
                    tvChoiceBox[tvChoiceBoxCurrentIndex].setVisibility(View.VISIBLE);
                }
            }

            if (gameON) {

                if (wrongAnswerClicked) {
                    gameStatusSound = MediaPlayer.create(TableGame.this, R.raw.gunshot);
                    gameStatusSound.start();
                    ivGameStatus.setImageResource(R.drawable.no);
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.6f, 1.0f, 0.6f, 1.0f);
                    scaleAnimation.setDuration(500l);
                    ivGameStatus.startAnimation(scaleAnimation);
                }
                else if ((tableMultiplier < 10) && (correctAnswerClicked)) {
                    ivGameStatus.setImageResource(R.drawable.welldone);
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.6f, 1.0f, 0.6f, 1.0f);
                    scaleAnimation.setDuration(500l);
                    ivGameStatus.startAnimation(scaleAnimation);

                    tvTableRow[tvTableRowCurrentIndex].setText(createTableRowText());
                    tvTableRow[tvTableRowCurrentIndex].setBackgroundColor(GREEN);
                    tvTableRow[tvTableRowCurrentIndex].setTextColor(PURPLE);



                    tableMultiplier++;
                    tvTableRowCurrentIndex++;
                    correctAnswerClicked = false;

                    setChoiceList();
                    for (tvChoiceBoxCurrentIndex = 0; tvChoiceBoxCurrentIndex < 5; tvChoiceBoxCurrentIndex++) {
                        tvChoiceBox[tvChoiceBoxCurrentIndex].setText(choiceList.get(tvChoiceBoxCurrentIndex).toString());
                        //tvChoiceBox[tvChoiceBoxCurrentIndex].setVisibility(View.VISIBLE);
                    }
                    gameStatusSound = MediaPlayer.create(TableGame.this, R.raw.correct);
                    gameStatusSound.start();
                    try {

                        Thread.sleep(700);
                    } catch (InterruptedException e) {
                    }
                    tvTableRow[tvTableRowCurrentIndex].setText(createTableRowText());
                    tvTableRow[tvTableRowCurrentIndex].setBackgroundColor(PURPLE);
                    tvTableRow[tvTableRowCurrentIndex].setTextColor(GREEN);
                    tvTableRow[tvTableRowCurrentIndex].setVisibility(View.VISIBLE);

                    AlphaAnimation alphaAnimation = new AlphaAnimation(0.1f, 1.0f);
                    alphaAnimation.setDuration(600l);
                    tvTableRow[tvTableRowCurrentIndex].setAlpha(1.0f);
                    tvTableRow[tvTableRowCurrentIndex].startAnimation(alphaAnimation);


                    tts.speak(tableNumber + "  ..  " + tableMultiplier + "'s aa", TextToSpeech.QUEUE_FLUSH, null);
                    //TableSound.playSound(media);
                }

                else if ((tableMultiplier == 10) && correctAnswerClicked) {
                    tvTableRow[tvTableRowCurrentIndex].setBackgroundColor(GREEN);
                    tvTableRow[tvTableRowCurrentIndex].setTextColor(PURPLE);
                    tvTableRow[tvTableRowCurrentIndex].setText(createTableRowText());

                    ivGameStatus.setImageResource(R.drawable.welldone2);
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.1f, 1.0f, 0.1f, 1.0f);
                    scaleAnimation.setDuration(500l);
                    ivGameStatus.startAnimation(scaleAnimation);

                    gameON = false;
                    gameStatusSound = MediaPlayer.create(TableGame.this, R.raw.greatwelldone);
                    gameStatusSound.start();
                    try {

                        Thread.sleep(2500);
                    } catch (InterruptedException e) {
                    }

                    gameStatusSound = MediaPlayer.create(TableGame.this, R.raw.claps);
                    gameStatusSound.start();
                }
            }
        }


    }




    String createTableRowText(){
        String returnString;
        if (gameON == true && correctAnswerClicked == true){
            returnString = " " + tableNumber + " X " + tableMultiplier + " = " + tableNumber*tableMultiplier;
        }
        else {
            returnString = " " + tableNumber + " X " + tableMultiplier + " = ";
        }
        return returnString;

    }

    public static void setChoiceList() {


        choiceList.clear();
        choiceList.add((tableNumber)*(tableMultiplier+1));
        choiceList.add((tableNumber)*(tableMultiplier-1));
        choiceList.add((tableNumber-1)*(tableMultiplier));
        choiceList.add((tableNumber+1)*(tableMultiplier));
        choiceList.add(tableNumber*tableMultiplier);
        Collections.shuffle(choiceList);
    }
}