package com.mymedchalagent.androidhari.mymedchalagent;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mymedchalagent.androidhari.mymedchalagent.Claass.CategoryModel;
import com.mymedchalagent.androidhari.mymedchalagent.Claass.SubCategoryModel;
import com.mymedchalagent.androidhari.mymedchalagent.Claass.TinyDB;

import java.util.ArrayList;

public class Add_Business extends AppCompatActivity {


    ProgressDialog pd;
    DatabaseReference databaseReference;
    TinyDB tinyDB;

        ArrayList<String> catlist = new ArrayList<>();
        ArrayList<String> subcatlist = new ArrayList<>();
    TextView locationtxt;
    Spinner categoryspinner,subcategoryspinner;
    Button next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__business);
        pd=new ProgressDialog(this);
        tinyDB = new TinyDB(this);
        locationtxt = (TextView)findViewById(R.id.location);
        categoryspinner = (Spinner)findViewById(R.id.spinner);
        subcategoryspinner = (Spinner)findViewById(R.id.spinner2);
        next = (Button)findViewById(R.id.button3);
        subcategoryspinner.setVisibility(View.GONE);
        categoryspinner.setVisibility(View.GONE);
        next.setVisibility(View.GONE);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        catlist.add("Select a Category");
        subcatlist.add("Select a SubCategory");

fillcategory();
//fillsubcategory();


    }

    private void fillcategory() {

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Category");
        databaseReference.keepSynced(true);
//        catlist.clear();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                Log.e("Count ", "" + dataSnapshot.toString());
               String  datasnapshot = dataSnapshot.toString();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {


                    //  String eventID= dataSnapshot.child("Hospitals").getKey();
                    String eventID = ds.getKey();
                    CategoryModel post = ds.getValue(CategoryModel.class);
                    Log.e("Get Data", ds.getValue().toString());

                    catlist.add(post.getName());
                    Log.d("TAG", eventID);


                }


                Log.d("ARAY", String.valueOf(catlist));


                bindcatspinner();
              //  fillsubcategory();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    private void bindcatspinner() {

        categoryspinner.setVisibility(View.VISIBLE);
      //  pd.dismiss();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, catlist);
//        adapter.setDropDownViewResource(R.layout.appbar_spinner_dropdown);
        categoryspinner.setAdapter(adapter);
        categoryspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("Spinner", catlist.get(position));
                String spinnerlocation = catlist.get(position);
                tinyDB.putString("cat", spinnerlocation);

                if (position>0){



                    fillsubcategory();

                }
                else {
                    subcategoryspinner.setVisibility(View.GONE);
                }


            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void fillsubcategory() {

    DatabaseReference  mDatabase =  FirebaseDatabase.getInstance().getReference().child("SubCategory");
        mDatabase.orderByChild("category").equalTo(tinyDB.getString("cat")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

  //              catlist.clear();
                Log.e("Count ", "" + dataSnapshot.toString());
                String  datasnapshot = dataSnapshot.toString();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {


                    //  String eventID= dataSnapshot.child("Hospitals").getKey();
                    String eventID = ds.getKey();
                    SubCategoryModel post = ds.getValue(SubCategoryModel.class);
                    Log.e("Get Data", ds.getValue().toString());
                    subcatlist.add(post.getName());
                    Log.d("TAG", eventID);


                }


                Log.d("ARAY", String.valueOf(subcatlist));


                bindsubcatspinner();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });


    }

    private void bindsubcatspinner() {

        subcategoryspinner.setVisibility(View.VISIBLE);
      //  pd.dismiss();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, subcatlist);
        adapter.setDropDownViewResource(R.layout.appbar_spinner_dropdown);
        subcategoryspinner.setAdapter(adapter);
        subcategoryspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("Spinner", subcatlist.get(position));
                String spinnerlocation = subcatlist.get(position);


                if (position>0){

                    next.setVisibility(View.VISIBLE);
                }
                else {

                    next.setVisibility(View.GONE);
                }
               // tinyDB.putString("subcat", spinnerlocation);

               // fillsubcategory();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public  void next(View v){

        startActivity(new Intent(Add_Business.this,More_BusinessDetails.class));
    }
}
