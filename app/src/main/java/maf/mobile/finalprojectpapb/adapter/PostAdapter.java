package maf.mobile.finalprojectpapb.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import maf.mobile.finalprojectpapb.R;
import maf.mobile.finalprojectpapb.model.Post;
import maf.mobile.finalprojectpapb.model.User;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ItemVH> {

    private FirebaseUser currentUser;
    private Context context;
    private List<Post> posts;
    private String url = "https://papb-project-final-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private DatabaseReference db = FirebaseDatabase.getInstance(url).getReference();

    public PostAdapter(Activity context, List<Post> posts, FirebaseUser currentUser){
        this.context = context;
        this.posts = posts;
        this.currentUser = currentUser;
    }
    @NonNull
    @Override
    public PostAdapter.ItemVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View card = LayoutInflater.from(context)
                .inflate(R.layout.post_item, parent, false);
        return new ItemVH(card);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ItemVH holder, int position) {
        Post t = this.posts.get(position);
        holder.tvContent.setText(t.getContent());
        holder.tvDate.setText(t.getTimestamp());

        if (t.getContentPhoto() !=null && !t.getContentPhoto().isEmpty()) {
            String imgUrl = t.getContentPhoto();
            Picasso.get().load(imgUrl).into(holder.ivPhotoContent);
            holder.ivPhotoContent.setVisibility(View.VISIBLE);
        }

        db.child("users").orderByChild("id").equalTo(t.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot snapshotUser : snapshot.getChildren()){
                        User users = snapshotUser.getValue(User.class);
                        if (users!=null) {
                            holder.tvProfile.setText("@"+users.getUsername());
                            Picasso.get().load(users.getImgUrl()).into(holder.ivProfile);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ItemVH extends RecyclerView.ViewHolder {
        private ImageView ivPhotoContent;
        private TextView tvDate;
        private CircleImageView ivProfile;
        private TextView tvProfile;
        private TextView tvContent;
        private ImageButton btEdit;
        private ImageButton btDelete;

        public ItemVH(@NonNull View itemView) {
            super(itemView);

            this.ivProfile = (CircleImageView) itemView.findViewById(R.id.ivProfileRecycler);
            this.tvProfile = (TextView) itemView.findViewById(R.id.tvProfileRecycler);
            this.tvContent = (TextView) itemView.findViewById(R.id.tvContentRecycler);
            this.tvDate = (TextView) itemView.findViewById(R.id.tvDateRecycler);
            this.ivPhotoContent = (ImageView) itemView.findViewById(R.id.ivPhotoContent);
            this.btEdit = (android.widget.ImageButton) itemView.findViewById(R.id.btEditRecycler);
            this.btDelete = (android.widget.ImageButton) itemView.findViewById(R.id.btDeleteRecycler);
        }
    }
}
