package com.example.admin.firebasedemo;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.microsoft.projectoxford.vision.VisionServiceClient;
import com.microsoft.projectoxford.vision.VisionServiceRestClient;
import com.microsoft.projectoxford.vision.contract.AnalysisResult;

import org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


public class Logged_Activity extends AppCompatActivity {
    private VisionServiceClient vsc;
    private TextView email_logged;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private ImageView imageView;
    private EditText txtImageName;
    private Uri imgUri;
    public static final String FB_STORAGE_PATH = "image/";
    public static final String FB_DATABASE_PATH = "image";
    public static final int REQUEST_CODE = 1234;
    private Button btn_check;
    private Button btn_upload;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_);
        email_logged = (TextView) findViewById(R.id.Email_logged);
        email_logged.setText(getIntent().getExtras().getString("Email"));
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(FB_DATABASE_PATH);
        imageView = (ImageView) findViewById(R.id.imageView);
        txtImageName = (EditText) findViewById(R.id.txtImageName);
        btn_check =findViewById(R.id.btn_check);
        btn_upload=findViewById(R.id.button3);
        btn_check.setEnabled(false);
    }

    public void browser_onclick(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), REQUEST_CODE);
        btn_check.setEnabled(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgUri = data.getData();
            try {
                btn_upload.setEnabled(true);
                Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
                imageView.setImageBitmap(bm);
                final ByteArrayInputStream inputStream=new ByteArrayInputStream(getBytesFromBitmap(bm));
                btn_check.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                            final AsyncTask<InputStream, String, String> visionTask = new AsyncTask<InputStream, String, String>() {
                                ProgressDialog mdialog = new ProgressDialog(Logged_Activity.this);
                                @Override
                                protected String doInBackground(InputStream... params) {
                                    try {
                                        if(vsc==null) {
                                            vsc = new VisionServiceRestClient(getString(R.string.sub_key),getString(R.string.end_point));
                                        }
                                        publishProgress("Waiting...");
                                        String[] features = {"Adult"};
                                        String[] details = {};
                                        AnalysisResult result = vsc.analyzeImage(inputStream,features, details);
                                        String strResult = new Gson().toJson(result);
                                        return strResult;
                                    } catch (Exception e) {
                                        return null;
                                    }
                                }

                                @Override
                                protected void onPreExecute() {
                                    mdialog.show();
                                }

                                @Override
                                protected void onPostExecute(String s) {
                                    super.onPostExecute(s);
                                    AnalysisResult result = new Gson().fromJson(s, AnalysisResult.class);
                                    if(result!=null) {
                                        mdialog.dismiss();
                                        TextView txtdetail = findViewById(R.id.check_detail);
                                        if (result.adult.isAdultContent == true) {
                                            txtdetail.setText("Adult Content");
                                            Toast.makeText(Logged_Activity.this, "Ảnh không được chấp nhận!", Toast.LENGTH_SHORT).show();
                                            btn_check.setEnabled(false);
                                            btn_upload.setEnabled(false);
                                        } else if (result.adult.isRacyContent == true) {
                                            txtdetail.setText("Racy Content");
                                            Toast.makeText(Logged_Activity.this, "Ảnh không được chấp nhận!", Toast.LENGTH_SHORT).show();
                                            btn_check.setEnabled(false);
                                            btn_upload.setEnabled(false);
                                        } else {
                                            txtdetail.setText("Accepted !");
                                            Toast.makeText(Logged_Activity.this, "Ảnh được chấp nhận!", Toast.LENGTH_SHORT).show();
                                            btn_upload.setEnabled(true);
                                        }
                                    }
                                    else {
                                        Toast.makeText(Logged_Activity.this,"Try again later!",Toast.LENGTH_LONG).show();
                                        imageView.setImageBitmap(null);
                                        mdialog.dismiss();
                                    }
                                }

                                @Override
                                protected void onProgressUpdate(String... values) {
                                    mdialog.setMessage(values[0]);
                                }
                            };
                            visionTask.execute(inputStream);
                        }


                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    public String getImagetxt(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
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
                StorageReference ref = mStorageRef.child(FB_STORAGE_PATH + txtImageName.getText().toString().trim() + "." + getImagetxt(imgUri));
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

    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Bạn muốn thoát khỏi chương trình?");
        builder.setCancelable(true);
        builder.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        LoginManager.getInstance().logOut();
                        FirebaseAuth.getInstance().signOut();
                        finish();
                    }
                });

        builder.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert12 = builder.create();
        alert12.show();
    }

    public void showimg_onclick(View view) {
        Intent i = new Intent(Logged_Activity.this, ImageListActivity.class);
        startActivity(i);
    }

    @Override
    protected void onResume() {
        btn_upload.setEnabled(false);
        super.onResume();
    }

    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,80, stream);
        return stream.toByteArray();
    }

}




