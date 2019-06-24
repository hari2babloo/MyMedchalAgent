package com.mymedchalagent.androidhari.mymedchalagent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mymedchalagent.androidhari.mymedchalagent.Claass.DetailsModel;
import com.mymedchalagent.androidhari.mymedchalagent.Claass.Signin;
import com.mymedchalagent.androidhari.mymedchalagent.Claass.TinyDB;

public class SampleUpload extends AppCompatActivity {

    DatabaseReference databaseReference;
    TinyDB tinyDB;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashpage);
        tinyDB = new TinyDB(this);
        databaseReference = FirebaseDatabase.getInstance().getReference("BusinessLists/"+tinyDB.getString("aarea"));

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                        DetailsModel detailsModel  = new DetailsModel();

        detailsModel.setName("MM Dhaba");
        detailsModel.setPropname("hari");
        detailsModel.setContact("1321");
        detailsModel.setContact2("9856");
        detailsModel.setContact3("756");
        detailsModel.setEmail("Hari@gmail.com");
        detailsModel.setAwards("BEst if best");
        detailsModel.setAddress("Raghavendra Nagar");
        detailsModel.setLocation("Athvelly");
        detailsModel.setDescription("Best Mobile in market");
        detailsModel.setLandmark("Water Tank");
        detailsModel.setLane("raghavendra nagar");
        detailsModel.setLat("0.0000");
        detailsModel.setLng("0.0000");
        detailsModel.setWhatsapp("9666");
        detailsModel.setTimingsfrom("10:00 am");
        detailsModel.setTimingsto("6:00 pm");
        detailsModel.setWebsite("www.google.com");
        detailsModel.setWorkindays("Mon-Fri");
        detailsModel.setImg("imglink");
        detailsModel.setVideo("Videolink");
        detailsModel.setRating("9");
        detailsModel.setSubcategory("Dhaba");
        detailsModel.setCost("$$");
        detailsModel.setCategory("Food");
        detailsModel.setSince("1999");
        detailsModel.setBand("na");
        databaseReference.push().setValue(detailsModel);

                databaseReference.push().setValue(detailsModel);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
