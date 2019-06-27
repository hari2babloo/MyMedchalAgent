package com.mymedchalagent.androidhari.mymedchalagent;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.collect.Range;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mymedchalagent.androidhari.mymedchalagent.Claass.DetailsModel;
import com.mymedchalagent.androidhari.mymedchalagent.Claass.RequestsModel;
import com.mymedchalagent.androidhari.mymedchalagent.Claass.TinyDB;
import com.mymedchalagent.androidhari.mymedchalagent.SupportFiles.ScalingUtilities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Calendar;

public class More_BusinessDetails extends AppCompatActivity {

    private static int RESULT_LOAD_IMAGE = 1;
    TextView Title;
    AwesomeValidation mAwesomeValidation;
    EditText propname,businesname,contact1,contact2,whatsappno,emailid,desc,services,cost,timingfro,timingto,workingdays,yearsince,address,landmark,lane,lat,lng,addlogo;
    LinearLayout Linfo,Laddr;
    String picturepath;
    ProgressDialog pd;

    Button submit,button;
    String imageuploaded;
    TinyDB tinyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_business_details);
        mAwesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        tinyDB = new TinyDB(this);
        pd = new ProgressDialog(this);
        button = (Button)findViewById(R.id.button);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("geo:37.7749,-122.4194");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
        Linfo = (LinearLayout)findViewById(R.id.info);
        Laddr = (LinearLayout)findViewById(R.id.adres);
        Laddr.setVisibility(View.VISIBLE);
        propname = (EditText)findViewById(R.id.propname);
        businesname = (EditText)findViewById(R.id.businesname);
        contact1 = (EditText)findViewById(R.id.contact1);
        contact2 = (EditText)findViewById(R.id.contact2);
        whatsappno = (EditText)findViewById(R.id.whatsapp);
        emailid = (EditText)findViewById(R.id.email);
        desc = (EditText)findViewById(R.id.desc);
        services = (EditText)findViewById(R.id.services);
        cost = (EditText)findViewById(R.id.cost);
        timingfro = (EditText)findViewById(R.id.timinfrom);
        timingto = (EditText)findViewById(R.id.timingto);
        workingdays = (EditText)findViewById(R.id.workingdays);
        yearsince = (EditText)findViewById(R.id.since);
        address = (EditText)findViewById(R.id.address);
        landmark = (EditText)findViewById(R.id.landmark);
        lane = (EditText)findViewById(R.id.lane);
        lat = (EditText)findViewById(R.id.lat);
        lng = (EditText)findViewById(R.id.lon);
        submit = (Button) findViewById(R.id.submit);
        timingfro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(More_BusinessDetails.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        timingfro.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Timing From");
                mTimePicker.show();
            }
        });
        timingto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(More_BusinessDetails.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        timingto.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Timing To");
                mTimePicker.show();

            }
        });

//        addlogo.setFocusable(false);
//        addlogo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(
//                        Intent.ACTION_PICK,
//                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
//                startActivityForResult(i, RESULT_LOAD_IMAGE);
//            }
//        });

        String regex = "[A-Za-z0-9\\s\\!\\\"\\#\\$\\%\\&\\'\\(\\)\\*\\+\\,\\-\\.\\/\\:\\;\\<\\>\\=\\?\\@\\[\\]\\{\\}\\\\\\\\\\^\\_\\`\\~]+$";
        //String regex = regex;
        mAwesomeValidation.addValidation(More_BusinessDetails.this, R.id.propname, "[a-zA-Z\\s]+", R.string.errpropname);
        mAwesomeValidation.addValidation(More_BusinessDetails.this, R.id.businesname, "[a-zA-Z\\s]+", R.string.errbname);
        mAwesomeValidation.addValidation(More_BusinessDetails.this, R.id.contact1, Patterns.PHONE, R.string.errcontact);
        mAwesomeValidation.addValidation(More_BusinessDetails.this, R.id.whatsapp, Patterns.PHONE, R.string.errwhatsap);
        mAwesomeValidation.addValidation(More_BusinessDetails.this, R.id.email, Patterns.EMAIL_ADDRESS, R.string.erremail);
        mAwesomeValidation.addValidation(More_BusinessDetails.this, R.id.desc, regex, R.string.errdesc);
        mAwesomeValidation.addValidation(More_BusinessDetails.this, R.id.services, regex, R.string.errservices);
        mAwesomeValidation.addValidation(More_BusinessDetails.this, R.id.cost, regex, R.string.errcost);
        mAwesomeValidation.addValidation(More_BusinessDetails.this, R.id.timinfrom, regex, R.string.errtmingfrom);
        mAwesomeValidation.addValidation(More_BusinessDetails.this, R.id.timingto, regex, R.string.errtmingto);
        mAwesomeValidation.addValidation(More_BusinessDetails.this, R.id.workingdays, regex, R.string.errworkingdays);
        mAwesomeValidation.addValidation(More_BusinessDetails.this,R.id.address,regex,R.string.erraddr);
        mAwesomeValidation.addValidation(More_BusinessDetails.this, R.id.landmark, regex, R.string.errlandmark);
        mAwesomeValidation.addValidation(More_BusinessDetails.this, R.id.lane, regex, R.string.errlane);
        mAwesomeValidation.addValidation(More_BusinessDetails.this, R.id.lat, regex, R.string.errlat);
        mAwesomeValidation.addValidation(More_BusinessDetails.this, R.id.lon, regex, R.string.errlng);

