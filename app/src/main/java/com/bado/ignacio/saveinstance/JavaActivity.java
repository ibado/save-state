package com.bado.ignacio.saveinstance;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.bado.ignacio.safefield.Safe;

public class JavaActivity extends AppCompatActivity {

    @Safe
    String javaField;

    @Safe
    Double aDouble;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
