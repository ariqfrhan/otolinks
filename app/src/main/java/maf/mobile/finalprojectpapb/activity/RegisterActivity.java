package maf.mobile.finalprojectpapb.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import maf.mobile.finalprojectpapb.R;
import maf.mobile.finalprojectpapb.model.User;

public class RegisterActivity extends AppCompatActivity {

    private EditText regUsername;
    private EditText regEmail;
    private EditText regPassword;
    private EditText regPhone;
    private Button btRegister;
    private FirebaseAuth mAuth;
    private String url = "https://papb-project-final-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private DatabaseReference firebaseDb;
    private Button btBack;
    private Uri defaultImg = Uri.parse("gs://papb-project-final.appspot.com/user_photos/profiledummy.png");
    private DatabaseReference registerDb;

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
        firebaseDb = FirebaseDatabase.getInstance(url).getReference();
        registerDb = firebaseDb.child("users");

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
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        String userId = user.getUid();
                                        User registerUser = new User(userId, username,email, phone, defaultImg.toString());
                                        registerDb.child(userId).setValue(registerUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(RegisterActivity.this, "Registrasi akun berhasil", Toast.LENGTH_SHORT).show();
                                                UserProfileChangeRequest profileCreate = new UserProfileChangeRequest.Builder()
                                                        .setDisplayName(username)
                                                        .setPhotoUri(defaultImg)
                                                        .build();
                                                user.updateProfile(profileCreate);
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
        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}