//        mAwesomeValidation.addValidation(More_BusinessDetails.this, R.id.businesname, RegexTemplate.TELEPHONE, R.string.err_tel);
//        mAwesomeValidation.addValidation(More_BusinessDetails.this, R.id.contact1, android.util.Patterns.EMAIL_ADDRESS, R.string.err_email);
//        mAwesomeValidation.addValidation(More_BusinessDetails.this, R.id.whatsapp, Range.closed(00, Calendar.getInstance().get(Calendar.AM_PM)), R.string.err_year);
//        mAwesomeValidation.addValidation(More_BusinessDetails.this, R.id.edt_height, Range.closed(0.0f, 2.72f), R.string.err_height);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mAwesomeValidation.validate()){

submiphotoanddata();
                }

            }
        });
    }

    private void submiphotoanddata() {

        pd.setMessage("Submitting your Details");
        pd.setCancelable(false);
        pd.show();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("BusinessLists").child(tinyDB.getString("aarea"));
        DetailsModel detailsModel = new DetailsModel();
        //
        detailsModel.setName(businesname.getText().toString());
        detailsModel.setPropname(propname.getText().toString());
        detailsModel.setContact(contact1.getText().toString());
        detailsModel.setContact2(contact2.getText().toString());
        //detailsModel.setContact3("756");
        detailsModel.setEmail(emailid.getText().toString());
        detailsModel.setAwards("No");
        detailsModel.setAddress(address.getText().toString());
        detailsModel.setLocation(tinyDB.getString("aarea"));
        detailsModel.setDescription(desc.getText().toString());
        detailsModel.setLandmark(landmark.getText().toString());
        detailsModel.setLane(lane.getText().toString());
        detailsModel.setLat(lat.getText().toString());
        detailsModel.setLng(lng.getText().toString());
        detailsModel.setWhatsapp(whatsappno.getText().toString());
        detailsModel.setTimingsfrom(timingfro.getText().toString());
        detailsModel.setTimingsto(timingto.getText().toString());
        detailsModel.setWebsite("na");
        detailsModel.setWorkindays(workingdays.getText().toString());
        detailsModel.setImg("imglink");
//        detailsModel.setVideo("Videolink");
  //      detailsModel.setRating("9");
        detailsModel.setSubcategory(tinyDB.getString("subcat"));
        detailsModel.setCost(cost.getText().toString());
        detailsModel.setCategory(tinyDB.getString("cat"));
        detailsModel.setSince(yearsince.getText().toString());
        detailsModel.setBand("na");

        DatabaseReference sss = myRef.push();

        String ss = sss.getKey();

        tinyDB.putString("key",ss);
        Log.e("sss",ss);




        sss.setValue(detailsModel).addOnSuccessListener(new OnSuccessListener<Void>() {


            @Override
            public void onSuccess(Void aVoid) {

  //              String mGroupId = myRef.getKey();




//                Log.e("valuesuploades",mGroupId);



                pd.setMessage("Updating Requests");
                pd.setCancelable(false);
                pd.show();

                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference myRef = database.getReference("requests").child(tinyDB.getString("aarea")).child(tinyDB.getString("requestkey"));
//        RequestsModel requestsModel = new RequestsModel();
//        requestsModel.setVisitdate(selecteddate);
                myRef.child("status").setValue("FINISHED");
                myRef.child("key").setValue(tinyDB.getString("key"));
                myRef.child("statusmsg").setValue("Your Details are Live");
                myRef.child("bname").setValue(businesname.getText().toString());
                myRef.child("subcategory").setValue(tinyDB.getString("subcat"));
                pd.dismiss();


                pd.dismiss();



                pd.setMessage("Creating Seller Profile");
                pd.setCancelable(false);
                pd.show();

                final FirebaseDatabase database2 = FirebaseDatabase.getInstance();
                final DatabaseReference myRef2 = database2.getReference("ServiceProvider").child(tinyDB.getString("uid"));
        RequestsModel requestsModel = new RequestsModel();
        requestsModel.setKey(tinyDB.getString("key"));
        requestsModel.setStatus("OPEN");
        requestsModel.setLocation(tinyDB.getString("aarea"));
        requestsModel.setTimestamp(System.currentTimeMillis());
        requestsModel.setBname(businesname.getText().toString());
                myRef.child("subcategory").setValue(tinyDB.getString("subcat"));
                myRef2.push().setValue(requestsModel);

                pd.dismiss();


                pd.dismiss();

                new AlertDialog.Builder(More_BusinessDetails.this)
                        .setTitle("Success!!!")
                        .setMessage("We have uploaded your Given Information")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {


                                startActivity(new Intent(More_BusinessDetails.this,Add_logo.class));
                                // Continue with delete operation'
                                pd.dismiss();
                            }
                        })
                        .show();
            }


        })

        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("valuesuploades","Failed");
            pd.dismiss();
            }
        })
        ;




    }






    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturepath = cursor.getString(columnIndex);
            cursor.close();

            // ImageView imageView = (ImageView) findViewById(R.id.imgView);

