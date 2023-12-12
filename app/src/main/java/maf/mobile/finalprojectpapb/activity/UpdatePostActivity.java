package maf.mobile.finalprojectpapb.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import maf.mobile.finalprojectpapb.R;
import maf.mobile.finalprojectpapb.model.Post;
import maf.mobile.finalprojectpapb.model.User;

public class UpdatePostActivity extends AppCompatActivity {

    private CircleImageView ivProfile;
    private TextView tvProfile;
    private TextView tvDate;
    private EditText etContent;
    private ImageView ivPhotoContent;
    private String url = "https://papb-project-final-default-rtdb.asia-southeast1.firebasedatabase.app/";
    DatabaseReference updateDb = FirebaseDatabase.getInstance(url).getReference();
    private String postId;
    private String userId;
    private TextView disclaimer;
    private Button btSave;
    private Button btCancel;
    private Query postGet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_post);

        ivProfile = (CircleImageView) findViewById(R.id.ivProfileEdit);
        tvProfile = (TextView) findViewById(R.id.tvProfileEdit);
        tvDate = (TextView) findViewById(R.id.tvDateDetail);
        etContent = (EditText) findViewById(R.id.etContentEdit);
        ivPhotoContent = (ImageView) findViewById(R.id.ivPhotoContentEdit);
        btSave = (Button) findViewById(R.id.btSave);
        btCancel = (Button) findViewById(R.id.btCancel);

        this.postId = getIntent().getStringExtra("postId");
        this.userId = getIntent().getStringExtra("userId");

        if (postId == null) {
            finish();
        }

        getEditablePost();
        getUserData();

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String edited = etContent.getText().toString();
                updatePost(edited);
            }
        });



    }

    private void getUserData() {
        updateDb.child("users").orderByChild("id").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot: snapshot.getChildren()) {
                        User users = userSnapshot.getValue(User.class);
                        if (users!=null) {
                            tvProfile.setText("@"+users.getUsername());
                            Picasso.get().load(users.getImgUrl()).into(ivProfile);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getEditablePost() {
        updateDb.child("posts").orderByChild("postId").equalTo(postId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                        Post post = postSnapshot.getValue(Post.class);
                        if (post.getContentPhoto()!=null && !post.getContentPhoto().isEmpty()) {
                            Picasso.get().load(post.getContentPhoto()).into(ivPhotoContent);
                            ivPhotoContent.setVisibility(View.VISIBLE);
                        }
                        etContent.setText(post.getContent());
                        tvDate.setText(post.getTimestamp());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updatePost(String edited) {
        updateDb.child("posts").child(postId).child("content").setValue(edited)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        showMessage("Post has been updated");
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showMessage("Failed to update post");
                    }
                });
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}