package com.android.sgms_20;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.amulyakhare.textdrawable.TextDrawable;
import com.facebook.drawee.view.SimpleDraweeView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private List<Posts> mPosts;
    boolean LikeChecker=false,DownVoteChecker=false,StarChecker=false;
    private Context mContext;
    private TextDrawable mDrawableBuilder;
    FirebaseAuth mAuth;
    private String type;
    String currentUserId;
    private String mode;
    private  Intent in;



    DatabaseReference UserRef,LikesRef,PostsRef,DownVotesRef,Post,UserReference;

    public PostsAdapter(Context context, List<Posts> posts) {
        mContext = context;
        mPosts= posts;
        mAuth=FirebaseAuth.getInstance();
        currentUserId=mAuth.getCurrentUser().getUid();
        UserReference=FirebaseDatabase.getInstance().getReference().child("Users");

        UserRef=FirebaseDatabase.getInstance().getReference().child("Posts").child(currentUserId).child("star");
        LikesRef=FirebaseDatabase.getInstance().getReference().child("Likes");
        DownVotesRef=FirebaseDatabase.getInstance().getReference().child("DownVotes");
        PostsRef=FirebaseDatabase.getInstance().getReference().child("Posts");
    }




    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, null, false));
    }

    public List<Posts> getPosts()
    {
        return mPosts;
    }

    public void setPosts(List<Posts> posts) {
        this.mPosts = posts;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {

        Posts question = mPosts.get(position);
        String PostKey=question.getPostid();
        Post=FirebaseDatabase.getInstance().getReference().child("Posts").child(PostKey);
        holder.setLikesButtonStatus(PostKey);
        holder.setDownVoteButtonStatus(PostKey);
        holder.setStar(PostKey);
        holder.setCommentCount(PostKey);




        Post.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists()){
                mode=dataSnapshot.child("mode").getValue().toString();
                String postType=dataSnapshot.child("postType").getValue().toString();

                   // if(PostKey.endsWith("AkX6MclvgrXpN8oOGI5v37dn7eb2")||PostKey.endsWith("nO3l336v84OXDNCkR0aFNm0Es1w2")||mode.equals("Private"))
                    if(postType.endsWith("Admin")||postType.endsWith("SubAdmin")||postType.endsWith("Club")||mode.equals("Private"))
                    {
                        //comments hidden for posts from admin,clubs and private
                        holder.cnt.setVisibility(View.GONE);
                        holder.cnt_head.setVisibility(View.GONE);
                        holder.CommentPostButton.setVisibility(View.GONE);
                    }
                    if(mode.equals("Private"))
                    {
                        //disable upvote and downvote
                        holder.LikePostButton.setVisibility(View.GONE);
                        holder.DisplayDownVotes.setVisibility(View.GONE);
                        holder.DisplayNoOfLikes.setVisibility(View.GONE);
                        holder.DownVoteButton.setVisibility(View.GONE);


                    }

                    if(mode.equals("Private"))
                    {
                        holder.textStatus.setVisibility(View.VISIBLE);
                        holder.statusHeading.setVisibility(View.VISIBLE);
                    }
                    if(postType.startsWith("delete"))
                    {
                        holder.textCategory.setVisibility(View.GONE);
                        holder.textSubcategory.setVisibility(View.GONE);
                        holder.textMode.setVisibility(View.GONE);
                        holder.textUid.setVisibility(View.GONE);
                        holder.LikePostButton.setVisibility(View.GONE);
                        holder.DisplayDownVotes.setVisibility(View.GONE);
                        holder.DisplayNoOfLikes.setVisibility(View.GONE);
                        holder.DownVoteButton.setVisibility(View.GONE);
                        holder.textStatus.setVisibility(View.GONE);
                        holder.statusHeading.setVisibility(View.GONE);
                        holder.cnt.setVisibility(View.GONE);
                        holder.cnt_head.setVisibility(View.GONE);
                        holder.CommentPostButton.setVisibility(View.GONE);
                        holder.settings.setVisibility(View.GONE);
                    }


               // Toast.makeText(mContext, mode, Toast.LENGTH_SHORT).show();
            }}

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //Comments will be visible only for posts not from admin and club and only public
        //Status will be shown only for private posts

        //String email=question.getEmail();


       /* */
       /* UserReference.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                type=dataSnapshot.child("type").getValue().toString();
*/


                if(currentUserId.equals("FU5r1KMEvOeQqCU5D8V7FQ4MGQW2"))
        {

            holder.mStar.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    SendUserToSnackBarActivity();

                }
            });
            UserReference.child(currentUserId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    type=dataSnapshot.child("type").getValue().toString();

            //if(currentUserId.equals("AkX6MclvgrXpN8oOGI5v37dn7eb2"))
            if(type.equals("Admin")||type.equals("SubAdmin"))
            {

                holder.pic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent ProIntent = new Intent(mContext, ProItemView.class);
                        ProIntent.putExtra("PostKey", PostKey);
                        mContext.startActivity(ProIntent);

                    }
                });
            }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            /*else{

                holder.pic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent ProIntent = new Intent(mContext, ProItemView.class);
                        ProIntent.putExtra("PostKey", PostKey);
                      //  mContext.startActivity(ProIntent);

                    }
                });

            }*/
            holder.settings.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v)
                {


                    Intent clickPosIntent=new Intent(mContext,ClickPostActivity.class);
                    clickPosIntent.putExtra("PostKey",PostKey);
                    mContext.startActivity(clickPosIntent);

                }
            });
            holder.textQuestion.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v)
                {


                    Intent clickPosIntent=new Intent(mContext,ClickPostActivity.class);
                    clickPosIntent.putExtra("PostKey",PostKey);
                    mContext.startActivity(clickPosIntent);

                }
            });

            holder.LikePostButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    SendUserToSnackBarActivity();

                }
            });
            holder.CommentPostButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent commentsIntent = new Intent(mContext, CommentsActivity.class);
                    commentsIntent.putExtra("PostKey", PostKey);
                    mContext.startActivity(commentsIntent);


                }
            });
            holder.cnt_head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent commentsIntent = new Intent(mContext, CommentsActivity.class);
                    commentsIntent.putExtra("PostKey", PostKey);
                    mContext.startActivity(commentsIntent);


                }
            });

            holder.DownVoteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    SendUserToSnackBarActivity();
                }
            });
        }
        else {

            holder.mStar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {

                    StarChecker = true;

                    PostsRef.child(PostKey).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            {
                            if (StarChecker == true)
                            {
                                if (dataSnapshot.child("star").hasChild(currentUserId))
                                {
                                    PostsRef.child(PostKey).child("star").child(currentUserId).removeValue();
                                    StarChecker = false;
                                    //DownVotesRef.child(PostKey).child(currentUserId).setValue(true);


                                }
                                else
                                    {   HashMap postsMap = new HashMap();
                                        postsMap.put("uid", currentUserId);

                                        postsMap.put("username", question.getName());
                                        postsMap.put("postType",question.getPostType());

                                        PostsRef.child(question.getPostid()).child("star").child(currentUserId).updateChildren(postsMap)

                                        //UserRef.child(question.getPostid()).updateChildren(postsMap)
                                                .addOnCompleteListener(new OnCompleteListener()
                                                {
                                                    @Override
                                                    public void onComplete(@NonNull Task task)
                                                    {
                                                        if(task.isSuccessful())
                                                        {
                                                            Toast.makeText(mContext, "Post is starred", Toast.LENGTH_SHORT).show();
                                                            //loadingBar.dismiss();
                                                        }
                                                        else
                                                        {
                                                            Toast.makeText(mContext, "Error Occured while starring the post.", Toast.LENGTH_SHORT).show();
                                                            //loadingBar.dismiss();
                                                        }
                                                    }
                                                });
                                    StarChecker = false;


}                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }

              });

            holder.mStar.setImageResource(R.drawable.ic_star_selected);

                    UserReference.child(currentUserId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot)
                        {
                            type=dataSnapshot.child("type").getValue().toString();

                            //if(currentUserId.equals("AkX6MclvgrXpN8oOGI5v37dn7eb2"))
                            if(type.equals("Admin")||type.equals("SubAdmin"))
                            {

                                holder.pic.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent ProIntent = new Intent(mContext, ProItemView.class);
                                        ProIntent.putExtra("PostKey", PostKey);
                                        mContext.startActivity(ProIntent);

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
            /*else{

                holder.pic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent ProIntent = new Intent(mContext, ProItemView.class);
                        ProIntent.putExtra("PostKey", PostKey);
                        //  mContext.startActivity(ProIntent);

                    }
                });

            }*/




            holder.settings.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {


                    Intent clickPosIntent = new Intent(mContext, ClickPostActivity.class);
                    clickPosIntent.putExtra("PostKey", PostKey);
                    mContext.startActivity(clickPosIntent);

                }
            });
                    holder.textQuestion.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {


                            Intent clickPosIntent = new Intent(mContext, ClickPostActivity.class);
                            clickPosIntent.putExtra("PostKey", PostKey);
                            mContext.startActivity(clickPosIntent);

                        }
                    });

            holder.CommentPostButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent commentsIntent = new Intent(mContext, CommentsActivity.class);
                    commentsIntent.putExtra("PostKey", PostKey);
                    mContext.startActivity(commentsIntent);


                }
            });
                    holder.cnt_head.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            Intent commentsIntent = new Intent(mContext, CommentsActivity.class);
                            commentsIntent.putExtra("PostKey", PostKey);
                            mContext.startActivity(commentsIntent);


                        }
                    });


            holder.LikePostButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    LikeChecker = true;

                    LikesRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (LikeChecker == true) {
                                if (dataSnapshot.child(PostKey).hasChild(currentUserId)) {
                                    LikesRef.child(PostKey).child(currentUserId).removeValue();
                                    LikeChecker = false;

                                    DownVotesRef.child(PostKey).child(currentUserId).setValue(true);


                                } else {

                                    LikesRef.child(PostKey).child(currentUserId).setValue(true);
                                    DownVotesRef.child(PostKey).child(currentUserId).removeValue();
                                    LikeChecker = false;


                                }
                            }
                            MainActivity.getInstance().sort();
                            //MainActivity.getInstance().finish();
                            //MainActivity(getClass())
                            //mContext.startActivity(new Intent(mContext,MainActivity.class));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }
            });


            holder.DownVoteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    DownVoteChecker = true;
                    DownVotesRef.addValueEventListener(new ValueEventListener()
                    {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (DownVoteChecker == true) {
                                if (dataSnapshot.child(PostKey).hasChild(currentUserId)) {
                                    DownVotesRef.child(PostKey).child(currentUserId).removeValue();
                                    DownVoteChecker = false;

                                    LikesRef.child(PostKey).child(currentUserId).setValue(true);


                                }
                                else
                                    {
                                    DownVotesRef.child(PostKey).child(currentUserId).setValue(true);
                                    DownVoteChecker = false;
                                    LikesRef.child(PostKey).child(currentUserId).removeValue();
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
      Context context = holder.PostImage.getContext();
      String t=question.getPostImage();
      String x= question.getPostPdf();
      if(!t.equals("null"))
      {
          holder.PostImage.setVisibility(View.VISIBLE);

          holder.PostImage.getLayoutParams().height=600;
          holder.PostImage.getLayoutParams().width=600;
          holder.PostImage.requestLayout();

          Picasso.with(context)
                  .load(t)
                  //.fit()
                  .placeholder(R.drawable.loader1)
                  .into(holder.PostImage);



          holder.PostImage.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {


                  Intent clickPosIntent = new Intent(mContext, ClickPhotoActivity.class);
                  clickPosIntent.putExtra("PostKey", PostKey);
                  clickPosIntent.putExtra("URL",t);
                  mContext.startActivity(clickPosIntent);


              }
          });

      }else if(!x.equals("null")){

          holder.PostImage.setVisibility(View.VISIBLE);
          holder.PostImage.getLayoutParams().height=300;
          holder.PostImage.getLayoutParams().width=300;
          holder.PostImage.requestLayout();

          holder.PostImage.setImageResource(R.drawable.pdf);

          holder.PostImage.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(x));
                  holder.itemView.getContext().startActivity(intent);
              }
          });

      }
      else
          {
          holder.PostImage.setVisibility(View.GONE);
      }




        holder.textMode.setText(question.getMode());
        //holder.textUid.setText(question.getUid());
        //holder.textSubcategory.setText(question.getSubcategory());

        holder.textCategory.setText(question.getCategory());
        holder.textAuthorName.setText(question.getName());
        holder.textJobTitle.setText(question.getEmail());
        holder.textDate.setText(question.getDate());
        //int charCount=question.getDescription().length();
        String txt=question.getDescription();
        if(txt.equals("")){
            holder.textQuestion.setVisibility(View.GONE);
        }else{
            //holder.textQuestion.setText(question.getDescription());
            if(question.getDescription().length()>110)
            {
                holder.textQuestion.setText(question.getDescription().substring(0,106)+".....");
            }
            else
            {
                holder.textQuestion.setText(question.getDescription());
            }
        }

        Tag firstTag = question.getTags().get(0);
        holder.textCategory.setText(firstTag.getText());
        Tag secondTag = question.getTags().get(1);
        holder.textSubcategory.setText(secondTag.getText());
        Tag thirdTag= question.getTags().get(2);
        holder.textMode.setText(thirdTag.getText());
        holder.textStatus.setText(question.getStatus());
        Tag fourthTag= question.getTags().get(3);
        holder.textUid.setText(fourthTag.getText());


        char letter = question.getName().charAt(0);
       letter = Character.toUpperCase(letter);



        mDrawableBuilder = TextDrawable.builder().buildRound(String.valueOf(letter), R.color.colorAccent);
       holder.pic.setImageDrawable(mDrawableBuilder);
      // else holder.pic.setImageURI(imgUri);

        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(1000);
        //drawable.setSize(40,15);
        drawable.setColor(firstTag.getColor());
        holder.textCategory.setBackgroundDrawable(drawable);
        GradientDrawable drawable1 = new GradientDrawable();
        drawable1.setCornerRadius(1000);

        drawable1.setColor(secondTag.getColor());
        holder.textSubcategory.setBackgroundDrawable(drawable1);

        GradientDrawable drawable2 = new GradientDrawable();
        drawable2.setCornerRadius(1000);
        drawable2.setColor(thirdTag.getColor());
        holder.textMode.setBackgroundDrawable(drawable2);
        GradientDrawable drawable3 = new GradientDrawable();
        drawable3.setCornerRadius(1000);
        drawable3.setColor(fourthTag.getColor());
        holder.textUid.setBackgroundDrawable(drawable3);




    }

    private void SendUserToSnackBarActivity()
    {
        Intent loginIntent=new Intent(mContext,SnackBarActivity.class);
        mContext.startActivity(loginIntent);
    }

    private int getColor(int color) {
        return ContextCompat.getColor(mContext, color);
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        View mView;
        AppCompatImageView LikePostButton,CommentPostButton,settings,DownVoteButton;
        ImageView pro;
        TextView DisplayNoOfLikes,DisplayDownVotes;
        int CountLikes,CountDownVotes;
        String currentUserId;
        DatabaseReference LikesRef,DownVotesRef,CommentsRef;

        ImageView mStar;
        TextView textAuthorName;
        TextView textMode;
        TextView textUid,cnt,cnt_head;
        TextView textJobTitle;
        TextView textDate;
        TextView textQuestion;
        TextView textCategory;
        TextView textSubcategory;
        TextView textStatus,statusHeading;
        ImageView pic;
        ImageView PostImage;
        ImageView PostImage1;








        public ViewHolder(View itemView) {
            super(itemView);
            mView=itemView;

            mStar=(ImageView)mView.findViewById(R.id.star);
            settings=(AppCompatImageView)mView.findViewById(R.id.view_settings);
            LikePostButton=(AppCompatImageView)mView.findViewById(R.id.view_likes);
            CommentPostButton=(AppCompatImageView) mView.findViewById(R.id.view_chat);
            DisplayNoOfLikes=(TextView)mView.findViewById(R.id.text_likes_count);
            pro=mView.findViewById(R.id.avatar);
            DownVoteButton=mView.findViewById(R.id.view_downVotes);
            DisplayDownVotes=mView.findViewById(R.id.text_downVotes_count);
            LikesRef=FirebaseDatabase.getInstance().getReference().child("Likes");
            CommentsRef=FirebaseDatabase.getInstance().getReference().child("Posts");
            DownVotesRef=FirebaseDatabase.getInstance().getReference().child("DownVotes");
            currentUserId= FirebaseAuth.getInstance().getCurrentUser().getUid();
            pic=itemView.findViewById(R.id.avatar);
            cnt=itemView.findViewById(R.id.text_chat_count);
            cnt_head=itemView.findViewById(R.id.text_chat_count1);

            textAuthorName = (TextView) itemView.findViewById(R.id.text_name);
            textJobTitle = (TextView) itemView.findViewById(R.id.text_job_title);
            textDate = (TextView) itemView.findViewById(R.id.text_date);
            textQuestion = (TextView) itemView.findViewById(R.id.text_question);
            textQuestion.setMovementMethod(LinkMovementMethod.getInstance());
            textCategory = (TextView) itemView.findViewById(R.id.filter_first);
            textMode=(TextView)itemView.findViewById(R.id.filter_third);
            textUid=(TextView)itemView.findViewById(R.id.filter_fourth);
            textStatus=itemView.findViewById(R.id.status);
            statusHeading=itemView.findViewById(R.id.statusheading);
            textSubcategory= (TextView) itemView.findViewById(R.id.filter_second);
            PostImage=itemView.findViewById(R.id.postImage);
            PostImage1=itemView.findViewById(R.id.postImage1);





        }
        public void setStar(final String PostKey)
        {
          PostsRef.addValueEventListener(new ValueEventListener()
          {
              @Override
              public void onDataChange(DataSnapshot dataSnapshot)
              {
                  if(dataSnapshot.child(PostKey).child("star").hasChild(currentUserId))
                  {
                      mStar.setImageResource(R.drawable.ic_star_selected);
                  }
                  else
                  {
                      mStar.setImageResource(R.drawable.ic_star_unselected);
                  }
              }

              @Override
              public void onCancelled(DatabaseError databaseError) {

              }
          })  ;
        }
        public void setLikesButtonStatus(final String PostKey)
        //public void setLikesButtonStatus()
        {

            LikesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(PostKey).hasChild(currentUserId))
                    {
                        CountLikes=(int)dataSnapshot.child(PostKey).getChildrenCount();
                        LikePostButton.setImageResource(R.drawable.upvote);
                        DisplayNoOfLikes.setText(Integer.toString(CountLikes));
                        HashMap useMap = new HashMap();
                        useMap.put("likes", Integer.toString(CountLikes));
                        if(!(PostsRef.child(PostKey).equals(null)))
                        PostsRef.child(PostKey).updateChildren(useMap);

                    }
                    else
                    {
                        CountLikes=(int)dataSnapshot.child(PostKey).getChildrenCount();
                        LikePostButton.setImageResource(R.drawable.ic);

                        DisplayNoOfLikes.setText(Integer.toString(CountLikes));
                        HashMap useMap = new HashMap();
                        useMap.put("likes", Integer.toString(CountLikes));
                        PostsRef.child(PostKey).updateChildren(useMap);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


        public void setDownVoteButtonStatus(final String PostKey)
        //public void setLikesButtonStatus()
        {

            DownVotesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(PostKey).hasChild(currentUserId))
                    {
                        CountDownVotes=(int)dataSnapshot.child(PostKey).getChildrenCount();
                        DownVoteButton.setImageResource(R.drawable.downvote);
                        DisplayDownVotes.setText(Integer.toString(CountDownVotes));


                    }
                    else
                    {
                        CountDownVotes=(int)dataSnapshot.child(PostKey).getChildrenCount();
                        DownVoteButton.setImageResource(R.drawable.arrows);

                        DisplayDownVotes.setText(Integer.toString(CountDownVotes));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


        public void setCommentCount(String postKey) {

            CommentsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(postKey).child("Comments").exists()){
                        int count = (int)dataSnapshot.child(postKey).child("Comments").getChildrenCount();
                        cnt.setText(Integer.toString(count));
                        if(count==1){
                            cnt_head.setText("Comment");
                        }else{
                            cnt_head.setText("Comments");
                        }

                    }else{
                        cnt.setText(Integer.toString(0));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {


                }
            });


        }
    }
}

