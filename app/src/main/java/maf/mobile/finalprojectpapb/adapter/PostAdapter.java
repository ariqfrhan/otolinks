package maf.mobile.finalprojectpapb.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import maf.mobile.finalprojectpapb.R;
import maf.mobile.finalprojectpapb.activity.UpdatePostActivity;
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

        if (t.getUserId().equals(currentUser.getUid())) {
            holder.btDelete.setVisibility(View.VISIBLE);
            holder.btDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Confirm Delete");
                    builder.setMessage("Do you want to delete this post?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int adapterPosition = holder.getAdapterPosition();
                            db.child("posts").child(posts.get(adapterPosition).getPostId())
                                    .removeValue()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            StorageReference mStorage = FirebaseStorage.getInstance().getReferenceFromUrl(t.getContentPhoto());
                                            mStorage.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(context, "Post deleted", Toast.LENGTH_SHORT).show();
                                                    notifyItemRemoved(adapterPosition);
                                                }
                                            });
                                        }
                                    });
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });

            holder.btEdit.setVisibility(View.VISIBLE);
            holder.btEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, UpdatePostActivity.class);
                    intent.putExtra("postId", t.getPostId());
                    context.startActivity(intent);
                }
            });
        }


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
