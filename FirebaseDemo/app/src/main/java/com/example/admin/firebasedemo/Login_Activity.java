package com.example.admin.firebasedemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login_Activity extends AppCompatActivity {
    private EditText txtEmailLogin;
    private EditText txtPassLogin;
    private FirebaseAuth firebaseAuth;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);
        txtEmailLogin = (EditText) findViewById(R.id.txt_emailLogin);
        txtPassLogin= (EditText) findViewById(R.id.txt_passwordLogin);
        firebaseAuth=firebaseAuth.getInstance();
        mImageView= (ImageView) findViewById(R.id.imageView);
        mImageView.setImageResource(R.drawable.firebase);
    }
    public void Login_Click(View v){
        if(TextUtils.isEmpty(txtEmailLogin.getText().toString()))
        {
            txtEmailLogin.setError("Ban chua nhap Email");
            return;
        }
        else if(TextUtils.isEmpty(txtPassLogin.getText().toString()))
        {
            txtPassLogin.setError("Ban chua nhap Password");
            return;
        }
        else {
            final ProgressDialog progressDialog = ProgressDialog.show(Login_Activity.this, "Please Wait...", "Processing...", true);
            (firebaseAuth.signInWithEmailAndPassword(txtEmailLogin.getText().toString(), txtPassLogin.getText().toString())).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        Toast.makeText(Login_Activity.this, "Đăng Nhập Thành Công", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(Login_Activity.this, Logged_Activity.class);
                        i.putExtra("Email", firebaseAuth.getCurrentUser().getEmail());
                        startActivity(i);
                    } else {
                        Log.e("Error", task.getException().toString());
                        Toast.makeText(Login_Activity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    public void reg_onclick(View view) {
        Intent i= new Intent(Login_Activity.this,Register_Activity.class);
        startActivity(i);
        finish();
    }

}
