package com.mymedchalagent.androidhari.mymedchalagent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mymedchalagent.androidhari.mymedchalagent.Claass.Signin;

public class SampleUpload extends AppCompatActivity {

    DatabaseReference databaseReference;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashpage);

        databaseReference = FirebaseDatabase.getInstance().getReference("Agent");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Signin signin = new Signin();
                signin.setAid("24");
                signin.setAname("hari");
                signin.setApass("12");
                signin.setAarea("Athvelly");
                signin.setAimage("xxxx");

                databaseReference.push().setValue(signin);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
