package maf.mobile.finalprojectpapb.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import maf.mobile.finalprojectpapb.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText regUsername;
    private EditText regEmail;
    private EditText regPassword;
    private EditText regPhone;
    private Button btRegister;
    private FirebaseAuth mAuth;
    private Button btBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        regUsername = (EditText) findViewById(R.id.etRegUsername);
        regEmail = (EditText) findViewById(R.id.etRegEmail);
        regPhone = (EditText) findViewById(R.id.etRegPhone);
        regPassword = (EditText) findViewById(R.id.etRegPassword);
        btRegister= (Button) findViewById(R.id.btRegister);
        btBack = (Button) findViewById(R.id.btBack);

        mAuth = FirebaseAuth.getInstance();

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = regEmail.getText().toString();
                final String username = regUsername.getText().toString();
                final String password = regPassword.getText().toString();
                final String phone = regPhone.getText().toString();

                if (email.isEmpty() || username.isEmpty() || password.isEmpty() || phone.isEmpty()) {
                    showMessage("Please fill all fields");
                }else{
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        UserProfileChangeRequest profileCreate = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(username)
                                                .build();

                                        mAuth.getCurrentUser().updateProfile(profileCreate)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            showMessage("The account has been created");
                                                        }
                                                    }
                                                });

                                        finish();
                                    }else{
                                        showMessage("Failed to create account" + task.getException().getMessage());
                                    }
                                }
                            });
                }
            }
        });
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
    }
}


