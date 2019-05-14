package com.mymedchalagent.androidhari.mymedchalagent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Dashpage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashpage);
    }

    public void add(View v){

        startActivity(new Intent(Dashpage.this,Add_Business.class));

        Toast.makeText(Dashpage.this, "Clicked", Toast.LENGTH_SHORT).show();
    }
}
