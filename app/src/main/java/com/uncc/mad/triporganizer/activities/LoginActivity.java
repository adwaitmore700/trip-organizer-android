package com.uncc.mad.triporganizer.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.uncc.mad.triporganizer.R;
import com.uncc.mad.triporganizer.models.UserProfile;

public class LoginActivity extends AppCompatActivity {
    public static FirebaseAuth mAuth;
    public static GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    private Button loginButton;
    private TextView username;
    private TextView password;
    private TextView signup;
    private ProgressDialog loader;
    FirebaseUser currentUser =null;
    public GoogleSignInAccount account = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        loginButton = findViewById(R.id.la_btn_login);
        username = findViewById(R.id.la_et_username);
        password = findViewById(R.id.la_et_password);
        signup = findViewById(R.id.la_btn_signUp);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(LoginActivity.this, gso);

        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
            }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNull()) {
                    String userName = username.getText().toString();
                    String pass = password.getText().toString();
                    mAuth.signInWithEmailAndPassword(userName, pass)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        loader = ProgressDialog.show(LoginActivity.this, "", "Initializing ...", true);
                                         Intent intent = new Intent(LoginActivity.this, UserProfileActivity.class);
                        startActivity(intent);
                        loader.dismiss();
                        finish();
                                    } else {
                                        Log.d("demo", "signInWithEmail:failure", task.getException());
                                        Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else
                    Toast.makeText(LoginActivity.this, "Fields cannot be blank", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        loader = ProgressDialog.show(LoginActivity.this, "", "Initializing ...", true);
//         currentUser = mAuth.getCurrentUser();
//         account = GoogleSignIn.getLastSignedInAccount(this);
//        if(currentUser !=null || account!=null){
//           startIntent();
//        }
//        else{
//            loader.dismiss();
//            Toast.makeText(LoginActivity.this, "Login to proceed", Toast.LENGTH_SHORT).show();}
//    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public Boolean isNull(){
        if((!username.getText().toString().equals("") && !password.getText().toString().equals(""))
        && (username.getText().toString() != null && password.getText().toString() != null))
        return true;
        else
        return false;
    }
        private void signIn() {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
            mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.d("demo", "signInWithCredential:success");
                        loader = ProgressDialog.show(LoginActivity.this, "", "Initializing ...", true);
                        Intent intent = new Intent(LoginActivity.this, UserProfileActivity.class);
                        startActivity(intent);
                        loader.dismiss();
                        finish();
                    } else {
                        Log.w("demo", "signInWithCredential:failure", task.getException());
                    }
                }
            });
        } catch (ApiException e) {
            Log.w("demo", "signInResult:failed code=" + e.toString());
           Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
        }
    }

//    public void startIntent(){
////        String user = null;
//        currentUser = mAuth.getCurrentUser();
//        account = GoogleSignIn.getLastSignedInAccount(this);
//        user = mAuth.getCurrentUser().getUid();
//        TripProfileActivity.db.collection("Users").document(user)
//        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
//                        startActivity(intent);
//                        loader.dismiss();
//                        finish();
//                    } else {
//
//                        finish();
//                    }
//                } else {
//                    loader.dismiss();
//                    Log.d("demo", "get failed with ", task.getException());
//                }
//            }
//        });
//    }
}
