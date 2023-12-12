package maf.mobile.finalprojectpapb.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import maf.mobile.finalprojectpapb.R;

public class LoginActivity extends AppCompatActivity {

    private TextView signUp;
    private EditText userEmail;
    private EditText userPassword;
    private Button login;
    private FirebaseAuth mAuth;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userEmail = (EditText) findViewById(R.id.etEmail);
        userPassword = (EditText) findViewById(R.id.etPassword);
        progress = findViewById(R.id.progressBar);

        login = (Button) findViewById(R.id.btLogin);

        mAuth = FirebaseAuth.getInstance();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = userEmail.getText().toString();
                final String pass = userPassword.getText().toString();

                userSignin(email, pass);
            }
        });

        signUp = findViewById(R.id.btSignup);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private void userSignin(String email, String pass) {
        if (email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please fill required field", Toast.LENGTH_SHORT);
            showMessage("Please fill required field");
        }else{
            login.setVisibility(View.GONE);
            progress.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                login.setVisibility(View.VISIBLE);
                                progress.setVisibility(View.GONE);
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            userEmail.setError(e.toString());
                            userPassword.setError(e.toString());
                            login.setVisibility(View.VISIBLE);
                            progress.setVisibility(View.GONE);
                        }
                    });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    private void showMessage(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
    }
}