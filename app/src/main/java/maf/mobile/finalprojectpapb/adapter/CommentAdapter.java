package maf.mobile.finalprojectpapb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import maf.mobile.finalprojectpapb.R;
import maf.mobile.finalprojectpapb.model.Comment;
import maf.mobile.finalprojectpapb.model.User;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ItemVH> {

    private Context context;
    private List<Comment> comments;
    private FirebaseUser currentUser;
    private String url = "https://papb-project-final-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private DatabaseReference db = FirebaseDatabase.getInstance(url).getReference();


    public CommentAdapter(Context context, List<Comment> comments, FirebaseUser currentUser){
        this.context = context;
        this.comments = comments;
        this.currentUser = currentUser;
    }
    @NonNull
    @Override
    public CommentAdapter.ItemVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View card = LayoutInflater.from(context)
                .inflate(R.layout.post_item, parent,false);
        return new ItemVH(card);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ItemVH holder, int position) {
        Comment c = this.comments.get(position);
        holder.tvContent.setText(c.getComment());
        holder.tvDate.setText(c.getTimestamp());

        db.child("users").orderByChild("id").equalTo(c.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot: snapshot.getChildren()) {
                        User users = userSnapshot.getValue(User.class);
                        if (users != null) {
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
        return comments.size();
    }

    public class ItemVH extends RecyclerView.ViewHolder {
        private TextView tvDate;
        private CircleImageView ivProfile;
        private TextView tvProfile;
        private TextView tvContent;
        public ItemVH(@NonNull View itemView) {
            super(itemView);

            this.ivProfile = (CircleImageView) itemView.findViewById(R.id.ivProfileRecycler);
            this.tvProfile = (TextView) itemView.findViewById(R.id.tvProfileRecycler);
            this.tvContent = (TextView) itemView.findViewById(R.id.tvContentRecycler);
            this.tvDate = (TextView) itemView.findViewById(R.id.tvDateRecycler);

        }
    }
}
