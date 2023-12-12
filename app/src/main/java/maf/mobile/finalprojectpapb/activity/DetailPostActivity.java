package maf.mobile.finalprojectpapb.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.units.qual.C;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import maf.mobile.finalprojectpapb.R;
import maf.mobile.finalprojectpapb.adapter.CommentAdapter;
import maf.mobile.finalprojectpapb.model.Comment;
import maf.mobile.finalprojectpapb.model.Post;
import maf.mobile.finalprojectpapb.model.User;

public class DetailPostActivity extends AppCompatActivity {

    private CircleImageView ivProfile;
    private TextView tvProfile;
    private TextView tvDate;
    private TextView tvContent;
    private EditText etComment;
    private Button btComment;

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String url = "https://papb-project-final-default-rtdb.asia-southeast1.firebasedatabase.app/";
    DatabaseReference detailDb = FirebaseDatabase.getInstance(url).getReference();
    String postId;
    String userId;
    private ImageView ivPhotoContent;
    private DatabaseReference commentDb;
    private List<Comment> commentData;
    private CommentAdapter commentAdapter;
    private RecyclerView rvComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_post);

        ivProfile = (CircleImageView) findViewById(R.id.ivProfileDetail);
        tvProfile = (TextView) findViewById(R.id.tvProfileDetail);
        tvDate = (TextView) findViewById(R.id.tvDateDetail);
        tvContent = (TextView) findViewById(R.id.tvContentDetail);
        etComment = (EditText) findViewById(R.id.etComment);
        btComment = (Button) findViewById(R.id.btComment);
        ivPhotoContent = (ImageView) findViewById(R.id.ivPhotoContentDetail);
        rvComment = (RecyclerView) findViewById(R.id.rvComment);

        commentDb = detailDb.child("comment");

        this.postId = getIntent().getStringExtra("postId");
        this.userId = getIntent().getStringExtra("userId");

        if (postId == null) {
            finish();
        }

        commentData = new ArrayList<>();
        commentAdapter = new CommentAdapter(this, commentData, user);
        rvComment.setLayoutManager(new LinearLayoutManager(this));
        rvComment.setAdapter(commentAdapter);



        detailDb.child("posts").orderByChild("postId").equalTo(postId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for(DataSnapshot postSnapshot : snapshot.getChildren()){
                        Post post = postSnapshot.getValue(Post.class);
                        if (post.getContentPhoto() !=null && !post.getContentPhoto().isEmpty()) {
                            String imgUrl = post.getContentPhoto();
                            Picasso.get().load(imgUrl).into(ivPhotoContent);
                            ivPhotoContent.setVisibility(View.VISIBLE);
                        }
                        tvContent.setText(post.getContent());
                        tvDate.setText(post.getTimestamp());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        detailDb.child("users").orderByChild("id").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
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

        btComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = etComment.getText().toString();
                String userId = user.getUid();
                String commentId = UUID.randomUUID().toString();
                String date = getDate();

                Comment komen = new Comment(commentId, userId, comment, date);
                commentDb.child(commentId).setValue(komen).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        showMessage("Successfuly Comment");
                        etComment.setText("");
                    }
                });

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        commentDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentData.clear();
                for (DataSnapshot commentSnapshot : snapshot.getChildren()) {
                    Comment comments = commentSnapshot.getValue(Comment.class);
                    comments.setCommentId(commentSnapshot.getKey());
                    commentData.add(comments);
                }
                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private String getDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
        return dateFormat.format(new Date());
    }
    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}