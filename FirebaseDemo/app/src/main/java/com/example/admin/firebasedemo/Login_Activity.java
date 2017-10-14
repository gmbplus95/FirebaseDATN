package com.example.admin.firebasedemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.Login;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.facebook.FacebookSdk;
import java.security.MessageDigest;

public class Login_Activity extends AppCompatActivity {
    private EditText txtEmailLogin;
    private EditText txtPassLogin;
    private FirebaseAuth firebaseAuth;
    private ImageView mImageView;
    private LoginButton loginFb;
    private CallbackManager callbackManager;
    String TAG="Login_Activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);
        txtEmailLogin = (EditText) findViewById(R.id.txt_emailLogin);
        txtPassLogin= (EditText) findViewById(R.id.txt_passwordLogin);
        firebaseAuth=firebaseAuth.getInstance();
        mImageView= (ImageView) findViewById(R.id.imageView);
        mImageView.setImageResource(R.drawable.firebase);
        LoginManager.getInstance().logOut();
        callbackManager =CallbackManager.Factory.create();
        loginFb=findViewById(R.id.btnLoginFb);
        callbackManager =CallbackManager.Factory.create();
        loginFb.setReadPermissions("email", "public_profile");
        loginFb.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            Intent i = new Intent(Login_Activity.this, Logged_Activity.class);
                            FirebaseUser user= firebaseAuth.getCurrentUser();
                            i.putExtra("Email", user.getDisplayName());
                            Toast.makeText(Login_Activity.this,"Đăng nhập thành công!",Toast.LENGTH_SHORT).show();
                            startActivity(i);
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(Login_Activity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LoginManager.getInstance().logOut();
    }

    @Override
    public void onBackPressed() {
        LoginManager.getInstance().logOut();
        finish();
    }

}
