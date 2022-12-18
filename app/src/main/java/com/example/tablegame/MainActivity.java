package com.example.tablegame;

import android.content.Context;
import android.content.Intent;

import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;


public class MainActivity extends AppCompatActivity {

    EditText etTableNumber;
    Button buttonPlayTableGame;

    final int REQUEST_CODE_START_TABLEGAME_ACTIVITY = 1;
    final int MAX_TABLE_NUMBER = 20;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        buttonPlayTableGame =findViewById(R.id.buttonPlayTableGame);
        etTableNumber = findViewById(R.id.etTableNumber);
        etTableNumber.requestFocus();


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
                    Intent intent = new Intent(MainActivity.this, com.example.tablegame.TableGame.class);
                    intent.putExtra("tableNumber", etTableNumber.getText().toString());
                    startActivity(intent);
                }
            }
        });





    }

}