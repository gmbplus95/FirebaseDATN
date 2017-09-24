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

import org.w3c.dom.Text;

public class Register_Activity extends AppCompatActivity {
    private EditText txtEmail;
    private EditText password;
    private FirebaseAuth firebaseAuth;
    private ImageView  imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_);
        txtEmail= (EditText) findViewById(R.id.txt_emailRegister);
        password= (EditText) findViewById(R.id.txt_passwordRegister);
        firebaseAuth=FirebaseAuth.getInstance();
        imageView= (ImageView) findViewById(R.id.imageView2);
        imageView.setImageResource(R.drawable.firebase);
    }
    public void Register_click(View v){
        if(TextUtils.isEmpty(txtEmail.getText().toString()) )
        {
            txtEmail.setError("Ban chua nhap Email");
            return;
        }
        else if((TextUtils.isEmpty(password.getText().toString()))){
            password.setError("Ban chua nhap Password");
            return;
        }
        else {
            final ProgressDialog progressDialog = ProgressDialog.show(Register_Activity.this, "Please Wait...", "Processing...", true);

            (firebaseAuth.createUserWithEmailAndPassword(txtEmail.getText().toString(), password.getText().toString())).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        Toast.makeText(Register_Activity.this, "Đăng Kí Thành Công", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(Register_Activity.this, Login_Activity.class);
                        startActivity(i);
                        finish();
                    } else {
                        Log.e("ERROR", task.getException().toString());
                        Toast.makeText(Register_Activity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                    }
                }
            });
        }
    }
    public void onBackPressed()
    {
        Intent intent = new Intent(Register_Activity.this,Login_Activity.class);
        startActivity(intent);
    }
}
