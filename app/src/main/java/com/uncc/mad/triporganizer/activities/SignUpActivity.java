package com.uncc.mad.triporganizer.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.uncc.mad.triporganizer.R;

public class SignUpActivity extends AppCompatActivity {

    private Button signupButton;
    private FirebaseAuth mAuth;
    EditText username,password,confirmPassword;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        username = findViewById(R.id.sa_et_username);
        password = findViewById(R.id.sa_et_password);
        confirmPassword = findViewById(R.id.sa_et_confirmPassword);
        mAuth = FirebaseAuth.getInstance();
        signupButton = findViewById(R.id.sa_btn_signUp);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = username.getText().toString();
                String pass = password.getText().toString();
                String confirmPass = confirmPassword.getText().toString();
                if (validate()) {
                    mAuth.createUserWithEmailAndPassword(userName, pass)
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SignUpActivity.this, "Sign Up Successfull", Toast.LENGTH_SHORT).show();
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Intent intent = new Intent(SignUpActivity.this, UserProfileActivity.class);
                                        startActivity(intent);

                                    } else {
                                        Toast.makeText(SignUpActivity.this, "Sign Up failed.",Toast.LENGTH_SHORT).show();
                                        Log.d("demo", "createUserWithEmail:failure", task.getException());
                                    }
                                }
                            });
                } else
                    Toast.makeText(SignUpActivity.this, "Invalid", Toast.LENGTH_SHORT).show();
            }


        });
    }
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser !=null){
            Intent intent = new Intent(SignUpActivity.this, UserProfileActivity.class);
            startActivity(intent);
        }

    }

    public boolean validate(){
        if(username.getText().toString()!=null && password.getText().toString()!=null
        && confirmPassword.getText().toString()!=null
                && !username.getText().toString().equals("") && !password.getText().toString().equals("")
        && !confirmPassword.getText().toString().equals("") && password.getText().toString().equals(confirmPassword.getText().toString()))
            return true;
            else
        return false;
    }

}