//           profileimg.setImageBitmap(BitmapFactory.decodeFile(picturepath));

            Log.e("picturepath",picturepath);


            decodefile(picturepath,800,800);

        }


    }


    private String decodefile(String path, int DESIREDWIDTH , int DESIREDHEIGHT) {

        String strMyImagePath = null;
        Bitmap scaledBitmap = null;

        try {
            // Part 1: Decode image
            Bitmap unscaledBitmap = ScalingUtilities.decodeFile(path, DESIREDWIDTH, DESIREDHEIGHT, ScalingUtilities.ScalingLogic.CROP);

            if (!(unscaledBitmap.getWidth() <= DESIREDWIDTH && unscaledBitmap.getHeight() <= DESIREDHEIGHT)) {
                // Part 2: Scale image
                scaledBitmap = ScalingUtilities.createScaledBitmap(unscaledBitmap, DESIREDWIDTH, DESIREDHEIGHT, ScalingUtilities.ScalingLogic.FIT);
                //  profileimg.setImageBitmap( scaledBitmap);
//                profileimg.setImageBitmap(BitmapFactory.decodeFile(strMyImagePath));
            } else {
                unscaledBitmap.recycle();
                return path;
            }

            // Store to tmp file

            String extr = Environment.getExternalStorageDirectory().toString();
            File mFolder = new File(extr + "/TMMFOLDER");
            if (!mFolder.exists()) {
                mFolder.mkdir();
            }

            String s = "tmp.png";

            File f = new File(mFolder.getAbsolutePath(), s);

            strMyImagePath = f.getAbsolutePath();
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(f);
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 75, fos);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {

                e.printStackTrace();
            } catch (Exception e) {

                e.printStackTrace();
            }

            scaledBitmap.recycle();
        } catch (Throwable e) {
        }

        if (strMyImagePath == null) {
            return path;
        }

    //    addlogo.setText(strMyImagePath);
  //      profileimg.setImageBitmap(BitmapFactory.decodeFile(strMyImagePath));

        return strMyImagePath;


    }
}
