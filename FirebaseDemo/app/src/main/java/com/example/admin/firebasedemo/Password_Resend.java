package com.example.admin.firebasedemo;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Password_Resend extends AppCompatActivity {
    private static final String TAG = "Password_Resend";
    private EditText edtemail;
    private Button btnSent;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password__resend);
        edtemail = findViewById(R.id.edtForgot);
        btnSent = findViewById(R.id.btnSent);
        firebaseAuth = FirebaseAuth.getInstance();

        btnSent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(edtemail.getText().toString()))
                {
                    edtemail.setError("Ban chua nhap Email");
                    return;
                }

                else{
                    firebaseAuth.sendPasswordResetEmail(edtemail.getText().toString().trim())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "Email sent.");
                                        Toast.makeText(Password_Resend.this,"Email Sent. Please check your mail!",Toast.LENGTH_LONG).show();
                                    }
                                    else{
                                        Log.e("Error", task.getException().toString());
                                        Toast.makeText(Password_Resend.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });

    }
}

