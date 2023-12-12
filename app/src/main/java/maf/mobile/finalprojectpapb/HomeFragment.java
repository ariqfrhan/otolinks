package maf.mobile.finalprojectpapb;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import maf.mobile.finalprojectpapb.activity.RegisterActivity;
import maf.mobile.finalprojectpapb.adapter.PostAdapter;
import maf.mobile.finalprojectpapb.model.Post;
import maf.mobile.finalprojectpapb.model.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private EditText etPost;
    private ImageView tvProfile;
    private Button btPost;
    private ImageView btImg;
    private ImageView imgPost;
    private Uri selectedImg;
    private DatabaseReference postDb;
    private List<Post> postData;
    private RecyclerView rvPost;
    private PostAdapter postAdapter;


    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String userId = user.getUid();
    private String url = "https://papb-project-final-default-rtdb.asia-southeast1.firebasedatabase.app/";
    DatabaseReference firebaseDb;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        tvProfile = (ImageView) view.findViewById(R.id.tvProfile);
        etPost = (EditText) view.findViewById(R.id.etPost);
        btImg = (ImageView) view.findViewById(R.id.imageView5);
        btPost = (Button) view.findViewById(R.id.btPost);
        imgPost = (ImageView) view.findViewById(R.id.imgPost);
        rvPost = (RecyclerView) view.findViewById(R.id.rvPost);

        firebaseDb = FirebaseDatabase.getInstance(url).getReference();
        postDb = firebaseDb.child("posts");

        getCurrentUser(user, firebaseDb);

        postData = new ArrayList<>();
        postAdapter = new PostAdapter(getActivity(), postData, user);
        rvPost.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvPost.setAdapter(postAdapter);

        ActivityResultLauncher<Intent> mGetImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                if (o.getResultCode() == Activity.RESULT_OK && o.getData()!=null) {
                    selectedImg = o.getData().getData();
                    imgPost.setImageURI(selectedImg);
                    imgPost.setVisibility(View.VISIBLE);
                }
            }
        });

        btImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                mGetImage.launch(intent);
            }
        });

        btPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = etPost.getText().toString();
                String userId = user.getUid();
                String postId = UUID.randomUUID().toString();
                String date = getDate();
                String[] contentPhoto = new String[1];

                postContent(content, userId, postId, date, contentPhoto);
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        postDb.orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postData.clear();
                for(DataSnapshot postSnapshot : snapshot.getChildren()){
                    Post post = postSnapshot.getValue(Post.class);
                    post.setPostId(postSnapshot.getKey());
                    postData.add(post);
                }
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void postContent(String content, String userId, String postId, String date, String[] contentPhoto) {
        if (selectedImg != null && selectedImg != Uri.EMPTY) {
            StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("post_photos").child(postId);
            mStorage.putFile(selectedImg)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        mStorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                contentPhoto[0] = uri.toString();

                                Post posted = new Post(userId, postId, content, contentPhoto[0], date);
                                postDb.child(postId).setValue(posted).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        showMessage("Successfully posted");
                                        etPost.setText("");
                                        if (selectedImg != null) {
                                            selectedImg = null;
                                            imgPost.setImageURI(null);
                                            imgPost.setVisibility(View.GONE);
                                        }
                                    }
                                });

                            }
                        });
                        }
                    });
        }else{
            Post posted = new Post(userId, postId, content, null, date);
            postDb.child(postId).setValue(posted).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    showMessage("Successfully Posted");
                    etPost.setText("");
                }
            });
        }
    }



    private void getCurrentUser(FirebaseUser user, DatabaseReference dbref) {
        dbref.child("users").orderByChild("id").equalTo(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        User currentUser =  dataSnapshot.getValue(User.class);
                        if (currentUser != null) {
                            Picasso.get().load(user.getPhotoUrl()).into(tvProfile);
                        }
                    }
                }
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
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}