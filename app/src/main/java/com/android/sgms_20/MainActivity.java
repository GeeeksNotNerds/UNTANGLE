package com.android.sgms_20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;
    String currentUserID;
    private DatabaseReference UsersRef;


    private RecyclerView postList;

    private DatabaseReference PostsRef,LikesRef;




    boolean LikeChecker=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth=FirebaseAuth.getInstance();
        currentUserID=mAuth.getCurrentUser().getUid();
        UsersRef= FirebaseDatabase.getInstance().getReference().child("Users");
        PostsRef= FirebaseDatabase.getInstance().getReference().child("Posts");
        LikesRef=FirebaseDatabase.getInstance().getReference().child("Likes");

        postList=(RecyclerView)findViewById(R.id.all_users_post_list);
        postList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);

        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        postList.setLayoutManager(linearLayoutManager);


        BottomNavigationView bottomNav=findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListner);

        DisplayAllUsersPost();




    }

    private void DisplayAllUsersPost()
    {
        //Retrieve using firebase recycler adapter
        //module class-retrieve post info from firebase database
        //static class

        FirebaseRecyclerOptions<Posts> options=
                new FirebaseRecyclerOptions.Builder<Posts>()
                        .setQuery(PostsRef,Posts.class)
                        .setLifecycleOwner(this)
                        .build();

        FirebaseRecyclerAdapter<Posts,PostsViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Posts, PostsViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull PostsViewHolder postsViewHolder, int i, @NonNull Posts posts)
            {
                final String PostKey=getRef(i).getKey();

                postsViewHolder.setFullname(posts.getFullname());
                postsViewHolder.setTime(posts.getTime());
                postsViewHolder.setDate(posts.getDate());
                postsViewHolder.setDescription(posts.getDescription());
                postsViewHolder.setProfileimage(getApplicationContext(),posts.getProfileimage());
                postsViewHolder.setLikesButtonStatus(PostKey);
                postsViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        Intent clickPosIntent=new Intent(MainActivity.this,ClickPostActivity.class);
                        clickPosIntent.putExtra("PostKey",PostKey);
                        startActivity(clickPosIntent);

                    }
                });

                postsViewHolder.CommentPostButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        Intent commentsIntent=new Intent(MainActivity.this,CommentsActivity.class);
                        commentsIntent.putExtra("PostKey",PostKey);
                        startActivity(commentsIntent);


                    }
                });

                postsViewHolder.LikePostButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        LikeChecker=true;
                        LikesRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(LikeChecker==true)
                                {
                                    if(dataSnapshot.child(PostKey).hasChild(currentUserID))
                                    {
                                        LikesRef.child(PostKey).child(currentUserID).removeValue();
                                        LikeChecker=false;

                                    }
                                    else
                                    {

                                        LikesRef.child(PostKey).child(currentUserID).setValue(true);
                                        LikeChecker=false;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }
                });
            }

            @NonNull
            @Override
            public PostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new PostsViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.all_posts_layout, parent, false));

            }




        };
        postList.setAdapter(firebaseRecyclerAdapter);
    }

        /*FirebaseRecyclerAdapter<Posts,PostsViewHolder> firebaseRecyclerAdapter=
                new FirebaseRecyclerAdapter<Posts, PostsViewHolder>(
                        Posts.class,
                        R.layout.all_posts_layout,
                        PostsViewHolder.class,
                        PostsRef)
                {
                    @Override
                    protected void onBindViewHolder(@NonNull PostsViewHolder postsViewHolder, int i, @NonNull Posts posts) {

                    }

                    @NonNull
                    @Override
                    public PostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        return null;
                    }
                };*/

    public static class PostsViewHolder extends RecyclerView.ViewHolder
    {
        View mView;
        ImageButton LikePostButton,CommentPostButton;
        TextView DisplayNoOfLikes;
        int CountLikes;
        String currentUserId;
        DatabaseReference LikesRef;


        public PostsViewHolder(@NonNull View itemView)
        {
            super(itemView);
            mView=itemView;
            LikePostButton=(ImageButton)mView.findViewById(R.id.like_button);
            CommentPostButton=(ImageButton)mView.findViewById(R.id.comment_button);
            DisplayNoOfLikes=(TextView)mView.findViewById(R.id.display_no_of_likes);

            LikesRef=FirebaseDatabase.getInstance().getReference().child("Likes");
            currentUserId=FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        public void setLikesButtonStatus(final String PostKey)

        {
            LikesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(PostKey).hasChild(currentUserId))
                    {
                        CountLikes=(int)dataSnapshot.child(PostKey).getChildrenCount();
                        LikePostButton.setImageResource(R.drawable.profile);
                        DisplayNoOfLikes.setText(Integer.toString(CountLikes)+(" Likes"));
                    }
                    else
                    {
                        CountLikes=(int)dataSnapshot.child(PostKey).getChildrenCount();
                        LikePostButton.setImageResource(R.drawable.profile);
                        DisplayNoOfLikes.setText(Integer.toString(CountLikes));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        public void setFullname(String fullname)
        {
            TextView username=(TextView)mView.findViewById(R.id.post_user_name);
            username.setText(fullname);
        }
        public void setProfileimage(Context ctx, String profileimage)
        {
            ImageView image=(ImageView)mView.findViewById(R.id.post_profile_image);
            Picasso.with(ctx).load(profileimage).into(image);
        }
        public void setTime(String time)
        {
            TextView PostTime=(TextView)mView.findViewById(R.id.post_time);
            PostTime.setText(time);
        }
        public void setDate(String date)
        {
            TextView PostDate=(TextView)mView.findViewById(R.id.post_date);
            PostDate.setText(date);
        }
        public void setDescription(String description)
        {
            TextView PostDescription=(TextView)mView.findViewById(R.id.post_description);
            PostDescription.setText(description);
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListner=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()){
                        case R.id.nav_home:
                            startActivity(new Intent(MainActivity.this,MainActivity.class));
                            finish();
                            break;
                        case R.id.nav_post:
                            startActivity(new Intent(MainActivity.this,PostActivity.class));
                            finish();
                            break;
                        case R.id.nav_profile:
                            startActivity(new Intent(MainActivity.this,ProfileActivity.class));
                            finish();
                            break;
                    }

                    return true;
                }
            };

    @Override
    protected void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();

        super.onStart();
        FirebaseUser currentUser=mAuth.getCurrentUser();
        if(currentUser==null)
        {
            sendUserToLoginActivity();
        }
        else
        {
            CheckUserExistence();
        }
    }

    private void CheckUserExistence()
    {
        final String current_user_id=mAuth.getCurrentUser().getUid();//online user
        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(current_user_id))
                {
                    SendToSetupActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void SendToSetupActivity()
    {
        Intent intent=new Intent(MainActivity.this,SetupActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void sendUserToLoginActivity() {
        Intent loginIntent=new Intent(MainActivity.this,LoginActivity.class);

        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }
}
