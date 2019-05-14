package com.mymedchalagent.androidhari.mymedchalagent;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mymedchalagent.androidhari.mymedchalagent.Claass.Signin;
import com.mymedchalagent.androidhari.mymedchalagent.Claass.TinyDB;

import java.security.cert.CertPathValidatorException;

public class MainActivity extends AppCompatActivity {


    DatabaseReference rootRef;
    TinyDB  tinyDB;
    ProgressDialog pd;
    EditText nametxt;
    EditText passtxt;
    Button submitbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        pd=new ProgressDialog(this);
        tinyDB = new TinyDB(this);
        if (!TextUtils.isEmpty(tinyDB.getString("aid"))){

            startActivity(new Intent(MainActivity.this,Dashpage.class));
        }

        nametxt = (EditText)findViewById(R.id.editText);
        passtxt = (EditText)findViewById(R.id.editText2);
        rootRef = FirebaseDatabase.getInstance().getReference("Agent");
        submitbtn = (Button) findViewById(R.id.button2);


        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             Validate();
            }
        });



    }

    private void Validate() {

 if (TextUtils.isEmpty(nametxt.getText())){

        nametxt.setError("Enter Name");
 }
 else if (TextUtils.isEmpty(passtxt.getText())){

     passtxt.setError("Enter Password");
 }
 else {

     pd.setMessage("Validating your Details..");
     pd.setCancelable(false);

     rootRef.orderByChild("aid").equalTo(nametxt.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
         @Override
         public void onDataChange(DataSnapshot dataSnapshot) {


             if (dataSnapshot.exists()){

                 Log.e("data",dataSnapshot.toString());

                 for (DataSnapshot ds : dataSnapshot.getChildren()){


                     Signin signin = ds.getValue(Signin.class);

                     if (signin.getApass().equalsIgnoreCase(passtxt.getText().toString())){

                         tinyDB.putString("aid",signin.aid);
                         tinyDB.putString("aname",signin.aname);
                         tinyDB.putString("aimage",signin.aimage);
                         tinyDB.putString("aarea",signin.aarea);


                         startActivity(new Intent(MainActivity.this,Dashpage.class));
                     }
                     else
                     {
                         Toast.makeText(MainActivity.this, "Enter Correct Password", Toast.LENGTH_SHORT).show();
                //         Snackbar.make(getWindow().getDecorView().getRootView(), "Enter Correct Password", Snackbar.LENGTH_LONG).show();

                     }

                 }

                 pd.dismiss();

             }

             else {
                 Toast.makeText(MainActivity.this, "Enter Correct Username", Toast.LENGTH_SHORT).show();
//                 Snackbar.make(getWindow().getDecorView().getRootView(), "Enter Correct Username", Snackbar.LENGTH_LONG).show();

                 pd.dismiss();
             }





         }

         @Override
         public void onCancelled(DatabaseError databaseError) {

             Toast.makeText(MainActivity.this, "NO data", Toast.LENGTH_SHORT).show();
//             Snackbar.make(getWindow().getDecorView().getRootView(), "No data", Snackbar.LENGTH_LONG).show();
         }
     });

 }


    }
}
