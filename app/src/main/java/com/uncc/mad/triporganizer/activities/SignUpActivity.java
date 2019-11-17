package com.uncc.mad.triporganizer.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.uncc.mad.triporganizer.R;

public class SignUpActivity extends AppCompatActivity {

    private Button signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signupButton = findViewById(R.id.sa_btn_signUp);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, DashboardActivity.class);
                startActivity(intent);
            }
        });
    }
}
