package com.mymedchalagent.androidhari.mymedchalagent;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mymedchalagent.androidhari.mymedchalagent.Claass.DetailsModel;
import com.mymedchalagent.androidhari.mymedchalagent.Claass.TinyDB;
import com.mymedchalagent.androidhari.mymedchalagent.SupportFiles.ScalingUtilities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class Add_logo extends AppCompatActivity {

    ImageView imageView;
    ProgressDialog pd;
    Button submit;
    private static int RESULT_LOAD_IMAGE = 1;

    TinyDB tinyDB;
    String picturepath;
    String profileimgurl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_logo);
        pd = new ProgressDialog(this);
        tinyDB = new TinyDB(this);
        imageView = (ImageView)findViewById(R.id.imageView);

        submit = (Button)findViewById(R.id.submit);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            pd.setMessage("Uploading your Logo...");
                StorageReference storage = FirebaseStorage.getInstance().getReference();
                //   StorageReference storageRef = storage.getReference();

//        StorageReference mountainsRef = storageRef.child("mountains.jpg");

// Create a reference to 'images/mountains.jpg'
                final StorageReference mountainImagesRef = storage.child("businesslogo/"+tinyDB.getString("key")+".jpg");

// While the file names are the same, the references point to different files
                mountainImagesRef.getName().equals(mountainImagesRef.getName());    // true
                mountainImagesRef.getPath().equals(mountainImagesRef.getPath());    // false
                imageView.setDrawingCacheEnabled(true);
                imageView.buildDrawingCache();
                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();

                UploadTask uploadTask = mountainImagesRef.putBytes(data);



                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return mountainImagesRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            Log.e("Status of Upload",task.getResult().toString());
                            profileimgurl = task.getResult().toString();

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            final DatabaseReference myRef = database.getReference("BusinessLists").child(tinyDB.getString("aarea")).child(tinyDB.getString("key"));
                            DetailsModel category = new DetailsModel();

                            category.setImg(profileimgurl);
                            myRef.child("img").setValue(profileimgurl);
                            pd.dismiss();

                            new AlertDialog.Builder(Add_logo.this)
                                    .setTitle("Success!!!")
                                    .setMessage("We have uploaded your Logo")

                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                    .setPositiveButton("FINISH", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {


                                            startActivity(new Intent(Add_logo.this,Dashpage.class));
                                            // Continue with delete operation'
                                            pd.dismiss();
                                        }
                                    })
                                    .show();
                            //uploadimgurl();
                        } else {
                            pd.dismiss();
                            Toast.makeText(Add_logo.this, "Please Try Again", Toast.LENGTH_SHORT).show();

                            // Handle failures
                            // ...
                        }
                    }
                });
            }
        });
    }

    private void uploadimgurl() {



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

//        addlogo.setText(strMyImagePath);
              imageView.setImageBitmap(BitmapFactory.decodeFile(strMyImagePath));

        return strMyImagePath;


    }
}
