package com.android.sgms_20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.shreyaspatil.MaterialDialog.MaterialDialog;
import com.squareup.picasso.Picasso;
import com.yalantis.filter.adapter.FilterAdapter;
import com.yalantis.filter.animator.FiltersListItemAnimator;
import com.yalantis.filter.listener.FilterListener;
import com.yalantis.filter.widget.Filter;
import com.yalantis.filter.widget.FilterItem;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class MainActivity extends AppCompatActivity implements FilterListener<Tag>
{
    private FirebaseAuth mAuth;

    private GoogleApiClient mGoogleApiClient;
    private String currentUserID;
    private String type;
    private int[] mColors;
    private String[] mAdmin= new String[]  {"AkX6MclvgrXpN8oOGI5v37dn7eb2"};
    private String[] mClub;
    private int colour1,colour2,colour3,colour4;
    private DatabaseReference UsersRef;
    private Button mSort1,mSort2;
    private ImageView mStar;
    private Toolbar mToolbar;
    private TextDrawable mDrawableBuilder;
    private String[] mTitles;
    private Button AdminTab,ClubTab,PublicTab;
    private List<Posts> mAllQuestions;
    private Filter<Tag> mFilter;
    private String adminCat;
    private Button Send;
    private LinearLayoutManager linearLayoutManager;
    private PostsAdapter mAdapter;
    AppCompatImageView LikePostButton,downVotePostButton;
    TextView DisplayNoOfLikes,DisplayDownVotes;
    int CountLikes,countDownVotes;
    int pos;
    boolean isAdmin;

    private ImageView pro;
    private int q,tab;
    private static MainActivity instance;


    private RecyclerView postList,mRecyclerView;
    private DatabaseReference MyPostRef,PostsRef,LikesRef,DownVotesRef;
    String letter="A";


    boolean LikeChecker=false,DownVoteChecker=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        q=0;
        AdminTab=(Button)findViewById(R.id.admin);
        ClubTab=(Button)findViewById(R.id.club);
        PublicTab=(Button)findViewById(R.id.chat);
        //Window window = MainActivity.getWindow();

        //public DataSnapshot dataSnapshot=new public DataSnapshot()
        mToolbar=(Toolbar) findViewById(R.id.toolbar);
        FirebaseMessaging.getInstance().subscribeToTopic("pushNotifications");//subscribing
        FirebaseMessaging.getInstance().unsubscribeFromTopic("pushNotifications");//unsubscribe
        pro=(ImageView)findViewById(R.id.thumbnail);
        setSupportActionBar(mToolbar);
        instance=this;
        setTitle("Home");



        if(!haveNetworkConnection())
        {
            Toast.makeText(MainActivity.this,"You are not Online....Please switch on your internet connection!",Toast.LENGTH_LONG).show();
        }
        tab=3;
        mAuth=FirebaseAuth.getInstance();
        currentUserID=mAuth.getCurrentUser().getUid();

       /* if(currentUserID.equals("AkX6MclvgrXpN8oOGI5v37dn7eb2")){
            isAdmin=true;
        }else{
            isAdmin=false;
        }*/


        UsersRef= FirebaseDatabase.getInstance().getReference().child("Users");
        PostsRef= FirebaseDatabase.getInstance().getReference().child("Posts");
        LikesRef=FirebaseDatabase.getInstance().getReference().child("Likes");
        DownVotesRef=FirebaseDatabase.getInstance().getReference().child("DownVotes");
        MyPostRef=FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);
        //Toast.makeText(instance, currentUserID, Toast.LENGTH_SHORT).show();
        UsersRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                type=dataSnapshot.child("type").getValue().toString();



       // if(currentUserID.equals("AkX6MclvgrXpN8oOGI5v37dn7eb2")||currentUserID.equals("HwRTgHAQF4UyfkoP8r0zN3MmO4y2"))
        if(type.equals("Admin")||type.equals("SubAdmin"))
                {
           MyPostRef.addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(DataSnapshot dataSnapshot) {
                   if (dataSnapshot.exists()) {
                       if(dataSnapshot.hasChild("subCategory"))
                       adminCat = dataSnapshot.child("subCategory").getValue().toString();
                       //  Toast.makeText(MainActivity.this, adminCat, Toast.LENGTH_SHORT).show();
                   }
               }

               @Override
               public void onCancelled(DatabaseError databaseError) {

               }
           });
        }

        if(currentUserID.equals("FU5r1KMEvOeQqCU5D8V7FQ4MGQW2"))
        {
            pro.setVisibility(View.INVISIBLE);
            //Toast.makeText(this, "Working", Toast.LENGTH_SHORT).show();
        }
        else
            {
            MyPostRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        if (dataSnapshot.hasChild("username"))
                        {
                            String myProfileName = dataSnapshot.child("username").getValue().toString();
                            char letter = myProfileName.charAt(0);
                            letter = Character.toUpperCase(letter);


                            mDrawableBuilder = TextDrawable.builder().buildRound(String.valueOf(letter), R.color.colorAccent);

                            pro.setImageDrawable(mDrawableBuilder);

                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            pro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    startActivity(new Intent(MainActivity.this, SideMenu.class));

                }
            });
        }


        ImagePipelineConfig config = ImagePipelineConfig
                .newBuilder(MainActivity.this)
                .setDownsampleEnabled(true)
                .build();
        Fresco.initialize(MainActivity.this, config);

        mColors = getResources().getIntArray(R.array.colors);
        mAdmin=getResources().getStringArray(R.array.admin_uid);
        mTitles = getResources().getStringArray(R.array.job_titles);
        mClub=getResources().getStringArray(R.array.club_uid);

        mFilter = (Filter<Tag>) findViewById(R.id.filter);
        mFilter.setAdapter(new Adapter(getTags()));
        mFilter.setListener(MainActivity.this);

        //the text to show when there's no selected items
        mFilter.setNoSelectedItemText(getString(R.string.str_all_selected));
        mFilter.build();

       // if(currentUserID.equals("AkX6MclvgrXpN8oOGI5v37dn7eb2"))
        if(type.equals("Admin")||type.equals("SubAdmin"))
                {
            PublicTab.setText("Private");
        }
        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        linearLayoutManager =new LinearLayoutManager(MainActivity.this,RecyclerView.VERTICAL,true);
        linearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        pos=linearLayoutManager.findLastVisibleItemPosition();
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemViewCacheSize(20);
        mRecyclerView.setDrawingCacheEnabled(true);
        mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mAdapter=new PostsAdapter(MainActivity.this,mAllQuestions=getQuestions());
        mRecyclerView.setAdapter(mAdapter);
        mSort1=(Button)findViewById(R.id.latest);

        mSort1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mSort1.setBackgroundResource(R.drawable.button_clicked);
                mSort2.setBackgroundResource(R.drawable.button_unclick);
                mSort1.setTextColor(mColors[27]);
                mSort2.setTextColor(mColors[22]);
                q=0;

                linearLayoutManager.setStackFromEnd(true);
                mRecyclerView.setHasFixedSize(true);
                mAdapter=new PostsAdapter(MainActivity.this,mAllQuestions=getQuestions());
                mRecyclerView.setAdapter(mAdapter);
            }
        });
        mSort2=(Button)findViewById(R.id.likes_sort);
        mSort2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mSort2.setBackgroundResource(R.drawable.button_clicked);
                mSort1.setBackgroundResource(R.drawable.button_unclick);
                mSort2.setTextColor(mColors[27]);
                mSort1.setTextColor(mColors[22]);
                q=1;
                linearLayoutManager.setStackFromEnd(true);
                mRecyclerView.setHasFixedSize(true);
                mAdapter=new PostsAdapter(MainActivity.this,mAllQuestions=getQuestions());
                mRecyclerView.setAdapter(mAdapter);
            }
        });


        AdminTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                if(isAdmin){
                   // add.setVisibility(View.VISIBLE);
                }else{
                   // add.setVisibility(View.GONE);
                }
                mSort1.setBackgroundResource(R.drawable.button_clicked);
                mSort2.setBackgroundResource(R.drawable.button_unclick);
                mSort1.setTextColor(mColors[27]);
                mSort2.setTextColor(mColors[22]);
                q=0;
                tab=1;
                AdminTab.setBackgroundResource(R.drawable.tabtype);
                ClubTab.setBackgroundResource(R.drawable.tabtypeno);
                PublicTab.setBackgroundResource(R.drawable.tabtypeno);
                linearLayoutManager.setStackFromEnd(true);
                mRecyclerView.setHasFixedSize(true);
                mAdapter=new PostsAdapter(MainActivity.this,mAllQuestions=getQuestions());
                mRecyclerView.setAdapter(mAdapter);
            }
        });
        ClubTab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if(isAdmin){
                   // add.setVisibility(View.VISIBLE);
                }else{
                    //add.setVisibility(View.GONE);
                }
                mSort1.setBackgroundResource(R.drawable.button_clicked);
                mSort2.setBackgroundResource(R.drawable.button_unclick);
                mSort1.setTextColor(mColors[27]);
                mSort2.setTextColor(mColors[22]);
                q=0;
                tab=2;
                AdminTab.setBackgroundResource(R.drawable.tabtypeno);
                ClubTab.setBackgroundResource(R.drawable.tabtype);
                PublicTab.setBackgroundResource(R.drawable.tabtypeno);
                linearLayoutManager.setStackFromEnd(true);
                mRecyclerView.setHasFixedSize(true);
                mAdapter=new PostsAdapter(MainActivity.this,mAllQuestions=getQuestions());
                mRecyclerView.setAdapter(mAdapter);
            }
        });
        PublicTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                if(!isAdmin){
                   // add.setVisibility(View.VISIBLE);
                }else{
                    //add.setVisibility(View.GONE);
                }
                mSort1.setBackgroundResource(R.drawable.button_clicked);
                mSort2.setBackgroundResource(R.drawable.button_unclick);
                mSort1.setTextColor(mColors[27]);
                mSort2.setTextColor(mColors[22]);
                q=0;
                tab=3;
                AdminTab.setBackgroundResource(R.drawable.tabtypeno);
                ClubTab.setBackgroundResource(R.drawable.tabtypeno);
                PublicTab.setBackgroundResource(R.drawable.tabtype);
                linearLayoutManager.setStackFromEnd(true);
                mRecyclerView.setHasFixedSize(true);
                mAdapter=new PostsAdapter(MainActivity.this,mAllQuestions=getQuestions());
                mRecyclerView.setAdapter(mAdapter);
            }
        });

        if(currentUserID.equals("FU5r1KMEvOeQqCU5D8V7FQ4MGQW2")){

           /* add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent loginIntent=new Intent(MainActivity.this,SnackBarActivity.class);
                    startActivity(loginIntent);
                }
            });
*/


        }else {


           /* add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent postIntent = new Intent(MainActivity.this, PostActivity.class);
                    postIntent.putExtra("UserID", currentUserID);
                    startActivity(postIntent);
                }
            });*/
        }
        //mRecyclerView.setAdapter(mAdapter = new PostsAdapter(this, mAllQuestions = getQuestions()));

        mRecyclerView.setItemAnimator(new FiltersListItemAnimator());
        BottomNavigationView bottomNav=findViewById(R.id.bottom_navigation);
        BottomNavigationView bottomNavAdmin=findViewById(R.id.bottom_navigation_admin);

        if(type.equals("Admin"))
        {
            bottomNav.setVisibility(View.GONE);
            bottomNavAdmin.setVisibility(View.VISIBLE);
           // bottomNavAdmin.getMenu().findItem(R.id.nav_home_admin).setChecked(true);
            bottomNavAdmin.setOnNavigationItemSelectedListener(navListner2);
            bottomNavAdmin.getMenu().findItem(R.id.nav_home_admin).setChecked(true);
        }
        else
        {
            bottomNav.setVisibility(View.VISIBLE);
            bottomNavAdmin.setVisibility(View.GONE);
            if(currentUserID.equals("FU5r1KMEvOeQqCU5D8V7FQ4MGQW2"))
        {
            bottomNav.setOnNavigationItemSelectedListener(navListner1);
            bottomNav.getMenu().findItem(R.id.nav_home).setChecked(true);
        }
        else
         {
            bottomNav.setOnNavigationItemSelectedListener(navListner);
            bottomNav.getMenu().findItem(R.id.nav_home).setChecked(true);
         }
        }
        int d = 0;
        for (int i = 0; i < 1; i++) {
            if (currentUserID.equals(mAdmin[i])) {
                d = 1;
                break;
            }
        }

       // if(d==1)
                if(type.equals("Admin")||type.equals("SubAdmin"))
        {
            Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    .getBoolean("isFirstRun", true);

            if (isFirstRun)
            {
                //Toast.makeText(instance, "Working", Toast.LENGTH_SHORT).show();
                //show start activity
                startActivity(new Intent(MainActivity.this,EntryActivity.class));


                // Show Dialog
               // mDialog.show();
            }


            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                    .putBoolean("isFirstRun", false).commit();}

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static MainActivity getInstance() {
        return instance;
    }


    /*private void SendUserToStarActivity()
    {
        Intent intent=new Intent(MainActivity.this, StarActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }*/


    private void calculateDiff(final List<Posts> oldList, final List<Posts> newList) {
        DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return oldList.size();
            }

            @Override
            public int getNewListSize() {
                return newList.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
            }
        }).dispatchUpdatesTo(mAdapter);
    }

    private List<Tag> getTags() {
        List<Tag> tags = new ArrayList<>();

        for (int i = 0; i < mTitles.length; ++i) {
            tags.add(new Tag(mTitles[i], mColors[i]));
        }

        return tags;
    }

    @Override
    public void onNothingSelected() {
        if (mRecyclerView != null) {
            mAdapter.setPosts(mAllQuestions);
            mAdapter.notifyDataSetChanged();
        }
    }
    public void getData(DataSnapshot dataSnapshot)
    {
        //dataSnapshot=new DataSnapshot(UsersRef,"");
    }
    public void sort()
    {
        linearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setHasFixedSize(true);
        mAdapter=new PostsAdapter(MainActivity.this,mAllQuestions=getQuestions());
        mRecyclerView.setAdapter(mAdapter);
    }

    public List<Posts> getQuestions() {
        return new ArrayList<Posts>() {
            {
                UsersRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        type=dataSnapshot.child("type").getValue().toString();

                if(tab==1)//Official Tab
                {
                    if(q==1)//sort by likes
                    {
                        PostsRef.orderByChild("likes").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot)
                            {
                                mAllQuestions.clear();

                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    String postKey = dataSnapshot1.child("PostKey").getValue().toString();

                                    final String owner;

                                    String uid = dataSnapshot1.child("uid").getValue().toString();
                                    String postType=dataSnapshot1.child("postType").getValue().toString();
                                    int c = 0;
                                    for (int i = 0; i < 1; i++) {
                                        if (uid.equals(mAdmin[i])) {
                                            c = 1;
                                            break;
                                        }
                                    }
                                    if (c != 1) {
                                        for (int j = 0; j < 1; j++) {
                                            if (uid.equals(mClub[j])) {
                                                c = 2;
                                                break;
                                            }
                                        }
                                    }

                                    if (postType.equals("Admin")||postType.equals("SubAdmin")) {
                                        owner = "Admin";
                                        c = 0;
                                    } else if (postType.equals("Club")) {
                                        owner = "Club";
                                        c = 0;
                                    } else if (currentUserID.equals(uid)) {
                                        owner = "MyPosts";
                                        c = 0;
                                    } else {
                                        owner = "General";
                                        c = 0;
                                    }


                                    String show = dataSnapshot1.child("showInformation").getValue().toString();
                                    String info, mail;
                                    //if(show.equals("no"))info="Anonymous";


                                    final String mode = dataSnapshot1.child("mode").getValue().toString();
                                    final String sub = dataSnapshot1.child("subCategory").getValue().toString();
                                    final String categ = dataSnapshot1.child("category").getValue().toString();
                                    String name = dataSnapshot1.child("username").getValue().toString();
                                    String status;
                                    if(!owner.equals("Admin"))status=dataSnapshot1.child("status").getValue().toString();
                                    else status="-";
                                    String user = dataSnapshot1.child("email").getValue().toString();
                                    String postpic=dataSnapshot1.child("PostImage").getValue().toString();

                                    String date = dataSnapshot1.child("date").getValue().toString();
                                    String post = dataSnapshot1.child("description").getValue().toString();
                                    String pdf = dataSnapshot1.child("PostPDF").getValue().toString();
                                    // String profilePic = dataSnapshot1.child("profileImage").getValue().toString();
                                    if(type.equals("Admin")||type.equals("SubAdmin"))
                                    {
                                        info=name;
                                        mail=user;
                                    }
                                    else {
                                        if (show.equals("no")) {
                                            info = "Anonymous";
                                            mail = " ";
                                        } else {
                                            info = name;
                                            mail = user;
                                        }}

                                    if (categ.equals("Official")) colour1 = mColors[7];
                                    if (categ.equals("Personal")) colour1 = mColors[8];
                                    if (categ.equals("Miscellaneous")) colour1 = mColors[19];
                                    if (sub.equals("Admission")) colour2 = mColors[9];
                                    if (sub.equals("Academic")) colour2 = mColors[10];
                                    if (sub.equals("Finance")) colour2 = mColors[11];
                                    if (sub.equals("Housing")) colour2 = mColors[16];
                                    if (sub.equals("Rights Violation")) colour2 = mColors[18];
                                    if (sub.equals("Health")) colour2 = mColors[17];
                                    if (mode.equals("Public")) colour3 = mColors[6];
                                    if (sub.equals("Internships")) colour2 = mColors[13];
                                    if (sub.equals("Competitions")) colour2 = mColors[14];
                                    if (sub.equals("Courses")) colour2 = mColors[15];
                                    if (mode.equals("Private")) colour3 = mColors[5];
                                    if (owner.equals("Admin")) colour4 = mColors[1];
                                    if (owner.equals("General")) colour4 = mColors[4];
                                    if (owner.equals("MyPosts")) colour4 = mColors[3];
                                    if (owner.equals("Club")) colour4 = mColors[2];
                                    if (sub.equals("Placements")) colour2 = mColors[12];
                                    if(categ.equals("Activities"))colour1=mColors[23];
                                    if(sub.equals("Workshops"))colour2=mColors[20];
                                    if(sub.equals("Results"))colour2=mColors[21];
                                    if(sub.equals("Events"))colour2=mColors[22];

                                    String like=dataSnapshot1.child("likes").getValue().toString();
                                    if(postType.equals("Admin")||postType.equals("SubAdmin")||(uid.equals(currentUserID)&& mode.equals("Private")))
                                    {
                                        add(new Posts(like,postKey, ""+info,   mail, post, date, date, uid, mode,postpic,pdf, categ, sub, show,status, new ArrayList<Tag>() {{
                                            add(new Tag(owner, colour4));
                                            add(new Tag(mode, colour3));
                                            add(new Tag(categ, colour1));
                                            add(new Tag(sub, colour2));
                                        }}));
                                    }
                                }
                                mAdapter=new PostsAdapter(MainActivity.this,mAllQuestions);
                                mRecyclerView.setAdapter(mAdapter);
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError)
                            {

                            }
                        });

                    }
                    else
                    {
                        PostsRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot)
                            {
                                mAllQuestions.clear();

                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    String postKey = dataSnapshot1.child("PostKey").getValue().toString();

                                    final String owner;
                                    String uid = dataSnapshot1.child("uid").getValue().toString();
                                    String postType=dataSnapshot1.child("postType").getValue().toString();
                                    int c = 0;
                                    for (int i = 0; i < 1; i++) {
                                        if (uid.equals(mAdmin[i])) {
                                            c = 1;
                                            break;
                                        }
                                    }
                                    if (c != 1) {
                                        for (int j = 0; j < 1; j++) {
                                            if (uid.equals(mClub[j])) {
                                                c = 2;
                                                break;
                                            }
                                        }
                                    }

                                    if (postType.equals("Admin")||postType.equals("SubAdmin")) {
                                        owner = "Admin";
                                        c = 0;
                                    } else if (postType.equals("Club")) {
                                        owner = "Club";
                                        c = 0;
                                    } else if (currentUserID.equals(uid)) {
                                        owner = "MyPosts";
                                        c = 0;
                                    } else {
                                        owner = "General";
                                        c = 0;
                                    }


                                    String show = dataSnapshot1.child("showInformation").getValue().toString();
                                    String info, mail;
                                    //if(show.equals("no"))info="Anonymous";


                                    final String mode = dataSnapshot1.child("mode").getValue().toString();
                                    final String sub = dataSnapshot1.child("subCategory").getValue().toString();
                                    final String categ = dataSnapshot1.child("category").getValue().toString();
                                    String name = dataSnapshot1.child("username").getValue().toString();
                                    String status;
                                    if(!owner.equals("Admin"))status=dataSnapshot1.child("status").getValue().toString();
                                    else status="-";
                                   // String postType=dataSnapshot1.child("postType").getValue().toString();
                                    String user = dataSnapshot1.child("email").getValue().toString();
                                    String postpic=dataSnapshot1.child("PostImage").getValue().toString();
                                    String date = dataSnapshot1.child("date").getValue().toString();
                                    String post = dataSnapshot1.child("description").getValue().toString();
                                    // String profilePic = dataSnapshot1.child("profileImage").getValue().toString();
                                    if(type.equals("Admin")||type.equals("SubAdmin"))
                                    {
                                        info=name;
                                        mail=user;
                                    }
                                    else {
                                        if (show.equals("no")) {
                                            info = "Anonymous";
                                            mail = " ";
                                        } else {
                                            info = name;
                                            mail = user;
                                        }}

                                    if (categ.equals("Official")) colour1 = mColors[7];
                                    if (categ.equals("Personal")) colour1 = mColors[8];
                                    if (categ.equals("Miscellaneous")) colour1 = mColors[19];
                                    if (sub.equals("Admission")) colour2 = mColors[9];
                                    if (sub.equals("Academic")) colour2 = mColors[10];
                                    if (sub.equals("Finance")) colour2 = mColors[11];
                                    if (sub.equals("Housing")) colour2 = mColors[16];
                                    if (sub.equals("Rights Violation")) colour2 = mColors[18];
                                    if (sub.equals("Health")) colour2 = mColors[17];
                                    if (mode.equals("Public")) colour3 = mColors[6];
                                    if (sub.equals("Internships")) colour2 = mColors[13];
                                    if (sub.equals("Competitions")) colour2 = mColors[14];
                                    if (sub.equals("Courses")) colour2 = mColors[15];
                                    if (mode.equals("Private")) colour3 = mColors[5];
                                    if (owner.equals("Admin")) colour4 = mColors[1];
                                    if (owner.equals("General")) colour4 = mColors[4];
                                    if (owner.equals("MyPosts")) colour4 = mColors[3];
                                    if (owner.equals("Club")) colour4 = mColors[2];
                                    if (sub.equals("Placements")) colour2 = mColors[12];
                                    if(categ.equals("Activities"))colour1=mColors[23];
                                    if(sub.equals("Workshops"))colour2=mColors[20];
                                    if(sub.equals("Results"))colour2=mColors[21];
                                    if(sub.equals("Events"))colour2=mColors[22];
                                    String like=dataSnapshot1.child("likes").getValue().toString();

                                    String pdf =dataSnapshot1.child("PostPDF").getValue().toString();
                                   // if(uid.equals("AkX6MclvgrXpN8oOGI5v37dn7eb2"))

                                    //if(uid.equals("AkX6MclvgrXpN8oOGI5v37dn7eb2"))
                                    if(postType.equals("Admin")||postType.equals("SubAdmin")||(uid.equals(currentUserID)&& mode.equals("Private")))

                                    {
                                        add(new Posts(like,postKey, ""+info,   mail, post, date, date, uid, mode,postpic,pdf, categ, sub, show,status, new ArrayList<Tag>() {{
                                            add(new Tag(owner, colour4));
                                            add(new Tag(mode, colour3));
                                            add(new Tag(categ, colour1));
                                            add(new Tag(sub, colour2));
                                        }}));
                                    }
                                }
                                mAdapter=new PostsAdapter(MainActivity.this,mAllQuestions);
                                mRecyclerView.setAdapter(mAdapter);
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError)
                            {

                            }
                        });
                    }
                }
                else if(tab==2)//clubs tab
                {
                    if(q==1)
                    {
                        PostsRef.orderByChild("likes").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot)
                            {
                                mAllQuestions.clear();

                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    String postKey = dataSnapshot1.child("PostKey").getValue().toString();

                                    final String owner;
                                    String uid = dataSnapshot1.child("uid").getValue().toString();
                                    String postType=dataSnapshot1.child("postType").getValue().toString();


                                    if (postType.equals("Admin")||postType.equals("SubAdmin")) {
                                        owner = "Admin";
                                    //    c = 0;
                                    } else if (postType.equals("Club")) {
                                        owner = "Club";
                                      //  c = 0;
                                    } else if (currentUserID.equals(uid)) {
                                        owner = "MyPosts";
                                        //c = 0;
                                    } else {
                                        owner = "General";
                                        //c = 0;
                                    }


                                    String show = dataSnapshot1.child("showInformation").getValue().toString();
                                    String info, mail;
                                    //if(show.equals("no"))info="Anonymous";


                                    final String mode = dataSnapshot1.child("mode").getValue().toString();
                                    final String sub = dataSnapshot1.child("subCategory").getValue().toString();
                                    final String categ = dataSnapshot1.child("category").getValue().toString();
                                    String name = dataSnapshot1.child("username").getValue().toString();
                                    String status;
                                    if(!owner.equals("Admin"))status=dataSnapshot1.child("status").getValue().toString();
                                    else status="-";
                                    String user = dataSnapshot1.child("email").getValue().toString();
                                    String postpic=dataSnapshot1.child("PostImage").getValue().toString();
                                    String date = dataSnapshot1.child("date").getValue().toString();
                                    String post = dataSnapshot1.child("description").getValue().toString();
                                    // String profilePic = dataSnapshot1.child("profileImage").getValue().toString();
                                    //if(currentUserID.equals("AkX6MclvgrXpN8oOGI5v37dn7eb2"))
                                    if(type.equals("Admin")||type.equals("SubAdmin"))
                                    {
                                        info=name;
                                        mail=user;
                                    }
                                    else {
                                        if (show.equals("no")) {
                                            info = "Anonymous";
                                            mail = " ";
                                        } else {
                                            info = name;
                                            mail = user;
                                        }}

                                    if (categ.equals("Official")) colour1 = mColors[7];
                                    if (categ.equals("Personal")) colour1 = mColors[8];
                                    if (categ.equals("Miscellaneous")) colour1 = mColors[19];
                                    if (sub.equals("Admission")) colour2 = mColors[9];
                                    if (sub.equals("Academic")) colour2 = mColors[10];
                                    if (sub.equals("Finance")) colour2 = mColors[11];
                                    if (sub.equals("Housing")) colour2 = mColors[16];
                                    if (sub.equals("Rights Violation")) colour2 = mColors[18];
                                    if (sub.equals("Health")) colour2 = mColors[17];
                                    if (mode.equals("Public")) colour3 = mColors[6];
                                    if (sub.equals("Internships")) colour2 = mColors[13];
                                    if (sub.equals("Competitions")) colour2 = mColors[14];
                                    if (sub.equals("Courses")) colour2 = mColors[15];
                                    if (mode.equals("Private")) colour3 = mColors[5];
                                    if (owner.equals("Admin")) colour4 = mColors[1];
                                    if (owner.equals("General")) colour4 = mColors[4];
                                    if (owner.equals("MyPosts")) colour4 = mColors[3];
                                    if (owner.equals("Club")) colour4 = mColors[2];
                                    if (sub.equals("Placements")) colour2 = mColors[12];
                                    if(categ.equals("Activities"))colour1=mColors[23];
                                    if(sub.equals("Workshops"))colour2=mColors[20];
                                    if(sub.equals("Results"))colour2=mColors[21];
                                    if(sub.equals("Events"))colour2=mColors[22];
                                    String like=dataSnapshot1.child("likes").getValue().toString();

                                    String pdf=dataSnapshot1.child("PostPDF").getValue().toString();
                                   // if(uid.equals("nO3l336v84OXDNCkR0aFNm0Es1w2"))


                                    if(postType.equals("Club"))//posts by the clubs are only shown

                                    {
                                        add(new Posts(like,postKey, ""+info,   mail, post, date, date, uid, mode,postpic,pdf, categ, sub, show,status, new ArrayList<Tag>() {{
                                            add(new Tag(owner, colour4));
                                            add(new Tag(mode, colour3));
                                            add(new Tag(categ, colour1));
                                            add(new Tag(sub, colour2));
                                        }}));
                                    }
                                }
                                mAdapter=new PostsAdapter(MainActivity.this,mAllQuestions);
                                mRecyclerView.setAdapter(mAdapter);
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError)
                            {

                            }
                        });
                    }
                    else
                    {
                        PostsRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                mAllQuestions.clear();

                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    String postKey = dataSnapshot1.child("PostKey").getValue().toString();

                                    final String owner;
                                    String uid = dataSnapshot1.child("uid").getValue().toString();
                                    String postType = dataSnapshot1.child("postType").getValue().toString();

                                    if (postType.equals("Admin")||postType.equals("SubAdmin")) {
                                        owner = "Admin";
                                    //    c = 0;
                                    } else if (postType.equals("Club")) {
                                        owner = "Club";
                                      //  c = 0;
                                    } else if (currentUserID.equals(uid)) {
                                        owner = "MyPosts";
                                        //c = 0;
                                    } else {
                                        owner = "General";
                                        //c = 0;
                                    }


                                    String show = dataSnapshot1.child("showInformation").getValue().toString();
                                    String info, mail;
                                    //if(show.equals("no"))info="Anonymous";


                                    final String mode = dataSnapshot1.child("mode").getValue().toString();
                                    final String sub = dataSnapshot1.child("subCategory").getValue().toString();
                                    final String categ = dataSnapshot1.child("category").getValue().toString();
                                    String name = dataSnapshot1.child("username").getValue().toString();
                                    String status;
                                    if (!owner.equals("Admin"))
                                        status = dataSnapshot1.child("status").getValue().toString();
                                    else status = "-";
                                    String user = dataSnapshot1.child("email").getValue().toString();
                                    String postpic = dataSnapshot1.child("PostImage").getValue().toString();
                                    String date = dataSnapshot1.child("date").getValue().toString();
                                    String post = dataSnapshot1.child("description").getValue().toString();
                                    // String profilePic = dataSnapshot1.child("profileImage").getValue().toString();
                                    //if(currentUserID.equals("AkX6MclvgrXpN8oOGI5v37dn7eb2"))
                                    if (type.equals("Admin")||type.equals("SubAdmin")) {
                                        info = name;
                                        mail = user;
                                    } else {
                                        if (show.equals("no")) {
                                            info = "Anonymous";
                                            mail = " ";
                                        } else {
                                            info = name;
                                            mail = user;
                                        }
                                    }

                                    if (categ.equals("Official")) colour1 = mColors[7];
                                    if (categ.equals("Personal")) colour1 = mColors[8];
                                    if (categ.equals("Miscellaneous")) colour1 = mColors[19];
                                    if (sub.equals("Admission")) colour2 = mColors[9];
                                    if (sub.equals("Academic")) colour2 = mColors[10];
                                    if (sub.equals("Finance")) colour2 = mColors[11];
                                    if (sub.equals("Housing")) colour2 = mColors[16];
                                    if (sub.equals("Rights Violation")) colour2 = mColors[18];
                                    if (sub.equals("Health")) colour2 = mColors[17];
                                    if (mode.equals("Public")) colour3 = mColors[6];
                                    if (sub.equals("Internships")) colour2 = mColors[13];
                                    if (sub.equals("Competitions")) colour2 = mColors[14];
                                    if (sub.equals("Courses")) colour2 = mColors[15];
                                    if (mode.equals("Private")) colour3 = mColors[5];
                                    if (owner.equals("Admin")) colour4 = mColors[1];
                                    if (owner.equals("General")) colour4 = mColors[4];
                                    if (owner.equals("MyPosts")) colour4 = mColors[3];
                                    if (owner.equals("Club")) colour4 = mColors[2];
                                    if (sub.equals("Placements")) colour2 = mColors[12];
                                    if(categ.equals("Activities"))colour1=mColors[23];
                                    if(sub.equals("Workshops"))colour2=mColors[20];
                                    if(sub.equals("Results"))colour2=mColors[21];
                                    if(sub.equals("Events"))colour2=mColors[22];
                                    String like = dataSnapshot1.child("likes").getValue().toString();


                                    //String like=dataSnapshot1.child("likes").getValue().toString();
                                    String pdf = dataSnapshot1.child("PostPDF").getValue().toString();
                                   // if(uid.equals("nO3l336v84OXDNCkR0aFNm0Es1w2"))
                                    //{
                                      //  add(new Posts(like,postKey, ""+info,   mail, post, date, date, uid, mode,postpic,pdf, categ, sub, show,status, new ArrayList<Tag>() {{

                                    if (postType.equals("Club"))//posts by the clubs are only shown
                                    {
                                        add(new Posts(like, postKey, "" + info, mail, post, date, date, uid, mode, postpic,pdf, categ, sub, show, status, new ArrayList<Tag>() {{

                                            add(new Tag(owner, colour4));
                                            add(new Tag(mode, colour3));
                                            add(new Tag(categ, colour1));
                                            add(new Tag(sub, colour2));
                                        }}));
                                    }
                                }
                                mAdapter = new PostsAdapter(MainActivity.this, mAllQuestions);
                                mRecyclerView.setAdapter(mAdapter);
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError)
                            {

                            }
                        });
                    }
                }
                else if(tab==3)//public tab
                {//post by all except clubs and official
                    if(q==1)//if private then to admin if cat matches
                        //if public then to all except admin
                    {
                        PostsRef.orderByChild("likes").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot)
                            {
                                mAllQuestions.clear();

                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    String postKey = dataSnapshot1.child("PostKey").getValue().toString();

                                    final String owner;
                                    String uid = dataSnapshot1.child("uid").getValue().toString();
                                    String postType=dataSnapshot1.child("postType").getValue().toString();
                                    int c = 0;
                                    for (int i = 0; i < 1; i++) {
                                        if (uid.equals(mAdmin[i])) {
                                            c = 1;
                                            break;
                                        }
                                    }
                                    if (c != 1) {
                                        for (int j = 0; j < 1; j++) {
                                            if (uid.equals(mClub[j])) {
                                                c = 2;
                                                break;
                                            }
                                        }
                                    }

                                    if (postType.equals("Admin")||postType.equals("SubAdmin")) {
                                        owner = "Admin";
                                        c = 0;
                                    } else if (postType.equals("Club")) {
                                        owner = "Club";
                                        c = 0;
                                    } else if (currentUserID.equals(uid)) {
                                        owner = "MyPosts";
                                        c = 0;
                                    } else {
                                        owner = "General";
                                        c = 0;
                                    }

                                    String show = dataSnapshot1.child("showInformation").getValue().toString();
                                    String info, mail;
                                    //if(show.equals("no"))info="Anonymous";


                                    final String mode = dataSnapshot1.child("mode").getValue().toString();
                                    final String sub = dataSnapshot1.child("subCategory").getValue().toString();
                                    final String categ = dataSnapshot1.child("category").getValue().toString();
                                    String name = dataSnapshot1.child("username").getValue().toString();
                                    String status;
                                    if(!owner.equals("Admin"))status=dataSnapshot1.child("status").getValue().toString();
                                    else status="-";
                                    //String postType=dataSnapshot1.child("postType").getValue().toString();
                                    String user = dataSnapshot1.child("email").getValue().toString();
                                    String postpic=dataSnapshot1.child("PostImage").getValue().toString();
                                    String date = dataSnapshot1.child("date").getValue().toString();
                                    String post = dataSnapshot1.child("description").getValue().toString();
                                    // String profilePic = dataSnapshot1.child("profileImage").getValue().toString();
                                    //if(currentUserID.equals("AkX6MclvgrXpN8oOGI5v37dn7eb2"))
                                    if(type.equals("Admin")||type.equals("SubAdmin"))
                                    {
                                        info=name;
                                        mail=user;
                                    }
                                    else {
                                        if (show.equals("no")) {
                                            info = "Anonymous";
                                            mail = " ";
                                        } else {
                                            info = name;
                                            mail = user;
                                        }}

                                    if (categ.equals("Official")) colour1 = mColors[7];
                                    if (categ.equals("Personal")) colour1 = mColors[8];
                                    if (categ.equals("Miscellaneous")) colour1 = mColors[19];
                                    if (sub.equals("Admission")) colour2 = mColors[9];
                                    if (sub.equals("Academic")) colour2 = mColors[10];
                                    if (sub.equals("Finance")) colour2 = mColors[11];
                                    if (sub.equals("Housing")) colour2 = mColors[16];
                                    if (sub.equals("Rights Violation")) colour2 = mColors[18];
                                    if (sub.equals("Health")) colour2 = mColors[17];
                                    if (mode.equals("Public")) colour3 = mColors[6];
                                    if (sub.equals("Internships")) colour2 = mColors[13];
                                    if (sub.equals("Competitions")) colour2 = mColors[14];
                                    if (sub.equals("Courses")) colour2 = mColors[15];
                                    if (mode.equals("Private")) colour3 = mColors[5];
                                    if (owner.equals("Admin")) colour4 = mColors[1];
                                    if (owner.equals("General")) colour4 = mColors[4];
                                    if (owner.equals("MyPosts")) colour4 = mColors[3];
                                    if (owner.equals("Club")) colour4 = mColors[2];
                                    if (sub.equals("Placements")) colour2 = mColors[12];
                                    if(categ.equals("Activities"))colour1=mColors[23];
                                    if(sub.equals("Workshops"))colour2=mColors[20];
                                    if(sub.equals("Results"))colour2=mColors[21];
                                    if(sub.equals("Events"))colour2=mColors[22];
                                    String like=dataSnapshot1.child("likes").getValue().toString();

                                    String pdf=dataSnapshot1.child("PostPDF").getValue().toString();
                                   // if((!uid.equals("nO3l336v84OXDNCkR0aFNm0Es1w2"))&&(!uid.equals("AkX6MclvgrXpN8oOGI5v37dn7eb2")))
                                    //{
                                      //  add(new Posts(like,postKey, ""+info,   mail, post, date, date, uid, mode,postpic,pdf, categ, sub, show,status, new ArrayList<Tag>() {{



                                   // if((!uid.equals("AkX6MclvgrXpN8oOGI5v37dn7eb2"))&&(!uid.equals("nO3l336v84OXDNCkR0aFNm0Es1w2")))
                                    if((!postType.equals("Admin"))&&(!postType.equals("SubAdmin"))&&(!postType.equals("Club")))
                                    {
                                       // if(mode.equals("Private")&&currentUserID.equals("AkX6MclvgrXpN8oOGI5v37dn7eb2")&&adminCat.equals(sub))
                                        if(mode.equals("Private") && (type.equals("Admin")||type.equals("SubAdmin")) && adminCat.equals(sub))
                                        {
                                            add(new Posts(like,postKey, ""+info,   mail, post, date, date, uid, mode,postpic,pdf, categ, sub, show,status, new ArrayList<Tag>() {{
                                                add(new Tag(owner, colour4));
                                                add(new Tag(mode, colour3));
                                                add(new Tag(categ, colour1));
                                                add(new Tag(sub, colour2));
                                            }}));
                                        }
                                        else if(mode.equals("Public")&&(!type.equals("Admin"))&&(!type.equals("SubAdmin")))
                                        {
                                            add(new Posts(like,postKey, ""+info,   mail, post, date, date, uid, mode,postpic,pdf, categ, sub, show,status, new ArrayList<Tag>() {{
                                                add(new Tag(owner, colour4));
                                                add(new Tag(mode, colour3));
                                                add(new Tag(categ, colour1));
                                                add(new Tag(sub, colour2));
                                            }}));
                                        }
                                    }





                                    /*if((!currentUserID.equals("AkX6MclvgrXpN8oOGI5v37dn7eb2")))//visible only to students and clubs
                                    {
                                        if((!uid.equals("nO3l336v84OXDNCkR0aFNm0Es1w2"))&&(!uid.equals("AkX6MclvgrXpN8oOGI5v37dn7eb2")))
                                    {
                                        if(mode.equals("Public")){
                                        add(new Posts(like,postKey, ""+info,   mail, post, date, date, uid, mode,postpic, categ, sub, show,status, new ArrayList<Tag>() {{

                                            add(new Tag(owner, colour4));
                                            add(new Tag(mode, colour3));
                                            add(new Tag(categ, colour1));
                                            add(new Tag(sub, colour2));
                                        }}));
                                    }}
                                }
                                    if(uid.equals("AkX6MclvgrXpN8oOGI5v37dn7eb2")||uid.equals(currentUserID)) //show admin only private issues
                                    {
                                        if(mode.equals("Private"))
                                        {
                                            if((!uid.equals("nO3l336v84OXDNCkR0aFNm0Es1w2"))&&(!uid.equals("AkX6MclvgrXpN8oOGI5v37dn7eb2")))
                                            {
                                                if(isAdmin) {
                                                    if (adminCat.equals(sub)) {
                                                        add(new Posts(like,postKey, ""+info,   mail, post, date, date, uid, mode,postpic, categ, sub, show,status, new ArrayList<Tag>() {{
                                                            add(new Tag(owner, colour4));
                                                            add(new Tag(mode, colour3));
                                                            add(new Tag(categ, colour1));
                                                            add(new Tag(sub, colour2));
                                                        }}));
                                                    }
                                                }
                                                /*else
                                                {
                                                add(new Posts(like,postKey, ""+info,   mail, post, date, date, uid, mode,postpic, categ, sub, show,status, new ArrayList<Tag>() {{
                                                    add(new Tag(owner, colour4));
                                                    add(new Tag(mode, colour3));
                                                    add(new Tag(categ, colour1));
                                                    add(new Tag(sub, colour2));
                                                }}));
                                                }
                                            }

                                        }
                                    }*/
                                }
                                mAdapter=new PostsAdapter(MainActivity.this,mAllQuestions);
                                mRecyclerView.setAdapter(mAdapter);
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError)
                            {

                            }
                        });
                    }
                    else
                    {
                        PostsRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot)
                            {
                                mAllQuestions.clear();

                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    String postKey = dataSnapshot1.child("PostKey").getValue().toString();

                                    final String owner;
                                    String uid = dataSnapshot1.child("uid").getValue().toString();
                                    String postType=dataSnapshot1.child("postType").getValue().toString();


                                    if (postType.equals("Admin")||postType.equals("SubAdmin")) {
                                        owner = "Admin";
                                    //    c = 0;
                                    } else if (postType.equals("Club")) {
                                        owner = "Club";
                                      //  c = 0;
                                    } else if (currentUserID.equals(uid)) {
                                        owner = "MyPosts";
                                        //c = 0;
                                    } else {
                                        owner = "General";
                                        //c = 0;
                                    }

                                    String show = dataSnapshot1.child("showInformation").getValue().toString();
                                    String info, mail;
                                    //if(show.equals("no"))info="Anonymous";


                                    final String mode = dataSnapshot1.child("mode").getValue().toString();
                                    final String sub = dataSnapshot1.child("subCategory").getValue().toString();
                                    final String categ = dataSnapshot1.child("category").getValue().toString();
                                    String name = dataSnapshot1.child("username").getValue().toString();
                                    String status;
                                    if(!owner.equals("Admin"))status=dataSnapshot1.child("status").getValue().toString();
                                    else status="-";
                                    //String postType=dataSnapshot1.child("postType").getValue().toString();
                                    String user = dataSnapshot1.child("email").getValue().toString();
                                    String postpic=dataSnapshot1.child("PostImage").getValue().toString();
                                    String date = dataSnapshot1.child("date").getValue().toString();
                                    String post = dataSnapshot1.child("description").getValue().toString();
                                    // String profilePic = dataSnapshot1.child("profileImage").getValue().toString();
                                    //if(currentUserID.equals("AkX6MclvgrXpN8oOGI5v37dn7eb2"))
                                    if(type.equals("Admin")||type.equals("SubAdmin"))
                                    {
                                        info=name;
                                        mail=user;
                                    }
                                    else {
                                        if (show.equals("no")) {
                                            info = "Anonymous";
                                            mail = " ";
                                        } else {
                                            info = name;
                                            mail = user;
                                        }}

                                    if (categ.equals("Official")) colour1 = mColors[7];
                                    if (categ.equals("Personal")) colour1 = mColors[8];
                                    if (categ.equals("Miscellaneous")) colour1 = mColors[19];
                                    if (sub.equals("Admission")) colour2 = mColors[9];
                                    if (sub.equals("Academic")) colour2 = mColors[10];
                                    if (sub.equals("Finance")) colour2 = mColors[11];
                                    if (sub.equals("Housing")) colour2 = mColors[16];
                                    if (sub.equals("Rights Violation")) colour2 = mColors[18];
                                    if (sub.equals("Health")) colour2 = mColors[17];
                                    if (mode.equals("Public")) colour3 = mColors[6];
                                    if (sub.equals("Internships")) colour2 = mColors[13];
                                    if (sub.equals("Competitions")) colour2 = mColors[14];
                                    if (sub.equals("Courses")) colour2 = mColors[15];
                                    if (mode.equals("Private")) colour3 = mColors[5];
                                    if (owner.equals("Admin")) colour4 = mColors[1];
                                    if (owner.equals("General")) colour4 = mColors[4];
                                    if (owner.equals("MyPosts")) colour4 = mColors[3];
                                    if (owner.equals("Club")) colour4 = mColors[2];
                                    if (sub.equals("Placements")) colour2 = mColors[12];
                                    if(categ.equals("Activities"))colour1=mColors[23];
                                    if(sub.equals("Workshops"))colour2=mColors[20];
                                    if(sub.equals("Results"))colour2=mColors[21];
                                    if(sub.equals("Events"))colour2=mColors[22];

                                    String pdf = dataSnapshot1.child("PostPDF").getValue().toString();

                                    String like=dataSnapshot1.child("likes").getValue().toString();
                                   



                                   // if((!uid.equals("AkX6MclvgrXpN8oOGI5v37dn7eb2"))&&(!uid.equals("nO3l336v84OXDNCkR0aFNm0Es1w2")))
                                    if((!postType.equals("Admin"))&&(!postType.equals("SubAdmin")) && (!postType.equals("Club")))
                                    {
                                        //if(mode.equals("Private")&&currentUserID.equals("AkX6MclvgrXpN8oOGI5v37dn7eb2")&&adminCat.equals(sub))
                                        if(mode.equals("Private") && (type.equals("Admin")||type.equals("SubAdmin"))&&adminCat.equals(sub))
                                        {
                                            add(new Posts(like,postKey, ""+info,   mail, post, date, date, uid, mode,postpic,pdf, categ, sub, show,status, new ArrayList<Tag>() {{
                                                add(new Tag(owner, colour4));
                                                add(new Tag(mode, colour3));
                                                add(new Tag(categ, colour1));
                                                add(new Tag(sub, colour2));
                                            }}));
                                        }
                                        //else if(mode.equals("Public")&&(!currentUserID.equals("AkX6MclvgrXpN8oOGI5v37dn7eb2")))
                                        else if(mode.equals("Public")&&(!type.equals("Admin"))&&(!type.equals("SubAdmin")))
                                        {
                                            add(new Posts(like,postKey, ""+info,   mail, post, date, date, uid, mode,postpic,pdf, categ, sub, show,status, new ArrayList<Tag>() {{
                                                add(new Tag(owner, colour4));
                                                add(new Tag(mode, colour3));
                                                add(new Tag(categ, colour1));
                                                add(new Tag(sub, colour2));
                                            }}));
                                        }
                                    }
                                }
                                mAdapter=new PostsAdapter(MainActivity.this,mAllQuestions);
                                mRecyclerView.setAdapter(mAdapter);
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError)
                            {

                            }
                        });
                    }
                }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        };
    }

    private List<Posts> findByTags(List<Tag> tags) {
        List<Posts> questions = new ArrayList<>();

        for (Posts question : mAllQuestions) {
            for (Tag tag : tags) {
                if (question.hasTag(tag.getText()) && !questions.contains(question)) {
                    questions.add(question);
                }
            }
        }

        return questions;
    }

    @Override
    public void onFiltersSelected(@NotNull ArrayList<Tag> filters) {
        List<Posts> newQuestions = findByTags(filters);
        List<Posts> oldQuestions = mAdapter.getPosts();
        mAdapter.setPosts(newQuestions);
        calculateDiff(oldQuestions, newQuestions);
        mAdapter.notifyDataSetChanged();

        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onFilterSelected(Tag item) {
        if (item.getText().equals(mTitles[0])) {
            mFilter.deselectAll();
            mFilter.collapse();
        }
    }

    @Override
    public void onFilterDeselected(Tag item) {

    }

    class Adapter extends FilterAdapter<Tag> {

        Adapter(@NotNull List<? extends Tag> items) {
            super(items);
        }

        @NotNull
        @Override
        public FilterItem createView(int position, Tag item) {
            FilterItem filterItem = new FilterItem(MainActivity.this);

            filterItem.setStrokeColor(R.color.colorPrimary);
            filterItem.setTextColor(R.color.colorPrimary);
            //filterItem.setCornerRadius(14);
            filterItem.setCheckedTextColor(ContextCompat.getColor(MainActivity.this, android.R.color.white));
            filterItem.setColor(ContextCompat.getColor(MainActivity.this, android.R.color.white));
            filterItem.setCheckedColor(mColors[position]);
            filterItem.setText(item.getText());
            filterItem.deselect();

            return filterItem;
        }
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListner1=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.nav_post:
                            SendUserToLoginActivity();
                            break;
                        case R.id.nav_profile:
                            SendUserToLoginActivity();
                            break;
                        case  R.id.nav_star:
                            SendUserToLoginActivity();
                            break;
                    }
                    return true;
                }
            };

    private void SendUserToLoginActivity()
    {
        Intent loginIntent=new Intent(MainActivity.this,SnackBarActivity.class);
        startActivity(loginIntent);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListner=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()){

                        case R.id.nav_post:
                            Intent intent=new Intent(MainActivity.this,PostActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                          // finish();
                            break;
                        case R.id.nav_profile:
                            Intent Pintent=new Intent(MainActivity.this,ProfileActivity.class);
                            Pintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(Pintent);
                           //finish();
                            break;
                        case R.id.nav_star:
                            Intent Pintent1=new Intent(MainActivity.this,StarActivity.class);
                            Pintent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(Pintent1);
                          //finish();
                            break;
                    }

                    return true;
                }
            };
    private BottomNavigationView.OnNavigationItemSelectedListener navListner2=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()){

                        case R.id.nav_post_admin:
                            Intent intent=new Intent(MainActivity.this,PostActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            // finish();
                            break;
                        case R.id.nav_profile_admin:
                            Intent Pintent=new Intent(MainActivity.this,ProfileActivity.class);
                            Pintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(Pintent);
                            //finish();
                            break;
                        case R.id.nav_star_admin:
                            Intent Pintent1=new Intent(MainActivity.this,StarActivity.class);
                            Pintent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(Pintent1);
                            //finish();
                            break;
                        case R.id.nav_add_admin:
                            Intent Pintent2=new Intent(MainActivity.this,AddAdmin.class);
                            Pintent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(Pintent2);

                    }

                    return true;
                }
            };

    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseUser currentUser=mAuth.getCurrentUser();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
        if(currentUser==null)
        {
            //sendUserToLoginActivity();
           /* mAuth=FirebaseAuth.getInstance();
            mAuth.signInWithEmailAndPassword("withoutloginuser@gmail.com","LoginFast").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Toast.makeText(MainActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                }
            });*/
        }
        else
        {
            //if(!currentUserID.equals("AkX6MclvgrXpN8oOGI5v37dn7eb2"))
            UsersRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    type=dataSnapshot.child("type").getValue().toString();
                    if(type.equals("Admin")||type.equals("subAdmin"))
                    {
                        if(!dataSnapshot.hasChild("subCategory"))
                        {
                            SendToSetupActivity();
                        }
                    }
                    //if(!currentUserID.equals("AkX6MclvgrXpN8oOGI5v37dn7eb2")||!currentUserID.equals("HwRTgHAQF4UyfkoP8r0zN3MmO4y2"))
                      if(!type.equals("Admin")&&!type.equals("SubAdmin")&&!type.equals("Club"))
                        CheckUserExistence();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

    }

    private void CheckUserExistence()
    {
        final String current_user_id=mAuth.getCurrentUser().getUid();//online user
        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(current_user_id))
                {

                }
                if(!dataSnapshot.hasChild(current_user_id))
                {

                    String token;
                    String id=mAuth.getUid().toString();
                    String Mail=mAuth.getCurrentUser().getEmail().toString();
                    token= FirebaseInstanceId.getInstance().getToken();
                    int pos1=Mail.indexOf('.');
                    int Apos=Mail.indexOf('@');
                    String userName=Mail.substring(0,pos1);
                    String first=Mail.substring(0,1);
                    first=first.toUpperCase();
                    userName=first+Mail.substring(1,pos1);
                    int pos2=Mail.indexOf('@',pos1+1);
                    String admissionNo=Mail.substring(Apos-8,Apos);
                    int pos3=Mail.indexOf('.',pos2+1);
                    String branch=Mail.substring(pos2+1,pos3);

                    HashMap user1 = new HashMap();
                    user1.put("username", userName);
                    user1.put("department", branch);
                    user1.put("email", Mail);
                    user1.put("admission_number", admissionNo);
                    user1.put("device_token",token);
                    user1.put("type","Student");

                    UsersRef.child(id).updateChildren(user1).addOnCompleteListener(new OnCompleteListener()
                    {
                        @Override
                        public void onComplete(@NonNull Task task)
                        {
                            //progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful())
                            {

                                //if(current_user_id.equals("AkX6MclvgrXpN8oOGI5v37dn7eb2")||current_user_id.equals("HwRTgHAQF4UyfkoP8r0zN3MmO4y2"))
                                //{
                                  //  SendToSetupActivity();
                                //}
                                //else
                                  //  {
                             //       Toast.makeText(MainActivity.this, "Details Saved", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                //}
                            }
                            else
                            {
                                Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                   // if(current_user_id.equals("ZbMXiJTBUBZoEl801QcepnEJO8n2"))
                    //SendToSetupActivity();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void SendToSetupActivity()
    {
        Intent intent=new Intent(MainActivity.this, adminSetup.class);
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


    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

}


