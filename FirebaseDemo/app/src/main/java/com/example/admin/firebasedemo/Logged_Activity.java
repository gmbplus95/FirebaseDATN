package com.example.admin.firebasedemo;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Logged_Activity extends AppCompatActivity {
    private TextView email_logged;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private ImageView imageView;
    private EditText txtImageName;
    private Uri imgUri;


    public static final String FB_STORAGE_PATH="image/";
    public static final String FB_DATABASE_PATH="image";
    public static final int REQUEST_CODE=1234;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_);
        email_logged= (TextView) findViewById(R.id.Email_logged);
        email_logged.setText(getIntent().getExtras().getString("Email"));
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabaseRef= FirebaseDatabase.getInstance().getReference(FB_DATABASE_PATH);
        imageView= (ImageView) findViewById(R.id.imageView);
        txtImageName= (EditText) findViewById(R.id.txtImageName);

    }

    public void browser_onclick(View view) {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Image"),REQUEST_CODE);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imgUri =data.getData();
            try {
                Bitmap bm= MediaStore.Images.Media.getBitmap(getContentResolver(),imgUri);
                imageView.setImageBitmap(bm);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public String getImagetxt(Uri uri){
        ContentResolver contentResolver =getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }
    @SuppressWarnings("VisibleForTests")
    public void Upload_onclick(View view) {
        if (TextUtils.isEmpty(txtImageName.getText().toString())) {
            txtImageName.setError("Ban Chua Nhap Ten Anh");
            return;
        } else {
            if (imgUri != null) {
                final ProgressDialog dialog = new ProgressDialog(this);
                dialog.setTitle("Uploading...");
                dialog.show();
                StorageReference ref = mStorageRef.child(FB_STORAGE_PATH + System.currentTimeMillis() + "." + getImagetxt(imgUri));
                ref.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Upload Successfully", Toast.LENGTH_SHORT).show();
                        ImageUpload imageUpload = new ImageUpload(txtImageName.getText().toString(), taskSnapshot.getDownloadUrl().toString());
                        String uploadID = mDatabaseRef.push().getKey();
                        mDatabaseRef.child(uploadID).setValue(imageUpload);

                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                dialog.dismiss();
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {

                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                dialog.setMessage("Uploaded " + (int) progress + "%");
                            }
                        });

            } else {
                Toast.makeText(getApplicationContext(), "Please Select Image!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void onBackPressed()
    {
        Intent intent = new Intent(Logged_Activity.this,Login_Activity.class);
        startActivity(intent);
    }

    public void showimg_onclick(View view) {
        Intent i=new Intent(Logged_Activity.this,ImageListActivity.class);
        startActivity(i);
    }
}
