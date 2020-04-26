package com.android.sgms_20;



import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {


    private List<Posts> mPosts;
    boolean LikeChecker=false;
    private Context mContext;
    FirebaseAuth mAuth;
    String currentUserId;
    private  Intent in;

    DatabaseReference LikesRef,PostsRef;

    public PostsAdapter(Context context, List<Posts> posts) {
        mContext = context;
        mPosts= posts;
        mAuth=FirebaseAuth.getInstance();
        currentUserId=mAuth.getCurrentUser().getUid();
        LikesRef=FirebaseDatabase.getInstance().getReference().child("Likes");
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
    public void onBindViewHolder(ViewHolder holder, int position) {



        Posts question = mPosts.get(position);
        String PostKey=question.getPostid();




        //String PostKey=question.getUid().toString();
      //  String PostKey=PostsRef.child("PostKey").toString();

        holder.setLikesButtonStatus(PostKey);
        holder.settings.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {


                Intent clickPosIntent=new Intent(mContext,ClickPostActivity.class);
                clickPosIntent.putExtra("PostKey",PostKey);
                mContext.startActivity(clickPosIntent);

            }
        });

        holder.CommentPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {


                Intent commentsIntent=new Intent(mContext,CommentsActivity.class);
                commentsIntent.putExtra("PostKey",PostKey);
                mContext.startActivity(commentsIntent);


            }
        });

        holder.LikePostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {


                LikeChecker=true;
                LikesRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(LikeChecker==true)
                        {
                            if(dataSnapshot.child(PostKey).hasChild(currentUserId))
                            {
                                LikesRef.child(PostKey).child(currentUserId).removeValue();
                                LikeChecker=false;

                            }
                            else
                            {

                                LikesRef.child(PostKey).child(currentUserId).setValue(true);
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

        holder.textMode.setText(question.getMode());
        //holder.textUid.setText(question.getUid());
        //holder.textSubcategory.setText(question.getSubcategory());

        holder.textCategory.setText(question.getCategory());
        holder.textAuthorName.setText(question.getName());
        holder.textJobTitle.setText(question.getEmail());
        holder.textDate.setText(question.getDate());
        holder.textQuestion.setText(question.getDescription());
        Tag firstTag = question.getTags().get(0);
        holder.textCategory.setText(firstTag.getText());
        Tag secondTag = question.getTags().get(1);
        holder.textSubcategory.setText(secondTag.getText());
        Tag thirdTag= question.getTags().get(2);
        holder.textMode.setText(thirdTag.getText());

        Tag fourthTag= question.getTags().get(3);
        holder.textUid.setText(fourthTag.getText());


        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(1000);
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




        /*Question question = mQuestions.get(position);


        holder.textAuthorName.setText(question.getAuthorName());
        holder.textJobTitle.setText(question.getAuthorJobTitle());
        holder.textDate.setText(question.getDate());
        holder.textQuestion.setText(question.getText());
        Tag firstTag = question.getTags().get(0);
        holder.firstFilter.setText(firstTag.getText());
        Tag secondTag = question.getTags().get(1);
        holder.secondFilter.setText(secondTag.getText());

        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(1000);
        drawable.setColor(firstTag.getColor());
        holder.firstFilter.setBackgroundDrawable(drawable);
        GradientDrawable drawable1 = new GradientDrawable();
        drawable1.setCornerRadius(1000);
        drawable1.setColor(secondTag.getColor());
        holder.secondFilter.setBackgroundDrawable(drawable1);*/
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
        AppCompatImageView LikePostButton,CommentPostButton,settings;
        TextView DisplayNoOfLikes;
        int CountLikes;
        String currentUserId;
        DatabaseReference LikesRef;


        TextView textAuthorName;
        TextView textMode;
        TextView textUid;
        TextView textJobTitle;
        TextView textDate;
        TextView textQuestion;
        TextView textCategory;
        TextView textSubcategory;









        public ViewHolder(View itemView) {
            super(itemView);
            mView=itemView;

            settings=(AppCompatImageView)mView.findViewById(R.id.view_settings);
            LikePostButton=(AppCompatImageView)mView.findViewById(R.id.view_likes);
            CommentPostButton=(AppCompatImageView) mView.findViewById(R.id.view_chat);
            DisplayNoOfLikes=(TextView)mView.findViewById(R.id.text_likes_count);

            LikesRef=FirebaseDatabase.getInstance().getReference().child("Likes");



            currentUserId= FirebaseAuth.getInstance().getCurrentUser().getUid();

            textAuthorName = (TextView) itemView.findViewById(R.id.text_name);
            textJobTitle = (TextView) itemView.findViewById(R.id.text_job_title);
            textDate = (TextView) itemView.findViewById(R.id.text_date);
            textQuestion = (TextView) itemView.findViewById(R.id.text_question);
            textCategory = (TextView) itemView.findViewById(R.id.filter_first);
            textMode=(TextView)itemView.findViewById(R.id.filter_third);
            textUid=(TextView)itemView.findViewById(R.id.filter_fourth);

            textSubcategory= (TextView) itemView.findViewById(R.id.filter_second);

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
                        LikePostButton.setImageResource(R.drawable.ic_like);
                        DisplayNoOfLikes.setText(Integer.toString(CountLikes));
                    }
                    else
                    {
                        CountLikes=(int)dataSnapshot.child(PostKey).getChildrenCount();
                        LikePostButton.setImageResource(R.drawable.ic_heart);

                        DisplayNoOfLikes.setText(Integer.toString(CountLikes));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }
}

